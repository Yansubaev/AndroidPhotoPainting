package com.yans.painting.fragments.camerapreview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yans.painting.activity.MainActivity
import com.yans.painting.databinding.FragmentCameraPreviewBinding
import com.yans.painting.sessions.CameraSession

class CameraPreviewFragment : Fragment() {

    private lateinit var binding: FragmentCameraPreviewBinding

    private lateinit var act: MainActivity

    private lateinit var cameraSession: CameraSession

    private val onPhotoAvailable: (bitmap: Bitmap) -> Unit = { editPhoto(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCameraPreviewBinding.inflate(inflater, container, false)
        act = requireActivity() as MainActivity
        cameraSession = CameraSession(act, binding.texCameraPreview)

        binding.btnTakePhoto.setOnClickListener {
            takePhoto()
        }

        return binding.root
    }

    private fun takePhoto() {
        cameraSession.takePhoto()
    }

    private fun editPhoto(bitmap: Bitmap) {
        act.runOnUiThread {
            act.openEditPhotoFragment(bitmap)
        }
    }

    override fun onResume() {
        super.onResume()
        cameraSession.resumeSession()
        cameraSession.addOnPhotoAvailableListener(onPhotoAvailable)
    }

    override fun onPause() {
        super.onPause()
        cameraSession.pauseSession()
        cameraSession.removeOnPhotoAvailableListener(onPhotoAvailable)
    }

}