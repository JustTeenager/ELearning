package com.project.eng_assos.view

import android.os.Bundle
import android.util.Pair
import com.project.eng_assos.model.Question
import com.project.eng_assos.model.WordInLevel
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.view.bases.QuestionFragment
import com.project.eng_assos.viewmodel.QuestionViewModel
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

class LevelQuestionFragment: QuestionFragment() {
    companion object {
        private const val KEY_TO_LEVEL="key_to_level"
        fun newInstance(level:Int): LevelQuestionFragment {
            val args = Bundle()
            args.putInt(KEY_TO_LEVEL,level)
            val fragment = LevelQuestionFragment()
            fragment.arguments = args
            return fragment
        }
    }
    val level by lazy {  arguments?.getInt(KEY_TO_LEVEL)?:0}
    override val count = 10
    override fun replaceFragment(countCorrectAnswer: Int, count: Int) {
        callBack.replaceFragmentWithoutBackStack(LevelTestResultFragment.newInstance(countCorrectAnswer,count,level))
    }

    override fun setupQuestionList() {
        context?.let { context ->
            val flowable= DatabaseSingleton.getInstance(context)?.getWordsDao()
                    ?.getAllWordsByLevels(level)
                    ?.map { list -> list.shuffled().toMutableList() }
            val flowableSecond = DatabaseSingleton.getInstance(context)?.getWordsDao()?.getAllWords()
                ?.map { list -> list.shuffled().toMutableList() }

            Maybe.zip(flowable,flowableSecond,{ list1:MutableList<WordInLevel>, list2:MutableList<WordInLevel> -> Pair(list1,list2) }).subscribeOn(
                Schedulers.io())
                .subscribe {pair->
                    pair?.first?.take(count)?.forEach { word ->
                        pair.second.remove(word)
                        val wrongList = pair.second.take(3).map { it -> it.word }
                        val question = Question(word.word,word.translate,wrongList,word.level,countQuestion)
                        questionList.add(question)
                        countQuestion++
                        pair.second.add(word)
                        pair.second.shuffle()
                    }
                    countQuestion =0
                    binding.viewModel = QuestionViewModel(questionList[countQuestion], count)
                    activity?.runOnUiThread { dialog.dismiss()}
                }
        }
    }
}