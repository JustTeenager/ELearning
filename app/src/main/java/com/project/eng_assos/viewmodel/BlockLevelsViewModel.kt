package com.project.eng_assos.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.eng_assos.R
import com.project.eng_assos.model.BlocksLevel

class BlockLevelsViewModel(val range: List<Int>) : BaseObservable() {

    fun getRange(context: Context):String{
        return when (range){
            (1..50).toList() -> context.getString(R.string.all_levels)
            (1..10).toList() -> "${context.getString(R.string.levels)} ${range.first()}-${range.last()}"
            (11..20).toList() -> "${context.getString(R.string.levels)} ${range.first()}-${range.last()}"
            (21..30).toList() -> "${context.getString(R.string.levels)} ${range.first()}-${range.last()}"
            (31..40).toList() -> "${context.getString(R.string.levels)} ${range.first()}-${range.last()}"
            (41..50).toList() -> "${context.getString(R.string.levels)} ${range.first()}-${range.last()}"
            else -> context.getString(R.string.learned_only)
        }
    }
}