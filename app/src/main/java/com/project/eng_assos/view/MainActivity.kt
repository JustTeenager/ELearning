package com.project.eng_assos.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.wallet.*
import com.project.eng_assos.R
import com.project.eng_assos.dagger.component.DaggerAboutAppDialogComponent
import com.project.eng_assos.databinding.ActivityDrawerBinding
import com.project.eng_assos.utils.Callback
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.utils.SharedPrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainActivity : AppCompatActivity(),Callback {
    private val binding:ActivityDrawerBinding
    by lazy { DataBindingUtil.setContentView(this,R.layout.activity_drawer) }

    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991
    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "tut"
            //TODO сюда поставить настоящую ссылку на приложение
    private val URL_APP = "https://yandex.ru/images/search?from=tabbar&text=%D0%BA%D0%B0%D1%80%D1%82" +
            "%D0%B8%D0%BD%D0%BA%D0%B8&pos=0&img_url=https%3A%2F%2Fwww.zastavki.com%2Fpictures%2Fori" +
            "ginals%2F2014%2FNature___Rivers_and_lakes_Turquoise_lake_in_the_mountains_083623_.jpg&" +
            "rpt=simage"

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
       // val walletOptions = Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST)

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        addFragment(dialog, fragment)
        setUpNavigationView()
        MobileAds.initialize(this) {}
        setupAds()
    }

    @SuppressLint("CheckResult")
    private fun addFragment(
            dialog: ProgressDialog,
            fragment: Fragment
    ) {
        if (SharedPrefsManager.read(this, SharedPrefsManager.CODE_TO_DB_DOWNLOADING) != SharedPrefsManager.BD_CREATED) {
            dialog.show()
            dialog.setCancelable(false)
            DatabaseSingleton.initTheDb(this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
                        .commit()
                SharedPrefsManager.write(this@MainActivity,SharedPrefsManager.CODE_TO_PAY,SharedPrefsManager.NOT_PAYED)
                dialog.dismiss()
            }
            SharedPrefsManager.write(this, SharedPrefsManager.CODE_TO_DB_DOWNLOADING, SharedPrefsManager.BD_CREATED)
        } else {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }

    private fun setUpNavigationView() {
        binding.navView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.share -> {
                    shareUrl()
                }
                R.id.estimate -> {
                    estimateApp()
                }
                R.id.about_app -> {
                    aboutDialog.show(supportFragmentManager, null)
                }
                R.id.privacy_policy -> {
                    replaceFragment(PrivatePolicyFragment.newInstance())
                    binding.drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK ->
                        data?.let { intent ->
                            SharedPrefsManager.write(this,SharedPrefsManager.CODE_TO_PAY,SharedPrefsManager.PAYED)
                        }

                    RESULT_CANCELED -> {
                        Toast.makeText(this,
                                getString(R.string.cancel_payment), Toast.LENGTH_SHORT)
                                .show()
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        //AutoResolveHelper.getStatusFromIntent(data)?.let {
                        //  handleError(it.statusCode)
                        //}
                    }
                }
            }
        }
    }
    private fun setupAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.ad_key),
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

    private fun shareUrl(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,URL_APP )
        startActivity(Intent.createChooser(intent,getString(R.string.share_title)))
    }

    private fun estimateApp(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_APP))
        startActivity(intent)
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
        if (SharedPrefsManager.read(this,SharedPrefsManager.CODE_TO_PAY) != SharedPrefsManager.PAYED) {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
            }
            setupAds()
        }
    }
}