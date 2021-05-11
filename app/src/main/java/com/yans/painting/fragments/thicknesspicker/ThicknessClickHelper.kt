package com.yans.painting.fragments.thicknesspicker

import android.view.View
import android.widget.ImageButton
import com.yans.painting.R
import com.yans.painting.databinding.FragmentBrushThicknessBinding

class ThicknessClickHelper(private val binding: FragmentBrushThicknessBinding) : View.OnClickListener {

    private val onThicknessPickedListeners: MutableList<(thickness: Int) -> Unit> = mutableListOf()

    init {
        binding.btnThickness1.setOnClickListener(this)
        binding.btnThickness2.setOnClickListener(this)
        binding.btnThickness3.setOnClickListener(this)
        binding.btnThickness4.setOnClickListener(this)
        binding.btnThickness5.setOnClickListener(this)
        binding.btnThickness6.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null && v is ImageButton){
            when(v.id){
                R.id.btn_thickness_1 -> {
                    onThicknessPickedListeners.forEach {
                        it(binding.root.resources.getDimensionPixelSize(R.dimen.dp_3))
                    }
                }
                R.id.btn_thickness_2 -> {
                    onThicknessPickedListeners.forEach {
                        it(binding.root.resources.getDimensionPixelSize(R.dimen.dp_6))
                    }
                }
                R.id.btn_thickness_3 -> {
                    onThicknessPickedListeners.forEach {
                        it(binding.root.resources.getDimensionPixelSize(R.dimen.dp_9))
                    }
                }
                R.id.btn_thickness_4 -> {
                    onThicknessPickedListeners.forEach {
                        it(binding.root.resources.getDimensionPixelSize(R.dimen.dp_12))
                    }
                }
                R.id.btn_thickness_5 -> {
                    onThicknessPickedListeners.forEach {
                        it(binding.root.resources.getDimensionPixelSize(R.dimen.dp_15))
                    }
                }
                R.id.btn_thickness_6 -> {
                    onThicknessPickedListeners.forEach {
                        it(binding.root.resources.getDimensionPixelSize(R.dimen.dp_18))
                    }
                }
            }
        }
    }

    fun addOnColorPickedListener(listener: (thickness: Int) -> Unit){
        onThicknessPickedListeners.add(listener)
    }

    fun removeOnColorPickedListener(listener: (thickness: Int) -> Unit){
        onThicknessPickedListeners.remove(listener)
    }

}