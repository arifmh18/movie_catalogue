package com.ardat.moviecatalogue

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import com.ardat.moviecatalogue.adapter.MovieAdapter
import com.ardat.moviecatalogue.model.MovieModel

class MainActivity : AppCompatActivity() {

    private var main_list : ListView? = null
    private lateinit var adapter: MovieAdapter

    private lateinit var dataJudul : Array<String>
    private lateinit var dataDeskripsi : Array<String>
    private lateinit var dataTahun : Array<String>
    private lateinit var dataScore : Array<String>
    private lateinit var dataGambar : TypedArray
    private var movie = arrayListOf<MovieModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_list = findViewById(R.id.main_list) as ListView
        adapter = MovieAdapter(this)
        main_list!!.adapter = adapter

        getData()
        insertData()

        main_list!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
            intent.putExtra("data",movie[position])
            startActivity(intent)
        }
    }

    private fun insertData() {
        for (i in dataJudul.indices){
            val movies = MovieModel(
                dataGambar.getResourceId(i, -1),
                dataJudul[i],
                dataDeskripsi[i],
                dataTahun[i],
                dataScore[i]
            )
            movie.add(movies)
        }
        adapter.movie = movie
    }

    private fun getData() {
        dataJudul = resources.getStringArray(R.array.judul_movie)
        dataDeskripsi = resources.getStringArray(R.array.deskripsi_movie)
        dataTahun = resources.getStringArray(R.array.tahun_movie)
        dataScore = resources.getStringArray(R.array.score_movie)
        dataGambar = resources.obtainTypedArray(R.array.gambar_movie)
    }

}
