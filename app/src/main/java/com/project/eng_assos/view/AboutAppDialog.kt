package com.project.eng_assos.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.project.eng_assos.R
import javax.inject.Inject

class AboutAppDialog @Inject constructor() : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_about,null,false)
        val builder = AlertDialog.Builder(context)
        return builder.setTitle(R.string.about_app).setView(v).create()
    }
}