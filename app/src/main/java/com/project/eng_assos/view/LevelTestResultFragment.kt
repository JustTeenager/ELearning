package com.project.eng_assos.view

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.project.eng_assos.model.Level
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.view.bases.TestResultFragment
import com.project.eng_assos.viewmodel.LiveDataWithLevel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class LevelTestResultFragment: TestResultFragment() {
    companion object{
        private const val COUNT_CORRECT_ANSWERS_KEY = "count_correct_answers_key"
        private const val COUNT_AVERAGE_ANSWERS_KEY = "count_average_answers_key"
        private const val LEVEL_KEY = "level_key"
        fun newInstance(countCorrect:Int,count:Int, level:Int):LevelTestResultFragment {
            val args = Bundle()
            args.putInt(COUNT_CORRECT_ANSWERS_KEY,countCorrect)
            args.putInt(COUNT_AVERAGE_ANSWERS_KEY,count)
            args.putInt(LEVEL_KEY,level)
            val fragment = LevelTestResultFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val countCorrect by lazy { arguments?.getInt(COUNT_CORRECT_ANSWERS_KEY)?:0 }
    override val countAverage by lazy { arguments?.getInt(COUNT_AVERAGE_ANSWERS_KEY)?:0 }
    private val level by lazy{arguments?.getInt(LEVEL_KEY)?:1  }
    override fun writeDatabase() {
        activity?.let {activity->
            val lifeData = ViewModelProviders.of(activity).get(LiveDataWithLevel::class.java)
            val observer =object: Observer<Level> {
                override fun onChanged(t: Level?) {
                    if(t!=null){
                        t.isTestFinished = true
                        t.marksForTest = countCorrect
                        context?.let {context->
                            Observable.just("1").subscribeOn(Schedulers.io()).subscribe {
                                DatabaseSingleton.getInstance(context)?.getLevelDao()
                                        ?.updateLevel(t)
                            }
                        }
                        lifeData.getLiveData().removeObserver(this)
                    }
                }
            }
            lifeData.getLiveData().observe(this, observer)
        }
    }

    override fun replaceFragment() {
        callback.replaceFragmentWithoutBackStack(LevelQuestionFragment.newInstance(level))
    }

}