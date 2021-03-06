package com.ardat.moviecatalogue.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.adapter.GridTvAdapter
import com.ardat.moviecatalogue.adapter.MovieTvAdapter
import com.ardat.moviecatalogue.baserespon.DataMovieBaseRespon
import com.ardat.moviecatalogue.baserespon.DataTvBaseRespon
import com.ardat.moviecatalogue.model.ResultMovieModel
import com.ardat.moviecatalogue.model.ResultTvModel
import com.ardat.moviecatalogue.object_interface.DataInterface
import com.ardat.moviecatalogue.object_interface.RetrofitBuilder
import kotlinx.android.synthetic.main.fragment_tv.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvFragment : Fragment(), View.OnClickListener {
    private var main_list : RecyclerView? = null
    private var tv_grid_view : ImageButton? = null
    private var tv_list_view : ImageButton? = null
    private var progressBar : ProgressBar? = null
    private var tv_search : EditText? = null
    private var tv_searchBtn : ImageButton? = null
    private var v : View? = null

    private var dataInterface : DataInterface? = null
    private var show = 0

    private var tv : List<ResultTvModel>? = null
    private var rmm : ArrayList<ResultTvModel>? = null

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

        if(savedInstanceState != null) {
            rmm  = savedInstanceState.get("obj") as ArrayList<ResultTvModel>
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
        main_list = v?.findViewById(R.id.tv_list) as RecyclerView
        tv_grid_view = v?.findViewById(R.id.tv_grid_view) as ImageButton
        tv_list_view = v?.findViewById(R.id.tv_list_view) as ImageButton
        tv_search = v?.findViewById(R.id.tv_search) as EditText
        tv_searchBtn = v?.findViewById(R.id.tv_searchBtn) as ImageButton
        progressBar = v?.findViewById(R.id.progressBar) as ProgressBar

        main_list?.setHasFixedSize(true)

        dataInterface = RetrofitBuilder.builder(context!!).create(DataInterface::class.java)

        tv_search?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s)){
                    getData()
                } else {
                    searchData(s.toString())
                }
            }

        })
        tv_search?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val key = tv_search?.text.toString()
                searchData(key)
                return@OnKeyListener true
            }
            false
        })

        tv_grid_view?.setOnClickListener(this)
        tv_list_view?.setOnClickListener(this)
    }

    private fun searchData(key:String){
        val callSearch = dataInterface!!.searchDataTv(key)
        callSearch.enqueue(object: Callback<DataTvBaseRespon>{
            override fun onFailure(call: Call<DataTvBaseRespon>, t: Throwable) {
            }

            override fun onResponse(call: Call<DataTvBaseRespon>, response: Response<DataTvBaseRespon>) {
                if (response.isSuccessful) {
                    val respon = response.body() as DataTvBaseRespon
                    tv = respon.results
                    progressBar?.visibility = View.GONE

                    rmm = ArrayList()

                    for (i in 0 until tv!!.size) {
                        rmm!!.add(
                            ResultTvModel(
                                tv!!.get(i).vote_average,
                                tv!!.get(i).poster_path,
                                tv!!.get(i).id,
                                tv!!.get(i).name,
                                tv!!.get(i).overview,
                                tv!!.get(i).first_air_date
                            )
                        )
                    }

                    showRecyclerList()
                }
            }

        })
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
        if (rmm!!.size > 0) {
            progressBar(false)
            main_list?.layoutManager = GridLayoutManager(context, 3)
            val gridTvAdapter = GridTvAdapter(context, rmm!!)
            main_list?.adapter = gridTvAdapter
            main_list?.visibility = View.VISIBLE
            tv_no_data?.visibility = View.GONE
        } else {
            main_list?.visibility = View.GONE
            tv_no_data?.visibility = View.VISIBLE
        }
    }

    private fun getData() {
        progressBar(true)
        val callData = dataInterface!!.dataTv()
        callData.enqueue(object: Callback<DataTvBaseRespon>{
            override fun onFailure(call: Call<DataTvBaseRespon>, t: Throwable) {

            }

            override fun onResponse(call: Call<DataTvBaseRespon>, response: Response<DataTvBaseRespon>) {
                if (response.isSuccessful){
                    val respon = response.body() as DataTvBaseRespon
                    tv = respon.results
                    progressBar?.visibility = View.GONE

                    rmm = ArrayList()

                    for(i in 0 until tv!!.size) {
                        rmm!!.add(ResultTvModel(tv!!.get(i).vote_average, tv!!.get(i).poster_path, tv!!.get(i).id, tv!!.get(i).name,  tv!!.get(i).overview,  tv!!.get(i).first_air_date))
                    }

                    showRecyclerList()
                }
            }

        })
    }

    private fun showRecyclerList() {
        progressBar(false)
        if (rmm!!.size > 0) {
            main_list?.layoutManager = LinearLayoutManager(context)
            val listTvAdapter = MovieTvAdapter(context, rmm!!)
            main_list?.adapter = listTvAdapter
            main_list?.visibility = View.VISIBLE
            tv_no_data?.visibility = View.GONE
        } else {
            main_list?.visibility = View.GONE
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
