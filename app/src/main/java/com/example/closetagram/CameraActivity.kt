package com.example.closetagram

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.closetagram.camerax.CameraManager
import com.example.closetagram.databinding.ActivityCameraBinding
import org.koin.android.viewmodel.ext.android.viewModel

class CameraActivity: BaseActivity() {

    private val binding by binding<ActivityCameraBinding> (R.layout.activity_camera)
    private val viewModel: CameraViewModel by viewModel()
    private lateinit var cameraManager: CameraManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        createCameraManager()
        binding.apply {
            lifecycleOwner = this@CameraActivity
            viewModel = this@CameraActivity.viewModel
            initViewModel()
        }
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun initViewModel() {
        viewModel.apply {
            onItemSelectedEvent.observe(::getLifecycle) {
                cameraManager.changeAnalyzer(it)
            }
            onFabButtonEvent.observe(::getLifecycle) {
                it?.let {
                    cameraManager.takeImage()
                    binding.bottomNavigationViewFinder.transform(binding.fabFinder)

                }
            }
        }
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            binding.previewViewFinder,
            this,
            binding.graphicOverlayFinder
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }
}