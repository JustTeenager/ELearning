package com.project.eng_assos.utils.adapters


import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.project.eng_assos.R
import com.project.eng_assos.databinding.ItemButtonsBinding
import com.project.eng_assos.databinding.ItemTextLevelBinding
import com.project.eng_assos.databinding.ItemWordInLevelBinding
import com.project.eng_assos.model.WordInLevel
import com.project.eng_assos.utils.BaseHolder
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.utils.HolderBinding
import com.project.eng_assos.view.LevelQuestionFragment
import com.project.eng_assos.viewmodel.LiveDataWithLevel
import com.project.eng_assos.viewmodel.WordInLevelViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class LevelAdapter: BaseAdapter() {
    companion object{
        const val BUTTON_HOLDER_TYPE=2
        const val TEXT_LEVEL_HOLDER_TYPE=3
        const val LEVEL_HOLDER_TYPE=4
    }
    override fun getBaseHolder(viewType: Int):BaseHolder = when (viewType) {
        BUTTON_HOLDER_TYPE -> ButtonsHolder(binding as ItemButtonsBinding)
        TEXT_LEVEL_HOLDER_TYPE -> TextLevelHolder(binding as ItemTextLevelBinding)
        else -> WordsLevelHolder(binding as ItemWordInLevelBinding)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        when (position) {
            0 -> {
                (holder as ButtonsHolder).onBind(dataList[position])
            }
            1 -> {
                (holder as TextLevelHolder).onBind(dataList[position] as Int)
            }
            else -> {
                (holder as WordsLevelHolder).numberQuestion = position-1
                holder.onBind(dataList[position] as WordInLevel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> BUTTON_HOLDER_TYPE
            1 -> TEXT_LEVEL_HOLDER_TYPE
            else -> LEVEL_HOLDER_TYPE
        }
    }


    inner class ButtonsHolder(private val binding: ItemButtonsBinding): BaseHolder(binding),
        HolderBinding<Any> {
        override fun onBind(data: Any) {
            val viewModel = ViewModelProviders.of(binding.root.context as FragmentActivity).get(LiveDataWithLevel::class.java)
            viewModel.getLiveData().observe(
                binding.root.context as FragmentActivity,
                { t ->
                    if (t != null) {
                        if (t.isCompleted){
                            binding.flagLevelButton.text = binding.root.context.getString(R.string.flag_level_cancel)
                        }
                        binding.flagLevelButton.setOnClickListener {
                            callback.showAd()
                            t.isCompleted = !t.isCompleted
                            Observable.just("1").subscribeOn(Schedulers.io()).subscribe {
                                DatabaseSingleton.getInstance(binding.root.context)?.getLevelDao()?.updateLevel(t)
                            }
                            if (t.isCompleted){
                                binding.flagLevelButton.text = binding.root.context.getString(R.string.flag_level_cancel)
                            }
                            else{
                                binding.flagLevelButton.text = binding.root.context.getString(R.string.flag_level)
                            }
                        }

                        binding.takeTestButton.setOnClickListener {
                            callback.apply {
                                showAd()
                                replaceFragment(LevelQuestionFragment.newInstance(t.numberLevel))
                            }
                        }

                    }
                })
        }

    }

    inner class TextLevelHolder(private val binding: ItemTextLevelBinding): BaseHolder(binding),
        HolderBinding<Int> {
        override fun onBind(data: Int) {
            binding.level = "Уровень $data"
        }

    }

    inner class WordsLevelHolder(private val binding:ItemWordInLevelBinding): BaseHolder(binding), HolderBinding<WordInLevel> {
        var numberQuestion:Int = 1
        override fun onBind(data: WordInLevel) {
            val wordInLevelViewModel = WordInLevelViewModel(data, numberQuestion)
            binding.viewModel = wordInLevelViewModel
        }
    }
}