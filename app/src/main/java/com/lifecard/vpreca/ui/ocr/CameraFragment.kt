package com.lifecard.vpreca.ui.ocr

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class CameraFragment : Fragment() {
    companion object {
        fun newInstance() = CameraFragment()

        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CameraViewModel by viewModels()
    private var imageCapture: ImageCapture? = null
    private var _cameraProvider: ProcessCameraProvider? = null
    private val cameraProvider get() = _cameraProvider!!
    private var cameraViewOld: CameraView? = null

    private val args: CameraFragmentArgs by navArgs()

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

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            // Request camera permissions
            requestMultiplePermissions.launch(REQUIRED_PERMISSIONS)
        }

        binding.buttonTakePhoto.setOnClickListener(View.OnClickListener {
            takePhoto()
        })
        viewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer { loading ->
            when (loading) {
                true -> {
                    binding.buttonCancel.isEnabled = false
                    showLoadingDialog()
                }
                else -> {
                    binding.buttonCancel.isEnabled = true
                    hideLoadingDialog()
                }
            }
        })
        viewModel.codeOcr.observe(viewLifecycleOwner, androidx.lifecycle.Observer { ocr ->
            //show toast
            when (ocr.isNullOrEmpty()) {
                true -> showAlertErrorOcr()
                else -> {
                    setNavigationResult(ocr, "ocr_code")
                    findNavController().popBackStack()
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, androidx.lifecycle.Observer { message ->
            //show alert error
            if (!message.isNullOrEmpty()) {
                showAlertErrorOcr()
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
            setPositiveButton(
                R.string.button_retry, null
            )
            setTitle(R.string.camera_ocr_failure)
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
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startCameraX()
        } else {
            startCameraBellow21()
        }
    }

    private fun takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            takePhotoX()
        } else {
            takePhotoBellow21()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startCameraX() {
        val cameraPreviewBinding =
            LayoutCameraPreviewBinding.inflate(LayoutInflater.from(requireContext()))
        val cameraPreview = cameraPreviewBinding.cameraPreview

        binding.cameraContainer.addView(
            cameraPreview,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            _cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()
            val useCaseGroup = UseCaseGroup.Builder()
                .addUseCase(preview)
                .addUseCase(imageCapture!!)
                .setViewPort(cameraPreview.viewPort!!)
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, useCaseGroup
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePhotoX() {
        viewModel.startTakePhoto()

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return
        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                requireActivity().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    exc.printStackTrace()
                    viewModel.releaseLockTakePhoto()
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri?.let { imageUri ->
                        val percents = getPercentCrop()
                        viewModel.getCodeByGoogleVisionOcr(
                            requireContext(),
                            imageUri,
                            percents[0],
                            percents[1]
                        )

                        requireContext().contentResolver.delete(imageUri, null, null)
                    }

                    _cameraProvider?.let { cameraProvider ->
                        {
                            cameraProvider.unbindAll()
                        }
                    }
                }
            }
        )
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
                        viewModel.getCodeByGoogleVisionOcrByBitmap(
                            bitmap,
                            percents[0],
                            percents[1]
                        )
                    }
                        ?: kotlin.run {
                            //show alert
                            showAlertErrorOcr()
                        }
                })
            }
        })
    }

    private fun takePhotoBellow21() {
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

}