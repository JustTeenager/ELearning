package com.project.eng_assos.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.eng_assos.dagger.component.DaggerLevelsFragmentComponent
import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.databinding.FragmentLevelsBinding

import com.project.eng_assos.model.Level
import com.project.eng_assos.model.WordInLevel
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.utils.adapters.BaseAdapter
import com.project.eng_assos.utils.adapters.LevelsAdapter
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LevelsFragment :BaseRecyclerFragment(){

    companion object{
        fun newInstance():LevelsFragment {
            val args = Bundle()
            val fragment = LevelsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupAdapter():BaseAdapter {
        val recViewAdapter = LevelsAdapter()
        context?.let {
            DatabaseSingleton.getInstance(it)?.getLevelDao()?.getAllLevels()?.subscribeOn(Schedulers.io())
                ?.flatMapIterable { it -> it }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { level ->
                    recViewAdapter.addDataToList(level)
                }
        }
        return recViewAdapter
    }


}