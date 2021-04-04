package com.project.eng_assos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.eng_assos.model.Level

class LiveDataWithLevel:ViewModel() {
    private val levelLiveData:MutableLiveData<Level> = MutableLiveData()

    fun getLiveData():LiveData<Level> = levelLiveData

    fun setLiveData(level: Level){
        levelLiveData.value = level
    }
}