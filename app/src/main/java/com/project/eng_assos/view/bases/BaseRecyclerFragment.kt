package com.project.eng_assos.view.bases

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd.load
import com.project.eng_assos.dagger.component.DaggerLevelsFragmentComponent
import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.dagger.module.ContextModule
import com.project.eng_assos.databinding.FragmentLevelsBinding
import com.project.eng_assos.utils.Callback
import com.project.eng_assos.utils.adapters.BaseAdapter
import javax.inject.Inject

abstract class BaseRecyclerFragment : Fragment() {

    @Inject
    lateinit var binding: FragmentLevelsBinding

    @Inject
    lateinit var callback: Callback

    private fun incrementDagger(inflater: LayoutInflater, container: ViewGroup?) {
        DaggerLevelsFragmentComponent.builder().contextModule(activity?.let { ContextModule(it) })
            .bindingModule(
                BindingModule(inflater, container)
            ).build().inject(this)
    }

    abstract fun setupAdapter(): BaseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        incrementDagger(inflater, container)
        binding.recyclerview.apply {
            adapter = setupAdapter()
            layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }
}