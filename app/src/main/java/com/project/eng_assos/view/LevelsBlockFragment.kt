package com.project.eng_assos.view

import android.os.Bundle
import com.project.eng_assos.model.BlocksLevel
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.utils.adapters.BaseAdapter
import com.project.eng_assos.utils.adapters.BlockOfLevelsAdapter
import com.project.eng_assos.view.bases.BaseRecyclerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LevelsBlockFragment : BaseRecyclerFragment() {

    companion object {
        fun newInstance(): LevelsBlockFragment {
            val args = Bundle()
            val fragment = LevelsBlockFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupAdapter():BaseAdapter {
        val adapter =BlockOfLevelsAdapter()
        setupDataList(adapter)
        return adapter
    }

    private lateinit var disposable: Disposable
    private fun setupDataList(adapter: BlockOfLevelsAdapter) {
        context?.let {

           DatabaseSingleton.getInstance(it)?.getLevelDao()?.getCompletedLevels()
                ?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe {list->
                    adapter.addDataToList(BlocksLevel((1..50).toList()))
                    adapter.addDataToList(BlocksLevel(list))
                    adapter.addDataToList(BlocksLevel((1..10).toList()))
                    adapter.addDataToList(BlocksLevel((11..20).toList()))
                    adapter.addDataToList(BlocksLevel((21..30).toList()))
                    adapter.addDataToList(BlocksLevel((31..40).toList()))
                    adapter.addDataToList(BlocksLevel((41..50).toList()))
                }?.let { it -> disposable = it }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}