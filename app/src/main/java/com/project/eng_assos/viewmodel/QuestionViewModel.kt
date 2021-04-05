package com.project.eng_assos.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.project.eng_assos.model.Question

class QuestionViewModel(private val question: Question, private val countQuestion:Int):BaseObservable() {
    private val words:List<String> = (question.wrongWordsList+question.word).shuffled()


    fun getImage(context: Context): Drawable {
        val inputStream = context.assets.open("${question.level}/${question.word}.jpg");
        return Drawable.createFromStream(inputStream, null);
    }

    @Bindable
    fun getNumberQuestion(): String{
        return "${question.numberQuestion+1}/$countQuestion ${question.translate.toUpperCase()}"
    }

    @Bindable
    fun getFirstText() = words[0]

    @Bindable
    fun getSecondText() = words[1]

    @Bindable
    fun getThirdText() = words[2]

    @Bindable
    fun getFourthText() = words[3]
}