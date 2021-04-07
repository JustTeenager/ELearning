package com.project.eng_assos.dagger.component

import com.project.eng_assos.view.MainActivity
import dagger.Component

@Component
interface AboutAppDialogComponent {
    fun inject(activity: MainActivity)
}