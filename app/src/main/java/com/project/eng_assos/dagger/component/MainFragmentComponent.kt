package com.project.eng_assos.dagger.component

import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.view.MainFragment
import dagger.Component

@Component(modules = [ContextModule::class,BindingModule::class])
interface MainFragmentComponent {

    fun inject(fragment:MainFragment)
}