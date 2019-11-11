package com.ardat.moviecatalogue.fragment


import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.ardat.moviecatalogue.MovieDetailActivity

import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.adapter.MovieAdapter
import com.ardat.moviecatalogue.model.MovieModel

class TvFragment : Fragment() {

    private var main_list : ListView? = null
    private lateinit var adapter: MovieAdapter
    private var v : View? = null

    private lateinit var dataJudul : Array<String>
    private lateinit var dataDeskripsi : Array<String>
    private lateinit var dataTahun : Array<String>
    private lateinit var dataScore : Array<String>
    private lateinit var dataGambar : TypedArray
    private var movie = arrayListOf<MovieModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false)
    }

    private fun init(){
        main_list = v?.findViewById(R.id.movie_list) as ListView
        adapter = MovieAdapter(context!!)
        main_list!!.adapter = adapter

        getData()
        insertData()

        main_list!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, MovieDetailActivity::class.java)
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
        dataJudul = resources.getStringArray(R.array.judul_tv)
        dataDeskripsi = resources.getStringArray(R.array.deskripsi_tv)
        dataTahun = resources.getStringArray(R.array.tahun_tv)
        dataScore = resources.getStringArray(R.array.score_tv)
        dataGambar = resources.obtainTypedArray(R.array.gambar_tv)
    }

    override fun onStart() {
        super.onStart()
        init()
    }

}
