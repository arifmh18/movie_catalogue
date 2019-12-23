package com.ardat.favoritemoviecatalogue

import android.database.ContentObserver
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardat.favoritemoviecatalogue.adapter.MovieAdapter
import com.ardat.favoritemoviecatalogue.database.DatabaseContract
import com.ardat.favoritemoviecatalogue.database.MappingHelper
import com.ardat.favoritemoviecatalogue.model.ResultMovieModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var rmm : ArrayList<ResultMovieModel>? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("obj", rmm)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadMovieAsync()
            }
        }
        contentResolver?.registerContentObserver(DatabaseContract.MovieColoum.CONTENT_URI, true, myObserver)

        if(savedInstanceState != null) {
            rmm  = savedInstanceState.get("obj") as ArrayList<ResultMovieModel>
        } else {
            loadMovieAsync()
        }

    }

    private fun loadMovieAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar(true)
            val deferredMovie = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(DatabaseContract.MovieColoum.CONTENT_URI, null, null, null, null) as Cursor
                MappingHelper.mapCursorMovie(cursor)
            }
            rmm = deferredMovie.await()
            showRecyclerList()
        }
    }

    private fun showRecyclerList() {
        progressBar(false)
        if (rmm!!.size > 0) {
            fav_list?.visibility = View.VISIBLE
            fav_no_data?.visibility = View.GONE

            fav_list?.layoutManager = LinearLayoutManager(this)
            val listFavAdapter = MovieAdapter(this, rmm!!)
            fav_list?.adapter = listFavAdapter
        }
        else {
            fav_list?.visibility = View.GONE
            fav_no_data?.visibility = View.VISIBLE
        }
    }

    private fun progressBar(state: Boolean){
        if (state)
            progressBar?.visibility = View.VISIBLE
        else
            progressBar?.visibility = View.GONE

    }
}
