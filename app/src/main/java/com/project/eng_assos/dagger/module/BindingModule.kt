package com.project.eng_assos.dagger.module

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.project.eng_assos.R
import com.project.eng_assos.databinding.*
import dagger.Module
import dagger.Provides

@Module
class BindingModule(private val inflater: LayoutInflater,private val container:ViewGroup?) {

    @Provides
    fun getMainFragmentBinding():FragmentMainBinding
            = DataBindingUtil.inflate(inflater, R.layout.fragment_main,container,false)

    @Provides
    fun getLevelsFragmentBinding():FragmentLevelsBinding
            = DataBindingUtil.inflate(inflater, R.layout.fragment_levels,container,false)

    @Provides
    fun getQuestionFragmentBinding():FragmentQuestionBinding =
        DataBindingUtil.inflate(inflater,R.layout.fragment_question,container,false)

    @Provides
    fun getTestResultFragmentBinding():FragmentTestResultBinding =
        DataBindingUtil.inflate(inflater,R.layout.fragment_test_result,container,false)

    @Provides
    fun getPrivatePolicyFragmentBinding():FragmentPrivatePolicyBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_private_policy,container,false)

}