package com.ardat.moviecatalogue.fragment


import android.content.res.TypedArray
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.adapter.GridAdapter
import com.ardat.moviecatalogue.adapter.MovieAdapter
import com.ardat.moviecatalogue.model.MovieModel

class TvFragment : Fragment(), View.OnClickListener {
    private var main_list : RecyclerView? = null
    private var tv_grid_view : ImageButton? = null
    private var tv_list_view : ImageButton? = null
    private var v : View? = null

    private lateinit var dataJudul : Array<String>
    private lateinit var dataDeskripsi : Array<String>
    private lateinit var dataTahun : Array<String>
    private lateinit var dataScore : Array<String>
    private lateinit var dataGambar : TypedArray
    private var movie = ArrayList<MovieModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_tv, container, false)

        init()

        return v
    }

    private fun init(){
        main_list = v?.findViewById(R.id.tv_list) as RecyclerView
        tv_grid_view = v?.findViewById(R.id.tv_grid_view) as ImageButton
        tv_list_view = v?.findViewById(R.id.tv_list_view) as ImageButton

        main_list?.setHasFixedSize(true)

        getData()

        movie.addAll(getData())
        showRecyclerList()

        tv_grid_view?.setOnClickListener(this)
        tv_list_view?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_grid_view -> {
                showRecyclerGrid()
            }
            R.id.tv_list_view -> {
                showRecyclerList()
            }
        }
    }

    private fun getData() : ArrayList<MovieModel> {
        dataJudul = resources.getStringArray(R.array.judul_tv)
        dataDeskripsi = resources.getStringArray(R.array.deskripsi_tv)
        dataTahun = resources.getStringArray(R.array.tahun_tv)
        dataScore = resources.getStringArray(R.array.score_tv)
        dataGambar = resources.obtainTypedArray(R.array.gambar_tv)

        val listMovie = ArrayList<MovieModel>()
        for (i in dataJudul.indices){
            val movies = MovieModel(
                dataGambar.getResourceId(i, -1),
                dataJudul[i],
                dataDeskripsi[i],
                dataTahun[i],
                dataScore[i]
            )
            listMovie.add(movies)
        }
        return listMovie
    }

    private fun showRecyclerGrid() {
        main_list?.layoutManager = GridLayoutManager(context, 3)
        val gridHeroAdapter = GridAdapter(context, movie)
        main_list?.adapter = gridHeroAdapter
    }

    private fun showRecyclerList() {
        main_list?.layoutManager = LinearLayoutManager(context)
        val listHeroAdapter = MovieAdapter(context, movie)
        main_list?.adapter = listHeroAdapter
    }

}
