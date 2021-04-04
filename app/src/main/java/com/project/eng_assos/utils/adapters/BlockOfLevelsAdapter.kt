package com.project.eng_assos.utils.adapters

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.project.eng_assos.databinding.ItemBlockLevelsBinding
import com.project.eng_assos.databinding.ItemBlockLevelsTitleBinding
import com.project.eng_assos.model.BlocksLevel
import com.project.eng_assos.utils.BaseHolder
import com.project.eng_assos.utils.HolderBinding
import com.project.eng_assos.view.LevelsBlockFragment
import com.project.eng_assos.view.QuestionFragment
import com.project.eng_assos.viewmodel.BlockLevelsViewModel

class BlockOfLevelsAdapter : BaseAdapter()  {

    companion object{
        const val TITLE_HOLDER_TYPE=5
        const val ITEM_HOLDER_TYPE=6
    }

    override fun getBaseHolder(viewType: Int): BaseHolder {
       return when(viewType) {
           TITLE_HOLDER_TYPE -> TitleItem(binding as ItemBlockLevelsTitleBinding)
           else -> BlockItem(binding as ItemBlockLevelsBinding)
       }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position){
            0 -> TITLE_HOLDER_TYPE
            else -> ITEM_HOLDER_TYPE
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (position!=0) {
            (holder as BlockItem).onBind(dataList[position] as BlocksLevel)
        }
    }

    inner class TitleItem(val binding: ItemBlockLevelsTitleBinding) : BaseHolder(binding) {
    }

    inner class BlockItem(val binding: ItemBlockLevelsBinding) : BaseHolder(binding),HolderBinding<BlocksLevel> {
        val QUESTIONS_COUNT=20
        override fun onBind(data: BlocksLevel) {
            val model = ViewModelProviders.of(binding.root.context as FragmentActivity).get(BlockLevelsViewModel::class.java)
            //model.setLiveData(data)
            model.range=data.range
            Log.d("tut_data",model.range.toString())
            model.posititon=layoutPosition-1
            binding.viewmodel=model
            binding.levelButton.setOnClickListener {
                callback.replaceFragment(QuestionFragment.newInstance(QUESTIONS_COUNT))
            }
        }
    }
}