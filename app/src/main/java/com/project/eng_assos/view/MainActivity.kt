package com.project.eng_assos.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.project.eng_assos.R
import com.project.eng_assos.databinding.ActivityDrawerBinding
import com.project.eng_assos.utils.Callback
import com.project.eng_assos.utils.DatabaseSingleton
import com.project.eng_assos.utils.SharedPrefsManager
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader


class MainActivity : AppCompatActivity(),Callback {
    private val binding:ActivityDrawerBinding by lazy { DataBindingUtil.setContentView(this,R.layout.activity_drawer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment == null){
            fragment = MainFragment.newInstance()
        }
        //DatabaseSingleton.getInstance(this)
          //  ?.getLevelDao()?.getAllLevels()?.subscribeOn(Schedulers.io())?.subscribe{Log.d("tut","dbInited")}
        if (SharedPrefsManager.read(this,SharedPrefsManager.CODE_TO_DB_DOWNLOADING)!=SharedPrefsManager.BD_CREATED) {
            Log.d("tut","вошли в иф по созданию")
            DatabaseSingleton.initTheDb(this).subscribe {
                Log.d("tut","onComplete")
                supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
                    .commit()
            }
            SharedPrefsManager.write(this,SharedPrefsManager.CODE_TO_DB_DOWNLOADING,SharedPrefsManager.BD_CREATED)
        }
        else { supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit() }
        //supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()

        binding.navView.setNavigationItemSelectedListener { item->
            //TODO задать логику для кнопок в navigation view
            when(item.itemId){
                R.id.share->{}
                R.id.estimate->{}
                R.id.about_app->{}
                R.id.privacy_policy->{}
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(null).commit()
    }
}