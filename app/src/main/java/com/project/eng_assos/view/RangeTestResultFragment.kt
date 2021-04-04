package com.project.eng_assos.view

import android.os.Bundle
import com.project.eng_assos.view.bases.TestResultFragment

class RangeTestResultFragment: TestResultFragment() {
    companion object{
        private const val COUNT_CORRECT_ANSWERS_KEY = "count_correct_answers_key"
        private const val COUNT_AVERAGE_ANSWERS_KEY = "count_average_answers_key"
        fun newInstance(countCorrect:Int,count:Int):RangeTestResultFragment {
            val args = Bundle()
            args.putInt(COUNT_CORRECT_ANSWERS_KEY,countCorrect)
            args.putInt(COUNT_AVERAGE_ANSWERS_KEY,count)
            val fragment = RangeTestResultFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override val countCorrect by lazy { arguments?.getInt(COUNT_CORRECT_ANSWERS_KEY)?:0 }
    override val countAverage by lazy { arguments?.getInt(COUNT_AVERAGE_ANSWERS_KEY)?:0 }

    override fun writeDatabase() {

    }

    override fun replaceFragment() {
        callback.replaceFragmentWithoutBackStack(RangeQuestionFragment.newInstance())
    }
}