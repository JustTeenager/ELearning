package com.project.eng_assos.view.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.eng_assos.dagger.component.DaggerMainFragmentComponent
import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.databinding.FragmentTestResultBinding
import com.project.eng_assos.utils.Callback
import com.project.eng_assos.view.MainFragment
import javax.inject.Inject

abstract class TestResultFragment:Fragment() {

    abstract val countCorrect:Int
    abstract val countAverage:Int
    @Inject
    lateinit var binding:FragmentTestResultBinding
    @Inject
    lateinit var callback: Callback
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        context?.let {
            DaggerMainFragmentComponent.builder().contextModule(ContextModule(it)).bindingModule(
                BindingModule(inflater,container)).build().injectTestResult(this)
        }
        binding.result = "$countCorrect из $countAverage"
        binding.tryAgain.setOnClickListener {
            callback.showAd()
            replaceFragment()
        }
        binding.end.setOnClickListener {
            callback.showAd()
            callback.replaceFragmentWithoutBackStack(MainFragment.newInstance())
        }
        writeDatabase()
        return binding.root
    }

    abstract fun writeDatabase()
    abstract fun replaceFragment()

}