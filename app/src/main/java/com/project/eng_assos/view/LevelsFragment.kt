package com.project.eng_assos.view

import android.os.Bundle

import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.utils.adapters.BaseAdapter
import com.project.eng_assos.utils.adapters.LevelsAdapter
import com.project.eng_assos.view.bases.BaseRecyclerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LevelsFragment : BaseRecyclerFragment(){

    companion object{
        fun newInstance():LevelsFragment {
            val args = Bundle()
            val fragment = LevelsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupAdapter():BaseAdapter {
        val recViewAdapter = LevelsAdapter()
        context?.let {
            DatabaseSingleton.getInstance(it)?.getLevelDao()?.getAllLevels()?.subscribeOn(Schedulers.io())
                ?.flatMapIterable { it -> it }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { level ->
                    recViewAdapter.addDataToList(level)
                }
        }
        return recViewAdapter
    }


}