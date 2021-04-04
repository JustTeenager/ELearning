package com.project.eng_assos.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Words")
data class WordInLevel(
    @PrimaryKey
    @ColumnInfo(name = "word")
    var word:String,
    @ColumnInfo(name = "translate")
    var translate:String,
    @ColumnInfo(name = "transcription")
    var transcription:String,
    @ColumnInfo(name = "level")
    var level:Int
)