package com.ardat.moviecatalogue.fragment

import android.app.Activity
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.adapter.GridAdapter
import com.ardat.moviecatalogue.adapter.MovieAdapter
import com.ardat.moviecatalogue.database.DatabaseContract.MovieColoum.Companion.CONTENT_URI
import com.ardat.moviecatalogue.database.MappingHelper
import com.ardat.moviecatalogue.database.MovieHelper
import com.ardat.moviecatalogue.model.ResultMovieModel
import com.ardat.moviecatalogue.object_interface.DataInterface
import com.ardat.moviecatalogue.object_interface.RetrofitBuilder
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteMovieFragment : Fragment(), View.OnClickListener {
    private var main_list : RecyclerView? = null
    private var movie_grid_view : ImageButton? = null
    private var movie_list_view : ImageButton? = null
    private var progressBar : ProgressBar? = null
    private var v : View? = null
    private var mContext : Context? = null

    private var dataInterface : DataInterface? = null
    private var show = 0

    private var rmm : ArrayList<ResultMovieModel>? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

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

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadMovieAsync()
            }
        }

        mContext?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

        if(savedInstanceState != null) {
            rmm  = savedInstanceState.get("obj") as ArrayList<ResultMovieModel>
            show  = savedInstanceState.get("show") as Int
            if (show == 0){
                showRecyclerList()
            } else {
                showRecyclerGrid()
            }
        } else {
            loadMovieAsync()
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
        if (rmm!!.size > 0) {
            main_list?.layoutManager = GridLayoutManager(context, 3)
            val gridHeroAdapter = GridAdapter(context, rmm!!)
            main_list?.adapter = gridHeroAdapter
        }
        else {
            movie_no_data?.visibility = View.VISIBLE
        }
    }

    private fun loadMovieAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar(true)
            val deferredMovie = async(Dispatchers.IO) {
                val cursor = mContext?.contentResolver?.query(CONTENT_URI, null, null, null, null) as Cursor
                MappingHelper.mapCursorMovie(cursor)
            }
            progressBar(false)
            rmm = deferredMovie.await()
            showRecyclerList()
        }
    }

    private fun showRecyclerList() {
        progressBar(false)
        if (rmm!!.size > 0) {
            main_list?.layoutManager = LinearLayoutManager(context)
            val listHeroAdapter = MovieAdapter(context, rmm!!)
            main_list?.adapter = listHeroAdapter
        }
        else {
            movie_no_data?.visibility = View.VISIBLE
        }
    }

    private fun progressBar(state: Boolean){
        if (state)
            progressBar?.visibility = View.VISIBLE
        else
            progressBar?.visibility = View.GONE

    }
}
