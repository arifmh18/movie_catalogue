package com.ardat.moviecatalogue.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.model.ResultTvModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class TvDetailActivity : AppCompatActivity(), View.OnClickListener {
    private var detail_back : Button? = null
    private var detail_gambar : ImageView? = null
    private var detail_judul : TextView? = null
    private var detail_tahun : TextView? = null
    private var detail_score : TextView? = null
    private var detail_deskripsi : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        init()
        ambilData()
    }

    private fun ambilData() {
        val data = intent.getParcelableExtra<ResultTvModel>("data")

        detail_judul?.text = data.name
        detail_deskripsi?.text = data.overview
        detail_score?.text = data.vote_average.toString()
        detail_tahun?.text = data.first_air_date
        val img = "https://image.tmdb.org/t/p/w185"+data.poster_path
        Glide.with(this).load(img).apply(RequestOptions.skipMemoryCacheOf(true)).apply(
            RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(detail_gambar)
    }

    private fun init(){
        detail_back = findViewById(R.id.detail_back) as Button
        detail_gambar = findViewById(R.id.detail_gambar) as ImageView
        detail_judul = findViewById(R.id.detail_judul) as TextView
        detail_tahun = findViewById(R.id.detail_tahun) as TextView
        detail_score = findViewById(R.id.detail_score) as TextView
        detail_deskripsi = findViewById(R.id.detail_deskripsi) as TextView

        detail_back!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.detail_back -> {
                onBackPressed()
            }
        }
    }
}
