package com.project.eng_assos.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.eng_assos.model.Level
import com.project.eng_assos.model.WordInLevel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.CompletableSubject
import io.reactivex.subscribers.DisposableSubscriber

object DatabaseSingleton{

    private var database:AllDatabases? = null
    private val completable=CompletableSubject.create()
    fun getInstance(context: Context):AllDatabases?{
        if (database == null) {
            database =
                Room.databaseBuilder(context, AllDatabases::class.java, "database").addCallback(
                    object :
                        RoomDatabase.Callback() {
                        @SuppressLint("CheckResult")
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val words = readFile(context)
                            val observable =
                                Flowable.fromIterable(words)
                                    .map {
                                        val (word, translate, level, transcription) = it.split(";")
                                        return@map WordInLevel(
                                            word,
                                            translate,
                                            transcription,
                                            level.toInt()
                                        )
                                    }
                            //observable.subscribe { wordInLevel ->
                                //database?.getWordsDao()?.insertWordInLevel(wordInLevel)
                            //}
                            Log.d("tut_bd","вошли в онкрит")
                            val list = createListWithLevels()
                            val secondObservable = Flowable.fromIterable(list)
                                    //.subscribe { level ->
                                      //  database?.getLevelDao()?.insertLevel(level)
                                    //}
                            Flowable.zip(observable,secondObservable,
                                { w: WordInLevel, l: Level -> Pair(w,l) }).subscribeOn(Schedulers.io())
                                .subscribe(object:DisposableSubscriber<Pair<WordInLevel,Level>>(){
                                    override fun onNext(t: Pair<WordInLevel, Level>) {
                                        Log.d("tut","onNext")
                                        database?.getWordsDao()?.insertWordInLevel(t.first)
                                        if (t.second.numberLevel<=50) database?.getLevelDao()?.insertLevel(t.second)}

                                    override fun onError(t: Throwable?) {
                                    }

                                    override fun onComplete() {
                                        completable.onComplete()
                                    }
                                }
                                )
                        }
                    }).build()
            }
        return database
    }



    private fun readFile(context: Context):MutableList<String>{
        val words = mutableListOf<String>()
        val inputStream = context.assets?.open("words.txt")
        inputStream?.bufferedReader()?.forEachLine {
            words.add(it)
        }
        return words
    }


    fun initTheDb(context: Context):CompletableSubject{
        getInstance(context)?.getLevelDao()?.getAllLevels()?.subscribeOn(Schedulers.io())?.subscribe{Log.d("tut","dbInited")}
        return completable
    }


    private fun createListWithLevels():MutableList<Level?>{
        val list = mutableListOf<Level?>()
        for (i in 1..500){
            val level = Level(i, 0, isTestFinished = false, isCompleted = false)
            list.add(level)
        }
        return list
    }

}