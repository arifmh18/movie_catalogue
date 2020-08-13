package com.ardat.moviecatalogue.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.adapter.TabAdapter
import com.ardat.moviecatalogue.setting.SettingPref
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var main_viewPager : ViewPager? = null
    private var main_setting : ImageButton? = null
    private var main_tab : TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init(){
        main_viewPager = findViewById(R.id.main_viewPager)
        main_setting = findViewById(R.id.main_setting)
        main_tab = findViewById(R.id.main_tab)

        main_viewPager?.adapter = TabAdapter(this, supportFragmentManager)
        main_viewPager?.offscreenPageLimit = 2
        main_tab?.setupWithViewPager(main_viewPager)

        main_setting?.setOnClickListener {
            val mIntent = Intent(this, SettingPref::class.java)
            startActivity(mIntent)
        }

        main_fav?.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

}
