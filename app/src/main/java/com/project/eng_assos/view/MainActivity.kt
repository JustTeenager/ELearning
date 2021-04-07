package com.project.eng_assos.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.project.eng_assos.R
import com.project.eng_assos.dagger.component.DaggerAboutAppDialogComponent
import com.project.eng_assos.databinding.ActivityDrawerBinding
import com.project.eng_assos.utils.Callback
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.utils.SharedPrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import javax.inject.Inject


class MainActivity : AppCompatActivity(),Callback {
    private val binding:ActivityDrawerBinding by lazy { DataBindingUtil.setContentView(this,R.layout.activity_drawer) }

    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "tut"
    @Inject
    lateinit var aboutDialog: AboutAppDialog


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAboutAppDialogComponent.builder().build().inject(this)
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment == null){
            fragment = MainFragment.newInstance()
        }
        val dialog = ProgressDialog(this)
        if (SharedPrefsManager.read(this,SharedPrefsManager.CODE_TO_DB_DOWNLOADING)!=SharedPrefsManager.BD_CREATED) {
            dialog.show()
            dialog.setCancelable(false)
            DatabaseSingleton.initTheDb(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
                    .commit()
                dialog.dismiss()
            }
            SharedPrefsManager.write(this,SharedPrefsManager.CODE_TO_DB_DOWNLOADING,SharedPrefsManager.BD_CREATED)
        }
        else {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }

        binding.navView.setNavigationItemSelectedListener { item->

            when(item.itemId){
                R.id.share->{}
                R.id.estimate->{}
                R.id.about_app->{
                    aboutDialog.show(supportFragmentManager,null)
                }
                R.id.privacy_policy->{
                    replaceFragment(PrivatePolicyFragment.newInstance())
                    binding.drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        MobileAds.initialize(this) {}
        setupAds()
    }

    private fun setupAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.ad_key_test),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    Log.d(TAG,Thread.currentThread().name)
                    mInterstitialAd = interstitialAd
                }
            })
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(null).commit()
    }

    override fun replaceFragmentWithoutBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun showAd() {
        if (mInterstitialAd != null) {
            Log.d(TAG,"SHOW_THE_AD")
            mInterstitialAd?.show(this)
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.")
        }
        setupAds()
    }
}