package com.yans.painting.fragments.photoediting

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yans.painting.R
import com.yans.painting.activity.MainActivity
import com.yans.painting.constants.Constants
import com.yans.painting.databinding.FragmentPhotoEditingBinding
import com.yans.painting.fragments.colorpicker.ColorListFragment
import com.yans.painting.fragments.thicknesspicker.BrushThicknessFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class PhotoEditingFragment() : Fragment() {

    var photoBitmap: Bitmap? = null

    private lateinit var binding: FragmentPhotoEditingBinding

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(PhotoEditingViewModel::class.java)
    }

    private lateinit var act: MainActivity

    private var colorFragment: ColorListFragment? = null

    private var thicknessFragment: BrushThicknessFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoEditingBinding.inflate(inflater, container, false)
        act = requireActivity() as MainActivity

        photoBitmap?.let {
            binding.pntEditable.setImageBitmap(it)
        }

        colorFragment = ColorListFragment()
        thicknessFragment = BrushThicknessFragment()

        binding.btnBrushColor.setOnClickListener {
            fragmentManager?.let {
                colorFragment?.show(it, "color picker fragment")
            }
        }
        binding.btnBrushThickness.setOnClickListener {
            fragmentManager?.let {
                thicknessFragment?.show(it, "thickness picker fragment")
            }
        }
        binding.btnSave.setOnClickListener {
            checkPermissionsAndSavePhoto()
        }

        initViewModel()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        colorFragment?.setOnColorPickedListener { color ->
            viewModel.setBrushColor(color)
        }
        thicknessFragment?.setOnThicknessPickedListener { thickness ->
            viewModel.setBrushThickness(thickness)
        }
    }

    override fun onPause() {
        super.onPause()
        colorFragment?.removeOnColorPickedListener()
        thicknessFragment?.removeOnThicknessPickedListener()
    }

    private fun initViewModel() {
        viewModel.colorData.observe(this) { color ->
            (binding.imgBrushColorOutline.background as GradientDrawable).setTint(color)
            binding.pntEditable.setBrushColor(color)
        }
        viewModel.thicknessData.observe(this) { thickness ->
            (binding.imgBrushThickness).apply {
                val p = layoutParams as ConstraintLayout.LayoutParams
                p.width = thickness
                p.height = thickness
                layoutParams = p
            }
            binding.pntEditable.setBrushThickness(thickness)
        }
    }

    private fun checkPermissionsAndSavePhoto(){
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permissions[0]
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                permissions[1]
            ) != PackageManager.PERMISSION_GRANTED
        ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions,
                    Constants.REQUEST_STORAGE_PERMISSION
                )
        } else {
            savePaintedPhoto()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.REQUEST_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(requireContext(), "No permission granted", Toast.LENGTH_LONG).show()
            } else {
                savePaintedPhoto()
            }
        }
    }

    private fun savePaintedPhoto() {
        val bmp = binding.pntEditable.getPaintedImage()
        saveIntoAppStorage(bmp)
        return

        val dataFormat = SimpleDateFormat("yyyy-MM-dd_hh-mm-ss", Locale.getDefault())
        val name = dataFormat.format(Calendar.getInstance().time)
        saveToGallery(name, bmp)
    }

    private fun saveIntoAppStorage(bmp: Bitmap){
        val dataFormat = SimpleDateFormat("yyyy-MM-dd_hh-mm-ss", Locale.getDefault())
        val name = dataFormat.format(Calendar.getInstance().time) + ".jpg"
        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            name
        )
        try {
            FileOutputStream(file).use { out ->
                if(bmp.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    Toast.makeText(requireContext(), R.string.saves_success, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun saveToGallery(name: String, bmp: Bitmap): Boolean {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/PhotoPainting")
                put(MediaStore.MediaColumns.IS_PENDING, true)
            }
        }
        val resolver = requireContext().applicationContext.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try {
            uri?.let { uri ->
                resolver.openOutputStream(uri)?.let { stream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream)) {
                        throw IOException("Failed to save bitmap.")
                    } else {
                        Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                    }
                    resolver.update(uri, contentValues, null, null)
                } ?: throw IOException("Failed to get output stream.")

            } ?: throw IOException("Failed to create new MediaStore record")

        } catch (e: IOException) {
            if (uri != null) {
                resolver.delete(uri, null, null)
            }
            return false
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, false)
        }
        return true

    }
}