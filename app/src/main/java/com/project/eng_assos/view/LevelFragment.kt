package com.project.eng_assos.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.eng_assos.model.WordInLevel
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.utils.adapters.BaseAdapter
import com.project.eng_assos.utils.adapters.LevelAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LevelFragment:BaseRecyclerFragment() {

    companion object{
        private const val LEVEL_KEY = "level_key"
        fun newInstance(level:Int): LevelFragment{
            val args = Bundle()
            args.putInt(LEVEL_KEY,level)
            val fragment = LevelFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupAdapter():BaseAdapter {
        val levelAdapter = LevelAdapter()
        val level = arguments?.getInt(LEVEL_KEY)?:0
        levelAdapter.addDataToList(level)
        //TODO записать в лист все слова из уровня
        context?.let{
            DatabaseSingleton.getInstance(it)?.getWordsDao()?.getAllWords(level)?.subscribeOn(
                Schedulers.io())
                ?.flatMapIterable { it -> it }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{ wordInLevel->
                    levelAdapter.addDataToList(wordInLevel)
                }
        }
        return levelAdapter
    }
}