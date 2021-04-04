package com.project.eng_assos.utils

import android.app.Activity
import android.content.Context

object SharedPrefsManager {
    const val PREFERENCES_NAME="preferences_name"
    const val CODE_TO_DB_DOWNLOADING="code_to_downloading"
    const val CODE_NO_VALUE="code_no_value"
    const val BD_CREATED="bd_created"
    fun write(context: Context,key:String,value:String){
        val prefs=context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE)
        prefs.edit().putString(key,value).apply()
    }

    fun read(context: Context,key:String)=context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE).getString(key,
        CODE_NO_VALUE)

}