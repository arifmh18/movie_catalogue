package com.ardat.moviecatalogue.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.adapter.GridAdapter
import com.ardat.moviecatalogue.adapter.MovieAdapter
import com.ardat.moviecatalogue.baserespon.DataMovieBaseRespon
import com.ardat.moviecatalogue.model.ResultMovieModel
import com.ardat.moviecatalogue.object_interface.DataInterface
import com.ardat.moviecatalogue.object_interface.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieFragment : Fragment(), View.OnClickListener {

    private var main_list : RecyclerView? = null
    private var movie_grid_view : ImageButton? = null
    private var movie_list_view : ImageButton? = null
    private var progressBar : ProgressBar? = null
    private var v : View? = null

    private var dataInterface : DataInterface? = null
    private var show = 0

    private var movie : List<ResultMovieModel>? = null

    private var rmm : ArrayList<ResultMovieModel>? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("obj", rmm)
        outState.putInt("show", show)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_movie, container, false)

        init()

        if(savedInstanceState != null) {
            rmm  = savedInstanceState.get("obj") as ArrayList<ResultMovieModel>
            show  = savedInstanceState.get("show") as Int
            if (show == 0){
                showRecyclerList()
            } else {
                showRecyclerGrid()
            }
        } else {
            getData()
        }
        return v
    }

    private fun init(){
        main_list = v?.findViewById(R.id.movie_list) as RecyclerView
        movie_grid_view = v?.findViewById(R.id.movie_grid_view) as ImageButton
        movie_list_view = v?.findViewById(R.id.movie_list_view) as ImageButton
        progressBar = v?.findViewById(R.id.progressBar) as ProgressBar

        main_list?.setHasFixedSize(true)

        dataInterface = RetrofitBuilder.builder(context!!).create(DataInterface::class.java)

        movie_grid_view?.setOnClickListener(this)
        movie_list_view?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.movie_list_view -> {
                show = 0
                showRecyclerList()
            }
            R.id.movie_grid_view -> {
                show = 1
                showRecyclerGrid()
            }
        }
    }

    private fun showRecyclerGrid() {
        progressBar(false)
        main_list?.layoutManager = GridLayoutManager(context, 3)
        val gridHeroAdapter = GridAdapter(context, rmm!!)
        main_list?.adapter = gridHeroAdapter
    }

    private fun getData() {
        progressBar(true)
        val callData = dataInterface!!.dataMovie()
        callData.enqueue(object: Callback<DataMovieBaseRespon>{
            override fun onFailure(call: Call<DataMovieBaseRespon>, t: Throwable) {

            }

            override fun onResponse(call: Call<DataMovieBaseRespon>, response: Response<DataMovieBaseRespon>) {
                if (response.isSuccessful){
                    val respon = response.body() as DataMovieBaseRespon
                    movie = respon.results
                    progressBar?.visibility = View.GONE

                    rmm = ArrayList()

                    for(i in 0 until movie!!.size) {
                        rmm!!.add(ResultMovieModel(movie!!.get(i).vote_average, movie!!.get(i).poster_path, movie!!.get(i).id, movie!!.get(i).title,  movie!!.get(i).overview,  movie!!.get(i).release_date))
                    }

                    showRecyclerList()
                }
            }

        })
    }

    private fun showRecyclerList() {
        progressBar(false)
        main_list?.layoutManager = LinearLayoutManager(context)
        val listHeroAdapter = MovieAdapter(context, rmm!!)
        main_list?.adapter = listHeroAdapter
    }

    private fun progressBar(state: Boolean){
        if (state)
            progressBar?.visibility = View.VISIBLE
        else
            progressBar?.visibility = View.GONE

    }
}
