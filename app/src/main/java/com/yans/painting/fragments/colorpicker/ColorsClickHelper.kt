package com.yans.painting.fragments.colorpicker

import android.view.View
import android.widget.ImageButton
import com.yans.painting.databinding.FragmentColorListBinding

class ColorsClickHelper(binding: FragmentColorListBinding) : View.OnClickListener {

    private val onColorPickedListener: MutableList<(color: Int) -> Unit> = mutableListOf()

    init {
        binding.imageButton1.setOnClickListener(this)
        binding.imageButton2.setOnClickListener(this)
        binding.imageButton3.setOnClickListener(this)
        binding.imageButton4.setOnClickListener(this)
        binding.imageButton5.setOnClickListener(this)
        binding.imageButton6.setOnClickListener(this)
        binding.imageButton7.setOnClickListener(this)
        binding.imageButton8.setOnClickListener(this)
        binding.imageButton9.setOnClickListener(this)
        binding.imageButton10.setOnClickListener(this)
        binding.imageButton11.setOnClickListener(this)
        binding.imageButton12.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null && v is ImageButton) {
            val color = v.backgroundTintList?.defaultColor
            color?.let { color ->
                onColorPickedListener.forEach { it(color) }
            }
        } else {
            return
        }
    }

    fun addOnColorPickedListener(listener: (color: Int) -> Unit){
        onColorPickedListener.add(listener)
    }

    fun removeOnColorPickedListener(listener: (color: Int) -> Unit){
        onColorPickedListener.remove(listener)
    }
}