package com.project.eng_assos.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.project.eng_assos.model.BlocksLevel
import com.project.eng_assos.utils.adapters.BaseAdapter
import com.project.eng_assos.utils.adapters.BlockOfLevelsAdapter

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

    private fun setupDataList(adapter: BlockOfLevelsAdapter) {
        adapter.addDataToList(BlocksLevel((1..50).toList()))
        adapter.addDataToList(BlocksLevel((-4..-3).toList()))
        adapter.addDataToList(BlocksLevel((1..10).toList()))
        adapter.addDataToList(BlocksLevel((11..20).toList()))
        adapter.addDataToList(BlocksLevel((21..30).toList()))
        adapter.addDataToList(BlocksLevel((31..40).toList()))
        adapter.addDataToList(BlocksLevel((41..50).toList()))
        Log.d("tut",adapter.dataList.toString())
    }
}