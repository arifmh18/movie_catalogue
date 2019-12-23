package com.ardat.moviecatalogue.fragment

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
import com.ardat.moviecatalogue.adapter.GridTvAdapter
import com.ardat.moviecatalogue.adapter.MovieAdapter
import com.ardat.moviecatalogue.adapter.MovieTvAdapter
import com.ardat.moviecatalogue.baserespon.DataTvBaseRespon
import com.ardat.moviecatalogue.database.DatabaseContract
import com.ardat.moviecatalogue.database.DatabaseContract.MovieColoum.Companion.CONTENT_URI_TV
import com.ardat.moviecatalogue.database.MappingHelper
import com.ardat.moviecatalogue.database.MovieHelper
import com.ardat.moviecatalogue.database.TvHelper
import com.ardat.moviecatalogue.model.ResultTvModel
import com.ardat.moviecatalogue.object_interface.DataInterface
import com.ardat.moviecatalogue.object_interface.RetrofitBuilder
import kotlinx.android.synthetic.main.fragment_tv.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteTvFragment : Fragment(), View.OnClickListener {
    private var main_list : RecyclerView? = null
    private var tv_grid_view : ImageButton? = null
    private var tv_list_view : ImageButton? = null
    private var progressBar : ProgressBar? = null
    private var v : View? = null

    private var dataInterface : DataInterface? = null
    private var tvHelper : TvHelper? = null
    private var show = 0
    private var mContext : Context? = null

    private var rmm : ArrayList<ResultTvModel>? = null

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
        v =  inflater.inflate(R.layout.fragment_tv, container, false)

        init()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadTvAsync()
            }
        }

        mContext?.contentResolver?.registerContentObserver(CONTENT_URI_TV, true, myObserver)

        if(savedInstanceState != null) {
            rmm  = savedInstanceState.get("obj") as ArrayList<ResultTvModel>
            show  = savedInstanceState.get("show") as Int
            if (show == 0){
                showRecyclerList()
            } else {
                showRecyclerGrid()
            }
        } else {
            loadTvAsync()
        }
        return v
    }

    private fun init(){
        main_list = v?.findViewById(R.id.tv_list) as RecyclerView
        tv_grid_view = v?.findViewById(R.id.tv_grid_view) as ImageButton
        tv_list_view = v?.findViewById(R.id.tv_list_view) as ImageButton
        progressBar = v?.findViewById(R.id.progressBar) as ProgressBar

        main_list?.setHasFixedSize(true)

        dataInterface = RetrofitBuilder.builder(context!!).create(DataInterface::class.java)

        tv_grid_view?.setOnClickListener(this)
        tv_list_view?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.tv_list_view -> {
                show = 0
                showRecyclerList()
            }
            R.id.tv_grid_view -> {
                show = 1
                showRecyclerGrid()
            }
        }
    }

    private fun showRecyclerGrid() {
        progressBar(false)
        if (rmm!!.size > 0) {
            main_list?.layoutManager = GridLayoutManager(context, 3)
            val gridHeroAdapter = GridTvAdapter(context, rmm!!)
            main_list?.adapter = gridHeroAdapter
        }
        else {
            tv_no_data?.visibility = View.VISIBLE
        }

    }

    private fun loadTvAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar(true)
            val deferredMovie = async(Dispatchers.IO) {
                val cursor = mContext?.contentResolver?.query(CONTENT_URI_TV, null, null, null, null) as Cursor
                MappingHelper.mapCursorTv(cursor)
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
            val listHeroAdapter = MovieTvAdapter(context, rmm!!)
            main_list?.adapter = listHeroAdapter
        }
        else {
            tv_no_data?.visibility = View.VISIBLE
        }
    }

    private fun progressBar(state: Boolean){
        if (state)
            progressBar?.visibility = View.VISIBLE
        else
            progressBar?.visibility = View.GONE

    }
}
