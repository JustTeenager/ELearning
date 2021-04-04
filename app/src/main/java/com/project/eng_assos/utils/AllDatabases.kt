package com.project.eng_assos.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.eng_assos.model.Level
import com.project.eng_assos.model.WordInLevel
import com.project.eng_assos.utils.daos.LevelDao
import com.project.eng_assos.utils.daos.WordInLevelDao
import kotlin.reflect.KClass

@Database(entities = [Level::class, WordInLevel::class],version = 1)
abstract class AllDatabases:RoomDatabase() {

    abstract fun getLevelDao():LevelDao

    abstract fun getWordsDao():WordInLevelDao
}