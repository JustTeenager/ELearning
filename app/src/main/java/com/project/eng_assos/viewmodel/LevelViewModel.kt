package com.project.eng_assos.viewmodel

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import com.project.eng_assos.R

class LevelViewModel( @Bindable
                      val numberLevel:Int,
                      @Bindable
                      val markForTest: Int
                      ):BaseObservable()
{
     fun getLevel(context: Context):String{
         return "${context.getString(R.string.level)} $numberLevel"
     }

    fun getMark():String{
        return "$markForTest/10"
    }
}