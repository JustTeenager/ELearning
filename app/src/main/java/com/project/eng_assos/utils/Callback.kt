package com.project.eng_assos.utils

import androidx.fragment.app.Fragment

interface Callback {
    fun replaceFragment(fragment: Fragment)

    fun replaceFragmentWithoutBackStack(fragment: Fragment)

    fun showAd()
}