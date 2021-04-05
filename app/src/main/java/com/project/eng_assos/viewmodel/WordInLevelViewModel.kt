package com.project.eng_assos.viewmodel

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.view.View
import androidx.databinding.BaseObservable
import com.project.eng_assos.model.WordInLevel


class WordInLevelViewModel(private val wordInLevel: WordInLevel, private val numberQuestion: Int):BaseObservable() {

    fun getFirstString():String{
        return "$numberQuestion ${wordInLevel.word.toUpperCase()}"
    }

    fun getSecondString():String{
        return "-${wordInLevel.transcription}"
    }

    fun getThirdString():String{
        return "- ${wordInLevel.translate}"
    }

    fun getImage(context: Context):Drawable?{
        return try {
            val inputStream = context.assets.open("${wordInLevel.level}/${wordInLevel.word}.jpg")
            Drawable.createFromStream(inputStream, null)
        }catch (e: Exception){
            e.printStackTrace()
            null
        }

    }

    fun playSound(context: Context){
        var m = MediaPlayer()
        try {
            if (m.isPlaying) {
                m.stop()
                m.release()
                m = MediaPlayer()
            }
            val descriptor: AssetFileDescriptor = context.assets.openFd("${wordInLevel.level}/${wordInLevel.word}.mp3")
            m.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
            descriptor.close()
            m.prepare()
            m.setVolume(1f, 1f)
            m.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}