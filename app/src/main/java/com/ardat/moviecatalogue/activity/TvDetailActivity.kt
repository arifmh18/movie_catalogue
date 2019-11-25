package com.ardat.moviecatalogue.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.database.DatabaseContract
import com.ardat.moviecatalogue.database.MappingHelper
import com.ardat.moviecatalogue.database.TvHelper
import com.ardat.moviecatalogue.model.ResultMovieModel
import com.ardat.moviecatalogue.model.ResultTvModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TvDetailActivity : AppCompatActivity(), View.OnClickListener {
    private var detail_back : Button? = null
    private var detail_gambar : ImageView? = null
    private var detail_judul : TextView? = null
    private var detail_tahun : TextView? = null
    private var detail_score : TextView? = null
    private var detail_deskripsi : TextView? = null
    private var detail_favorite : ImageButton? = null
    private var detail_back_atas : ImageButton? = null

    private lateinit var tvHelper: TvHelper
    private var title: String? = ""
    private var deskripsi: String? = ""
    private var score: String? = ""
    private var tahun: String? = ""
    private var poster: String? = ""
    private var id: Int? = 0
    private var available = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        init()
        ambilData()
        loadTvAsync()
    }

    private fun ambilData() {
        val data = intent.getParcelableExtra<ResultTvModel>("data")

        title = data.name
        deskripsi = data.overview
        score = data.vote_average.toString()
        tahun = data.first_air_date
        poster = data.poster_path
        id = data.id

        detail_judul?.text = title
        detail_deskripsi?.text = deskripsi
        detail_score?.text = score
        detail_tahun?.text = tahun
        val img = "https://image.tmdb.org/t/p/w185"+poster
        Glide.with(this).load(img).apply(RequestOptions.skipMemoryCacheOf(true)).apply(
            RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(detail_gambar)
    }

    private fun init(){
        tvHelper = TvHelper.getInstance(applicationContext)
        tvHelper.open()

        detail_back = findViewById(R.id.detail_back) as Button
        detail_gambar = findViewById(R.id.detail_gambar) as ImageView
        detail_judul = findViewById(R.id.detail_judul) as TextView
        detail_tahun = findViewById(R.id.detail_tahun) as TextView
        detail_score = findViewById(R.id.detail_score) as TextView
        detail_deskripsi = findViewById(R.id.detail_deskripsi) as TextView
        detail_favorite = findViewById(R.id.detail_favorite) as ImageButton
        detail_back_atas = findViewById(R.id.detail_back_atas) as ImageButton

        detail_back!!.setOnClickListener(this)
        detail_favorite!!.setOnClickListener(this)
        detail_back_atas!!.setOnClickListener(this)
    }

    private fun loadTvAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredMovie = async(Dispatchers.IO) {
                val cursor = tvHelper.queryAll()
                MappingHelper.mapCursorTv(cursor)
            }
            val rmm = deferredMovie.await()

            if (rmm.contains(ResultTvModel(score?.toDouble(), poster, id, title, deskripsi, tahun))){
                detail_favorite?.setImageResource(R.drawable.ic_favorite_full)
                available = true
            } else {
                detail_favorite?.setImageResource(R.drawable.ic_favorite)
                available = false
            }
        }
    }

    private fun saveData(){
        val values = ContentValues()
        values.put(DatabaseContract.MovieColoum.idMovie, id)
        values.put(DatabaseContract.MovieColoum.judulMovie, title)
        values.put(DatabaseContract.MovieColoum.scoreMovie, score)
        values.put(DatabaseContract.MovieColoum.deskripsiMovie, deskripsi)
        values.put(DatabaseContract.MovieColoum.tahunMovie, tahun)
        values.put(DatabaseContract.MovieColoum.gambarMovie, poster)

        val result = tvHelper.insert(values)
        val add = getString(R.string.addFavorite)
        val fail = getString(R.string.failed)
        if (result > 0){
            Toast.makeText(this, add, Toast.LENGTH_SHORT).show()
            detail_favorite?.setImageResource(R.drawable.ic_favorite_full)
            available = true
        } else {
            Toast.makeText(this, fail, Toast.LENGTH_SHORT).show()
            available = false
        }
    }

    private fun deleteData(){
        val result = tvHelper.deleteById(id.toString()).toLong()
        val del = getString(R.string.deletedFavorite)
        if (result > 0){
            detail_favorite?.setImageResource(R.drawable.ic_favorite)
            Toast.makeText(this, del, Toast.LENGTH_SHORT).show()
            available = false
        } else  {
            detail_favorite?.setImageResource(R.drawable.ic_favorite_full)
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.detail_back -> {
                onBackPressed()
            }
            R.id.detail_back_atas -> {
                onBackPressed()
            }
            R.id.detail_favorite -> {
                if (available)
                    deleteData()
                else
                    saveData()
            }
        }
    }
}
