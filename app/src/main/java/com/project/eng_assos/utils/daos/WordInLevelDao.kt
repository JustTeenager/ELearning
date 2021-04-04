package com.project.eng_assos.utils.daos

import androidx.room.*
import com.project.eng_assos.model.WordInLevel
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface WordInLevelDao {

    @Query("SELECT * FROM Words WHERE level = :numberLevel ")
    fun getAllWordsByLevels(numberLevel: Int): Maybe<MutableList<WordInLevel>>

    @Query("SELECT * FROM Words")
    fun getAllWords():Maybe<MutableList<WordInLevel>>

    @Query("SELECT * FROM Words WHERE level IN (:numsList)")
    fun getWordsByRange(numsList: List<Int>): Maybe<MutableList<WordInLevel>>

    @Insert
    fun insertWordInLevel(wordInLevel: WordInLevel)

    @Query("DELETE FROM Words")
    fun deleteWordInLevel()

    @Update
    fun updateLevel(wordInLevel: WordInLevel)
}