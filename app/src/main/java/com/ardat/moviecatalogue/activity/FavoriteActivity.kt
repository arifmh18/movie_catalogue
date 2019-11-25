package com.ardat.moviecatalogue.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.adapter.FavAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_favorite.fav_back

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        init()

    }

    private fun init(){
        fav_viewPager?.adapter = FavAdapter(this, supportFragmentManager)
        fav_viewPager?.offscreenPageLimit = 2
        fav_tab?.setupWithViewPager(fav_viewPager)

        fav_back?.setOnClickListener{
            onBackPressed()
        }

        fav_refresh?.setOnClickListener {
            init()
        }
    }
}
