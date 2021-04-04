package com.project.eng_assos.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Levels")
data class Level(
    @PrimaryKey()
    @ColumnInfo(name = "number")
    var numberLevel:Int,
    @ColumnInfo(name = "mark")
    var marksForTest:Int,
    @ColumnInfo(name = "isTest")
    var isTestFinished:Boolean,
    @ColumnInfo(name = "isCompleted")
    var isCompleted:Boolean
)
