package com.project.eng_assos.utils.adapters

import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.project.eng_assos.R
import com.project.eng_assos.databinding.ItemBlockLevelsBinding
import com.project.eng_assos.databinding.ItemBlockLevelsTitleBinding
import com.project.eng_assos.model.BlocksLevel
import com.project.eng_assos.utils.BaseHolder
import com.project.eng_assos.utils.HolderBinding
import com.project.eng_assos.utils.SharedPrefsManager
import com.project.eng_assos.view.RangeQuestionFragment
import com.project.eng_assos.viewmodel.BlockLevelLiveData
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
        override fun onBind(data: BlocksLevel) {
            val viewModel = ViewModelProviders.of(binding.root.context as FragmentActivity).get(BlockLevelLiveData::class.java)
            val model = BlockLevelsViewModel(data.range)
            binding.viewmodel=model
            binding.levelButton.setOnClickListener {
                if (data.range.size<2){
                    Toast.makeText(binding.root.context,binding.root.context.getString(R.string.learned_one_level),Toast.LENGTH_SHORT)
                        .show()
                }else {
                    viewModel.setLiveData(data)
                    callback.replaceFragment(RangeQuestionFragment.newInstance())
                }
            }
            if (SharedPrefsManager.read(binding.root.context,
                    SharedPrefsManager.CODE_TO_PAY) != SharedPrefsManager.PAYED &&
                    model.getRange(binding.root.context)
                    != binding.root.context.getString(R.string.learned_only)) {
                        binding.levelButton.apply {
                        isClickable = false
                        setBackgroundColor(ActivityCompat.getColor(binding.root.context,R.color.red))
                }
            }
        }
    }
}