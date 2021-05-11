package com.yans.painting.activity

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yans.painting.R
import com.yans.painting.constants.Constants.REQUEST_CAMERA_PERMISSION
import com.yans.painting.databinding.ActivityMainBinding
import com.yans.painting.fragments.camerapreview.CameraPreviewFragment
import com.yans.painting.fragments.photoediting.PhotoEditingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var previewFragment: CameraPreviewFragment

    private lateinit var editingFragment: PhotoEditingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewFragment = CameraPreviewFragment()
        editingFragment = PhotoEditingFragment()

        supportFragmentManager.beginTransaction().apply {
            remove(editingFragment)
            add(R.id.lay_fragments_container, previewFragment)
            commit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "No permission granted", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    fun openEditPhotoFragment(bitmap: Bitmap){
        editingFragment.photoBitmap = bitmap
        supportFragmentManager.beginTransaction().apply {
            remove(previewFragment)
            add(R.id.lay_fragments_container, editingFragment)
            commit()
        }
    }

}