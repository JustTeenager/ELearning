package com.project.eng_assos.viewmodel

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.eng_assos.R
import com.project.eng_assos.model.BlocksLevel

class BlockLevelLiveData:ViewModel() {
    val data: MutableLiveData<BlocksLevel> = MutableLiveData<BlocksLevel>()

    fun setLiveData(level: BlocksLevel) {
        data.value = level
    }

    fun getLiveData(): LiveData<BlocksLevel> {
        return data
    }

}