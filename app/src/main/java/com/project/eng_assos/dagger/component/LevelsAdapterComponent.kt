package com.project.eng_assos.dagger.component

import com.project.eng_assos.dagger.module.BindingHoldersModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.utils.adapters.BaseAdapter
import com.project.eng_assos.utils.adapters.LevelsAdapter
import dagger.Component

@Component(modules = [ContextModule::class,BindingHoldersModule::class])
interface LevelsAdapterComponent {

    fun inject(adapter: BaseAdapter)
}