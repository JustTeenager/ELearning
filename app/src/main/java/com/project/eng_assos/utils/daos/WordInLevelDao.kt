package com.project.eng_assos.utils.daos

import androidx.room.*
import com.project.eng_assos.model.WordInLevel
import io.reactivex.Flowable

@Dao
interface WordInLevelDao {

    @Query("SELECT * FROM Words WHERE level = :numberLevel ")
    fun getAllWords(numberLevel: Int): Flowable<List<WordInLevel>>

    @Query("SELECT * FROM Words WHERE level IN (:numsList)")
    fun getWordsByRange(numsList: List<Int>): Flowable<List<WordInLevel>>

    @Insert
    fun insertWordInLevel(wordInLevel: WordInLevel)

    @Query("DELETE FROM Words")
    fun deleteWordInLevel()

    @Update
    fun updateLevel(wordInLevel: WordInLevel)
}