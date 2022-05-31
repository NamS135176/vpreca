package com.lifecard.vpreca.ui.ocr

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.opengl.ETC1.encodeImage
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.databinding.FragmentCameraBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        binding.buttonCancel.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Request camera permissions
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                requestMultiplePermissions.launch(REQUIRED_PERMISSIONS)
            }
        }

        binding.buttonTakePhoto.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                takePhoto()
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer { loading ->
            when (loading) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        })
        viewModel.codeOcr.observe(viewLifecycleOwner, androidx.lifecycle.Observer { ocr ->
            //show toast
            when (ocr.isNullOrEmpty()) {
                true -> showToast("Can not detect ocr", toastType = ToastType.Error)
                else -> {
                    setNavigationResult(ocr, "ocr_code")
                    findNavController().popBackStack()
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, androidx.lifecycle.Observer { message ->
            //show toast
            if (!message.isNullOrEmpty()) {
                showToast(message, toastType = ToastType.Error)
            }
        })
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            _cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePhoto() {
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
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    output.savedUri?.let { imageUri ->
                        val imageStream =
                            requireActivity().contentResolver.openInputStream(imageUri)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        viewModel.getCodeByGoogleVisionOcr(selectedImage)
                    }
                    println(msg)

                    _cameraProvider?.let { cameraProvider ->
                        {
                            cameraProvider.unbindAll()
                        }
                    }
                }
            }
        )
    }

}