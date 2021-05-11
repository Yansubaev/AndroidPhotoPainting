package com.yans.painting.fragments.photoediting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yans.painting.R

class PhotoEditingViewModel(application: Application) : AndroidViewModel(application) {

    private val _colorData: MutableLiveData<Int> = MutableLiveData()
    val colorData: LiveData<Int>
        get() = _colorData

    private val _thicknessData: MutableLiveData<Int> = MutableLiveData()
    val thicknessData: LiveData<Int>
        get() = _thicknessData

    init {
        _colorData.value = application.getColor(R.color.default_brush_color)
        _thicknessData.value = application.resources.getDimensionPixelSize(R.dimen.dp_3)
    }

    fun setBrushColor(color: Int) {
        _colorData.value = color
    }

    fun setBrushThickness(thickness: Int) {
        _thicknessData.value = thickness
    }
}