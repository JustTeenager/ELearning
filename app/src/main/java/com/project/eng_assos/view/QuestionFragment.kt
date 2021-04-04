package com.project.eng_assos.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.project.eng_assos.databinding.FragmentQuestionBinding
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.viewmodel.BlockLevelsViewModel
import javax.inject.Inject

class QuestionFragment:Fragment() {


    companion object {
        private const val KEY_TO_QUESTIONS_COUNT="key_to_questions_count"
        fun newInstance(count:Int): QuestionFragment {
            val args = Bundle()
            args.putInt(KEY_TO_QUESTIONS_COUNT,count)
            val fragment = QuestionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var binding:FragmentQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let { ViewModelProviders.of(it).get(BlockLevelsViewModel::class.java).getLiveData().observe(this,
            Observer {
                context?.let { context -> DatabaseSingleton.getInstance(context)?.getWordsDao()?.getWordsByRange(it.range) }
            })
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}