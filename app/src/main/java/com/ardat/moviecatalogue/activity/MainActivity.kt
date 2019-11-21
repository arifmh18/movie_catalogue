package com.ardat.moviecatalogue.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageButton
import androidx.viewpager.widget.ViewPager
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout

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
        main_viewPager = findViewById<ViewPager>(R.id.main_viewPager)
        main_setting = findViewById<ImageButton>(R.id.main_setting)
        main_tab = findViewById<TabLayout>(R.id.main_tab)

        main_viewPager?.adapter = TabAdapter(this, supportFragmentManager)
        main_tab?.setupWithViewPager(main_viewPager)

        main_setting?.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("paap destroy")
    }

    override fun onStart() {
        super.onStart()
        println("paap start")
    }

    override fun onStop() {
        super.onStop()
        println("paap stop")
    }

    override fun onRestart() {
        super.onRestart()
        println("paap restart")
    }
}
