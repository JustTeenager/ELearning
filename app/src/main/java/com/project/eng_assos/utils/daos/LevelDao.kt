package com.project.eng_assos.utils.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.project.eng_assos.model.Level
import io.reactivex.Flowable

@Dao
interface LevelDao {
    @Query("SELECT * FROM Levels")
    fun getAllLevels():Flowable<MutableList<Level>>

    @Insert
    fun insertLevel(level: Level)

    @Update
    fun updateLevel(level: Level)
}