package com.ardat.moviecatalogue.object_interface

import com.ardat.moviecatalogue.BuildConfig
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.baserespon.DataMovieBaseRespon
import com.ardat.moviecatalogue.baserespon.DataTvBaseRespon
import retrofit2.Call
import retrofit2.http.GET

interface DataInterface {

    @GET(BuildConfig.BASE_URL+BuildConfig.API_MOVIE+ BuildConfig.API_KEY)
    fun dataMovie() : Call<DataMovieBaseRespon>

    @GET(BuildConfig.BASE_URL+BuildConfig.API_TV+ BuildConfig.API_KEY)
    fun dataTv() : Call<DataTvBaseRespon>
}