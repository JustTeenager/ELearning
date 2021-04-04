package com.project.eng_assos.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.eng_assos.R
import com.project.eng_assos.model.BlocksLevel

class BlockLevelsViewModel() : ViewModel() {

    val data:MutableLiveData<BlocksLevel> = MutableLiveData<BlocksLevel>()
    lateinit var range:List<Int>

    fun setLiveData(level:BlocksLevel) {
        data.value = level
    }

    fun getLiveData():LiveData<BlocksLevel>{
        return data
    }

    fun getRange(context: Context):String{
        Log.d("tut_tut_range",range.toString())
        return when (range){
            0 -> context.getString(R.string.all_levels)
            1 -> context.getString(R.string.learned_only)
            else -> "пососи"
            //else -> "${context.getString(R.string.levels)} ${range.first()}-${range.last()}"
        }
    }
}