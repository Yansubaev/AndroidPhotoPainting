package com.yans.painting.fragments.colorpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yans.painting.databinding.FragmentColorListBinding

class ColorListFragment : BottomSheetDialogFragment() {

    private var colorPickedListener: ((color: Int) -> Unit)? = null

    private lateinit var binding: FragmentColorListBinding

    private lateinit var clickHelper: ColorsClickHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentColorListBinding.inflate(inflater, container, false)
        clickHelper = ColorsClickHelper(binding)
        clickHelper.addOnColorPickedListener {
            colorPickedListener?.invoke(it)
            dismiss()
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    fun setOnColorPickedListener(listener: (color: Int) -> Unit){
        colorPickedListener = listener
    }

    fun removeOnColorPickedListener(){
        colorPickedListener = null
    }
}