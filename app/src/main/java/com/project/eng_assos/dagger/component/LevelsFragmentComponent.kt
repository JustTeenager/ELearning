package com.project.eng_assos.dagger.component

import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.view.bases.BaseRecyclerFragment
import dagger.Component

@Component(modules = [BindingModule::class,ContextModule::class])
interface LevelsFragmentComponent {
    fun inject(baseFr: BaseRecyclerFragment)
}