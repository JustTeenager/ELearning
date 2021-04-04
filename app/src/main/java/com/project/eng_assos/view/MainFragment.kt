package com.project.eng_assos.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.eng_assos.dagger.component.DaggerMainFragmentComponent
import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.databinding.FragmentMainBinding
import com.project.eng_assos.model.WordInLevel
import com.project.eng_assos.utils.AllDatabases
import com.project.eng_assos.utils.Callback
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.viewmodel.BlockLevelsViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.*
import javax.inject.Inject

class MainFragment: Fragment() {

    @Inject
    lateinit var binding:FragmentMainBinding
    @Inject
    lateinit var callback: Callback

    companion object{
        fun newInstance(): MainFragment {
            val args = Bundle()
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DaggerMainFragmentComponent.builder().contextModule(activity?.let { ContextModule(it) }).bindingModule(
            BindingModule(inflater, container)
        ).build().inject(this)
        binding.learnButton.setOnClickListener { callback.replaceFragment(LevelsFragment.newInstance()) }
        binding.testButton.setOnClickListener { callback.replaceFragment(LevelsBlockFragment.newInstance()) }
        return binding.root
    }


}