package com.project.eng_assos.view.bases

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.project.eng_assos.R
import com.project.eng_assos.dagger.component.DaggerMainFragmentComponent
import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.databinding.FragmentQuestionBinding
import com.project.eng_assos.model.Question
import com.project.eng_assos.utils.Callback
import com.project.eng_assos.viewmodel.QuestionViewModel
import javax.inject.Inject

abstract class QuestionFragment:Fragment() {
    abstract val count:Int
    protected val questionList = mutableListOf<Question>()

    @Inject
    lateinit var binding:FragmentQuestionBinding

    @Inject
    lateinit var callBack:Callback
    protected var countQuestion = 0
    private var countCorrectAnswers = 0

    protected val dialog by lazy { ProgressDialog(context) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        context?.let {
            DaggerMainFragmentComponent.builder().bindingModule(BindingModule(inflater,container)).contextModule(
                ContextModule(it)).build().injectQuestion(this)
        }

        dialog.show()
        dialog.setCancelable(false)
        setupQuestionList()
        binding.apply {

            firstAnswer.setOnClickListener { clickAnswer(firstAnswer)
                changeClicks(false)
            }
            secondAnswer.setOnClickListener {   clickAnswer(secondAnswer)
                changeClicks(false)
            }
            thirdAnswer.setOnClickListener {   clickAnswer(thirdAnswer)
                changeClicks(false)
            }
            fourthAnswer.setOnClickListener {   clickAnswer(fourthAnswer)
                changeClicks(false)
            }

            nextQuestion.setOnClickListener {
                if(countQuestion<questionList.size-1)
                countQuestion++
                else{
                    replaceFragment(countCorrectAnswers,count)
                    //callBack.replaceFragmentWithoutBackStack(TestResultFragment.newInstance(countCorrectAnswers,count))
                }
                context?.let {
                    firstAnswer.setBackgroundColor(ContextCompat.getColor(it,R.color.grey_for_button))
                    secondAnswer.setBackgroundColor(ContextCompat.getColor(it,R.color.grey_for_button))
                    thirdAnswer.setBackgroundColor(ContextCompat.getColor(it,R.color.grey_for_button))
                    fourthAnswer.setBackgroundColor(ContextCompat.getColor(it,R.color.grey_for_button))
                    textResult.setTextColor(ContextCompat.getColor(it,R.color.white))
                }
                changeClicks(true)
                nextQuestion.visibility = INVISIBLE
                imageWord.visibility = INVISIBLE
                textResult.visibility = INVISIBLE

                viewModel = QuestionViewModel(questionList[countQuestion],count)
            }
        }
        return binding.root
    }

    private fun clickAnswer(button:Button){
        binding.nextQuestion.visibility = VISIBLE
        binding.imageWord.visibility = VISIBLE
        binding.textResult.visibility = VISIBLE
        if (button.text.toString() == questionList[countQuestion].word ){
            context?.let{
                button.setBackgroundColor(ContextCompat.getColor(it, R.color.green))
                binding.textResult.text = it.getString(R.string.true_answer)
            }
            countCorrectAnswers++
        }
        else{
            context?.let{
                button.setBackgroundColor(ContextCompat.getColor(it, R.color.red))
                binding.textResult.text = it.getString(R.string.wrong_answer)
                //binding.textResult.setTextColor(ContextCompat.getColor(it,R.color.red))
            }
        }
    }

    abstract fun replaceFragment(countCorrectAnswer:Int,count:Int)
    abstract fun setupQuestionList()
    private fun changeClicks(boolean: Boolean){
        binding.firstAnswer.isClickable = boolean
        binding.secondAnswer.isClickable = boolean
        binding.thirdAnswer.isClickable = boolean
        binding.fourthAnswer.isClickable = boolean
    }
}