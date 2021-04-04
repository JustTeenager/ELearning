package com.project.eng_assos.dagger.module

import android.content.Context
import com.project.eng_assos.utils.Callback
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class ContextModule(
    private val context: Context) {

   // @Binds
   // abstract fun getActivityContext(context: Context):Context
    @Provides
    fun getActivityContext():Context{
        return context
    }

    @Provides
    fun getCallback(): Callback{
        return context as Callback
    }
}