package com.project.eng_assos.utils.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.project.eng_assos.R
import com.project.eng_assos.dagger.component.DaggerLevelsAdapterComponent
import com.project.eng_assos.dagger.module.BindingHoldersModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.databinding.ItemLevelBinding

import com.project.eng_assos.databinding.ItemTextInLevelsBinding
import com.project.eng_assos.model.Level
import com.project.eng_assos.utils.BaseHolder
import com.project.eng_assos.utils.Callback
import com.project.eng_assos.utils.HolderBinding
import com.project.eng_assos.view.LevelFragment
import com.project.eng_assos.viewmodel.LevelViewModel
import com.project.eng_assos.viewmodel.LiveDataWithLevel
import javax.inject.Inject

class LevelsAdapter: BaseAdapter() {


    companion object{
        const val TEXT_HOLDER_TYPE=0
        const val LEVELS_HOLDER_TYPE=1
    }

    override fun getBaseHolder(viewType: Int): BaseHolder {
        return if (viewType == TEXT_HOLDER_TYPE ){
            TextHolder(binding as ItemTextInLevelsBinding)
        } else{
            LevelHolder(binding as ItemLevelBinding)
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        if (position != 0){
            (holder as LevelHolder).onBind(dataList[position] as Level)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> TEXT_HOLDER_TYPE
            else-> LEVELS_HOLDER_TYPE
        }
    }

    inner class TextHolder(binding: ItemTextInLevelsBinding): BaseHolder(binding){

    }

    inner class LevelHolder(private val binding: ItemLevelBinding): BaseHolder(binding),
        HolderBinding<Level> {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onBind(data: Level) {
            val viewModel = LevelViewModel(data.numberLevel,data.marksForTest)
            if (data.isTestFinished){
                binding.markText.visibility = VISIBLE
            }
            else{
                binding.markText.visibility = INVISIBLE
            }
            if (data.isCompleted){
                binding.doneImage.visibility = VISIBLE
            }
            else{
                binding.doneImage.visibility = INVISIBLE
            }
            binding.levelButton.setOnClickListener {
                callback.replaceFragment(LevelFragment.newInstance(data.numberLevel))
                val livedata = ViewModelProviders.of(binding.root.context as FragmentActivity).get(LiveDataWithLevel::class.java)
                livedata.setLiveData(data)
            }
            binding.viewmodel = viewModel
        }

    }


}