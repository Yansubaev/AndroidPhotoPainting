package com.yans.painting.fragments.thicknesspicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yans.painting.databinding.FragmentBrushThicknessBinding

class BrushThicknessFragment : BottomSheetDialogFragment() {

    private var thicknessPickedListener: ((thickness: Int) -> Unit)? = null

    private lateinit var binding: FragmentBrushThicknessBinding

    private lateinit var clickHelper: ThicknessClickHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrushThicknessBinding.inflate(inflater, container, false)

        clickHelper = ThicknessClickHelper(binding)
        clickHelper.addOnColorPickedListener {
            thicknessPickedListener?.invoke(it)
            dismiss()
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    fun setOnThicknessPickedListener(listener: (thickness: Int) -> Unit){
        thicknessPickedListener = listener
    }

    fun removeOnThicknessPickedListener(){
        thicknessPickedListener = null
    }

}