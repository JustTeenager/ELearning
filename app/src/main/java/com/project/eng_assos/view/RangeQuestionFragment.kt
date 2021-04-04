package com.project.eng_assos.view

import android.os.Bundle
import android.util.Pair
import androidx.lifecycle.ViewModelProviders
import com.project.eng_assos.model.Question
import com.project.eng_assos.model.WordInLevel
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.view.bases.QuestionFragment
import com.project.eng_assos.viewmodel.BlockLevelLiveData
import com.project.eng_assos.viewmodel.QuestionViewModel
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

class RangeQuestionFragment: QuestionFragment() {

    companion object {
        fun newInstance(): RangeQuestionFragment {
            val args = Bundle()
            val fragment = RangeQuestionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val count = 20
    override fun replaceFragment(countCorrectAnswer: Int, count: Int) {
        callBack.replaceFragmentWithoutBackStack(RangeTestResultFragment.newInstance(countCorrectAnswer,count))
    }


    override fun setupQuestionList() {
        activity?.let{activity->
            val livedata = ViewModelProviders.of(activity).get(BlockLevelLiveData::class.java)
            livedata.getLiveData().observe(this,{
                blocklevel->
                if (blocklevel != null) {
                    context?.let { context ->
                        val flowable = DatabaseSingleton.getInstance(context)?.getWordsDao()
                            ?.getWordsByRange(blocklevel.range)
                            ?.map { list -> list.shuffled().toMutableList() }
                        val flowableSecond = DatabaseSingleton.getInstance(context)?.getWordsDao()?.getAllWords()
                            ?.map { list -> list.shuffled().toMutableList() }
                        Maybe.zip(flowable, flowableSecond, { list1: MutableList<WordInLevel>, list2: MutableList<WordInLevel> -> Pair(list1, list2) }).subscribeOn(
                            Schedulers.io())
                            .subscribe { pair ->
                                pair?.first?.take(count)?.forEach { word ->
                                    pair.second.remove(word)
                                    val wrongList = pair.second.take(3).map { it -> it.word }
                                    val question = Question(word.word, word.translate, wrongList, word.level, countQuestion)
                                    questionList.add(question)
                                    countQuestion++
                                    pair.second.add(word)
                                    pair.second.shuffle()
                                }
                                countQuestion = 0
                                binding.viewModel = QuestionViewModel(questionList[countQuestion], count)
                                activity.runOnUiThread { dialog.dismiss()}
                        }
                    }
                }
        })
    }
    }
}