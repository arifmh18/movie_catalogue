package com.ardat.moviecatalogue.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.database.DatabaseContract
import com.ardat.moviecatalogue.database.MovieHelper
import com.ardat.moviecatalogue.model.ResultMovieModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieDetailActivity : AppCompatActivity(), View.OnClickListener {
    private var detail_back : Button? = null
    private var detail_gambar : ImageView? = null
    private var detail_judul : TextView? = null
    private var detail_tahun : TextView? = null
    private var detail_score : TextView? = null
    private var detail_deskripsi : TextView? = null
    private var detail_favorite : ImageButton? = null

    private var movie: ResultMovieModel? = null
    private var position: Int = 0
    private lateinit var movieHelper: MovieHelper
    private var title: String? = ""
    private var deskripsi: String? = ""
    private var score: String? = ""
    private var tahun: String? = ""
    private var poster: String? = ""
    private var id: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        init()
        ambilData()
    }

    private fun ambilData() {
        val data = intent.getParcelableExtra<ResultMovieModel>("data")

        title = data.title
        deskripsi = data.overview
        score = data.vote_average.toString()
        tahun = data.release_date
        poster = data.poster_path
        id = data.id

        detail_judul?.text = title
        detail_deskripsi?.text = deskripsi
        detail_score?.text = score
        detail_tahun?.text = tahun
        val img = "https://image.tmdb.org/t/p/w185"+poster
        Glide.with(this).load(img).apply(RequestOptions.skipMemoryCacheOf(true)).into(detail_gambar)
    }

    private fun init(){
        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        detail_back = findViewById(R.id.detail_back) as Button
        detail_gambar = findViewById(R.id.detail_gambar) as ImageView
        detail_judul = findViewById(R.id.detail_judul) as TextView
        detail_tahun = findViewById(R.id.detail_tahun) as TextView
        detail_score = findViewById(R.id.detail_score) as TextView
        detail_deskripsi = findViewById(R.id.detail_deskripsi) as TextView
        detail_favorite = findViewById(R.id.detail_favorite) as ImageButton

        detail_back!!.setOnClickListener(this)
        detail_favorite!!.setOnClickListener(this)
    }

    private fun saveData(){
        val values = ContentValues()
        values.put(DatabaseContract.MovieColoum.idMovie, id)
        values.put(DatabaseContract.MovieColoum.judulMovie, title)
        values.put(DatabaseContract.MovieColoum.scoreMovie, score)
        values.put(DatabaseContract.MovieColoum.deskripsiMovie, deskripsi)
        values.put(DatabaseContract.MovieColoum.tahunMovie, tahun)
        values.put(DatabaseContract.MovieColoum.gambarMovie, poster)

        val result = movieHelper.insert(values)
        if (result > 0){
            Toast.makeText(this, "Added Favorite", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.detail_back -> {
                onBackPressed()
            }
            R.id.detail_favorite -> {
                saveData()
            }
        }
    }
}
