package com.project.eng_assos.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.project.eng_assos.dagger.component.DaggerPrivatePolicyFragmentComponent
import com.project.eng_assos.dagger.module.BindingModule
import com.project.eng_assos.databinding.FragmentPrivatePolicyBinding
import javax.inject.Inject

class PrivatePolicyFragment:Fragment() {

    companion object{
        private const val PRIVATE_POLICY_URL = "https://alexirinbooks.wixsite.com/englishapp"
        fun newInstance():PrivatePolicyFragment {
            return PrivatePolicyFragment()
        }
    }

    @Inject
    lateinit var binding:FragmentPrivatePolicyBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        DaggerPrivatePolicyFragmentComponent.builder().bindingModule(BindingModule(inflater, container))
                .build().inject(this)
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(PRIVATE_POLICY_URL)
            settings.javaScriptEnabled = true
        }
        return binding.root
    }

}