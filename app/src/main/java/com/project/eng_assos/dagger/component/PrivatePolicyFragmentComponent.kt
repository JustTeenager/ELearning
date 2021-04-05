package com.project.eng_assos.dagger.component

import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.view.PrivatePolicyFragment
import dagger.Component

@Component(modules = [BindingModule::class])
interface PrivatePolicyFragmentComponent {

    fun inject(fragment: PrivatePolicyFragment)
}