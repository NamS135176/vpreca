package com.lifecard.vpreca.ui.ocr

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.core.impl.utils.futures.FutureCallback
import androidx.camera.core.impl.utils.futures.Futures
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.util.concurrent.ListenableFuture
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentCameraBinding
import com.lifecard.vpreca.databinding.LayoutCameraPreviewBinding
import com.lifecard.vpreca.databinding.LayoutCameraViewOldBinding
import com.lifecard.vpreca.utils.*
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraFragment : Fragment() {
    companion object {
        fun newInstance() = CameraFragment()

        private val REQUIRED_PERMISSIONS =
            mutableListOf(Manifest.permission.CAMERA).toTypedArray()
    }

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CameraViewModel by viewModels()
    private var imageCapture: ImageCapture? = null
    private var _cameraProvider: ProcessCameraProvider? = null
    private val cameraProvider get() = _cameraProvider!!
    private var cameraViewOld: CameraView? = null
    private var camera: Camera? = null
    private lateinit var cameraController: LifecycleCameraController
    private val args: CameraFragmentArgs by navArgs()

    @androidx.camera.core.ExperimentalGetImage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
        val textCaptionHint = binding.textCaption
        args.hint?.let { textCaptionHint.text = it }

        binding.buttonCancel.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })

        binding.buttonTakePhoto.setOnClickListener(View.OnClickListener {
            takePhoto()
        })
        viewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer { loading ->
            when (loading) {
                true -> {
                    binding.buttonCancel.isEnabled = false
                    showLoadingCameraDialog()
                }
                else -> {
                    binding.buttonCancel.isEnabled = true
                    hideLoadingCameraDialog()
                }
            }
        })
        viewModel.codeOcr.observe(viewLifecycleOwner, androidx.lifecycle.Observer { ocr ->
            //show toast
            when (ocr.isNullOrEmpty()) {
                true -> {
                    val builder =   MaterialAlertDialogBuilder(requireContext()).apply {
                        setMessage("?????????????????????????????????")
                    }.create()
                    builder.setCancelable(false)
                    builder.show()
                    Handler().postDelayed({
                        builder.dismiss()
                        findNavController().popBackStack()
                    }, 3000)
//                    findNavController().popBackStack()
                }
                else -> {
                    setNavigationResult(ocr, "ocr_code")
                    findNavController().popBackStack()
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, androidx.lifecycle.Observer { message ->
            //show alert error
            if (!message.isNullOrEmpty()) {
               val builder =  MaterialAlertDialogBuilder(requireContext()).apply {
                    setMessage("?????????????????????????????????")
                }.create()
                builder.setCancelable(false)
                builder.show()
                Handler().postDelayed({
                    builder.dismiss()
                    findNavController().popBackStack()
                }, 3000)
//                findNavController().popBackStack()
            }
        })
        viewModel.networkTrouble.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isError ->
                if (isError) {
                    showInternetTrouble()
                }
            }
        )
        viewModel.lockButtonTakePhoto.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { locked ->
                binding.buttonTakePhoto.isEnabled = !locked
                binding.buttonTakePhoto.background = if (locked) ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_button_camera_disable
                ) else ContextCompat.getDrawable(requireContext(), R.drawable.ic_button_camera)
            })

        viewModel.lockTakePhoto()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            // Request camera permissions
            requestMultiplePermissions.launch(REQUIRED_PERMISSIONS)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraViewOld?.destroy()
    }

    override fun onPause() {
        super.onPause()
        cameraViewOld?.close()
    }

    override fun onResume() {
        super.onResume()
        cameraViewOld?.open()
    }

    private fun showAlertErrorOcr() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setMessage(R.string.camera_ocr_failure)
        }.create().show()
    }

    //    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                println("${it.key} = ${it.value}")
            }
            if (allPermissionsGranted()) {
                startCamera()
            }
            else{
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setPositiveButton("????????????") { _, _ ->
                        // do something on positive button click
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+requireActivity().packageName)))
                    }
                    setNegativeButton("???????????????", null)
                    setTitle("????????????????????????????????????")
                    setMessage("???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????")
                }.create().show()
            }
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        viewModel.releaseLockTakePhoto()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startCameraX()
        } else {
            startCameraBellow21()
        }
    }

    @androidx.camera.core.ExperimentalGetImage
    private fun takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            takePhotoX()
        } else {
            takePhotoBellow21()
        }
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startCameraX() {
        val cameraPreviewBinding =
            LayoutCameraPreviewBinding.inflate(LayoutInflater.from(requireContext()))
        val cameraPreview = cameraPreviewBinding.cameraPreview
        cameraController = LifecycleCameraController(requireContext())
        cameraController.bindToLifecycle(viewLifecycleOwner)
        cameraController.isTapToFocusEnabled = true
        cameraPreview.controller = cameraController

        binding.cameraContainer.addView(
            cameraPreview,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                _cameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(cameraPreview.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder()
                    .apply {
                        view?.display?.rotation?.let { setTargetRotation(it) }
                        try {
                            setMaxResolution(Size(1024, 1024 * 16 / 9))
                        } catch (e: Exception) {
                        }
                    }
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setJpegQuality(100)
                    .build()

                val useCaseGroup = UseCaseGroup.Builder()
                    .apply {
                        addUseCase(preview)
                        imageCapture?.let { addUseCase(it) }
                        cameraPreview.viewPort?.let { setViewPort(it) }
                    }
                    .build()

                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                camera = cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, useCaseGroup
                )

                cameraPreview.previewStreamState.observe(viewLifecycleOwner) { value ->
                    if (value.equals(PreviewView.StreamState.STREAMING)) {
                        val x = (binding.focusLeft.x + binding.focusLeft.width) / 2
                        val y = binding.focusLeft.y + binding.focusLeft.height / 2

                        setUpCameraAutoFocus(cameraPreview, x, y)
                    }
                }
            } catch (exc: Exception) {
                println(exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpCameraAutoFocus(previewView: PreviewView, x: Float, y: Float) {
        cameraController.cameraControl?.let { cameraControl ->
            val pointFactory: MeteringPointFactory = previewView.meteringPointFactory
            val afPointWidth = 1.0f / 6.0f // 1/6 total area
            val aePointWidth = afPointWidth * 1.5f
            val afPoint = pointFactory.createPoint(x, y, afPointWidth)
            val aePoint = pointFactory.createPoint(x, y, aePointWidth)
            val future: ListenableFuture<FocusMeteringResult> =
                cameraControl.startFocusAndMetering(
                    FocusMeteringAction.Builder(
                        afPoint,
                        FocusMeteringAction.FLAG_AF
                    ).addPoint(
                        aePoint,
                        FocusMeteringAction.FLAG_AE
                    ).build()
                )

            Futures.addCallback(future, object : FutureCallback<FocusMeteringResult?> {
                override fun onSuccess(@Nullable result: FocusMeteringResult?) {}
                override fun onFailure(t: Throwable) {
                    // Throw the unexpected error.
                    throw RuntimeException(t)
                }
            }, CameraXExecutors.directExecutor())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            _cameraProvider?.unbindAll()
        }
    }

    @androidx.camera.core.ExperimentalGetImage
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePhotoX() {
        viewModel.startTakePhoto()

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(ContextCompat.getMainExecutor(requireContext()), object :
            ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val bitmap = convertImageProxyToBitmap(image)

                image.close()

                val percents = getPercentCrop()

                viewModel.ocrTextractDetect(
                    requireContext(),
                    bitmap,
                    image.imageInfo.rotationDegrees,
                    percents[0],
                    percents[1]
                )
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                println(exception)
                viewModel.releaseLockTakePhoto(true)
            }
        })
    }

    private fun startCameraBellow21() {
        val cameraOldBinding =
            LayoutCameraViewOldBinding.inflate(LayoutInflater.from(requireContext()))
        cameraViewOld = cameraOldBinding.camera
        cameraViewOld?.setLifecycleOwner(viewLifecycleOwner)


        binding.cameraContainer.addView(
            cameraViewOld,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        cameraViewOld?.open()
        cameraViewOld?.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap(1024, 1024, BitmapCallback { bitmap ->
                    bitmap?.let {
                        val percents = getPercentCrop()
                        viewModel.ocrTextractDetect(
                            requireContext(),
                            bitmap,
                            0,
                            percents[0],
                            percents[1]
                        )
                    }
                        ?: kotlin.run {
                            viewModel.releaseLockTakePhoto(true)
                            //show alert
//                            findNavController().popBackStack()
                            val builder =   MaterialAlertDialogBuilder(requireContext()).apply {
                                setMessage("?????????????????????????????????")
                            }.create()
                            builder.setCancelable(false)
                            builder.show()
                            Handler().postDelayed({
                                builder.dismiss()
                                findNavController().popBackStack()
                            }, 3000)
                        }
                })
            }
        })
    }

    private fun takePhotoBellow21() {
        viewModel.startTakePhoto()
        cameraViewOld?.takePicture()
    }

    private fun getPercentCrop(): FloatArray {
        val percents = FloatArray(2)
        //calculate crop image
        val statusBarHeight = getStatusBarHeight()
        val offset = Converter.convertDpToPixel(20f, requireContext())
        val location = IntArray(2)
        binding.focusLeft.getLocationOnScreen(location)
        val height = binding.focusLeft.measuredHeight
        val parentHeight = binding.cameraContainer.measuredHeight
        val y = location[1] - statusBarHeight - offset
        val percentTop = y / parentHeight
        val percentHeight = (height + 2 * offset) / parentHeight
        percents[0] = percentTop
        percents[1] = percentHeight
        return percents
    }

    @androidx.camera.core.ExperimentalGetImage
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun convertImageProxyToBitmap(image: ImageProxy): Bitmap {
        return if (image.format == ImageFormat.YUV_420_888) {
            image.image?.convertImageProxyToBitmap()!!
        } else if (image.format == ImageFormat.JPEG) {
            image.convertImageProxyToBitmap()
        } else {
            image.image?.convertImageProxyToBitmap()!!
        }
    }

}