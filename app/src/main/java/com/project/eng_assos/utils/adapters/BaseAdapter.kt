package com.project.eng_assos.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.project.eng_assos.dagger.component.DaggerLevelsAdapterComponent
import com.project.eng_assos.dagger.module.BindingHoldersModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.utils.BaseHolder
import com.project.eng_assos.utils.Callback
import javax.inject.Inject

abstract class BaseAdapter: RecyclerView.Adapter<BaseHolder>() {
    open val dataList = mutableListOf(Any())

    override fun getItemCount() = dataList.size
    @Inject
    protected lateinit var binding: ViewDataBinding
    @Inject
    protected lateinit var callback: Callback

    abstract fun getBaseHolder(viewType: Int):BaseHolder

    fun addDataToList(data: Any){
        dataList.add(data)
        notifyItemChanged(dataList.size-1)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        DaggerLevelsAdapterComponent.builder()
            .bindingHoldersModule(BindingHoldersModule(LayoutInflater.from(parent.context),parent,viewType))
            .contextModule(ContextModule(parent.context))
            .build().inject(this)
        return getBaseHolder(viewType)
    }
}