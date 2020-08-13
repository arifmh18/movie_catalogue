package com.ardat.moviecatalogue.object_interface

import com.ardat.moviecatalogue.BuildConfig
import com.ardat.moviecatalogue.baserespon.DataMovieBaseRespon
import com.ardat.moviecatalogue.baserespon.DataTvBaseRespon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DataInterface {

    @GET(BuildConfig.BASE_URL+BuildConfig.API_MOVIE+ BuildConfig.API_KEY)
    fun dataMovie() : Call<DataMovieBaseRespon>

    @GET(BuildConfig.BASE_URL+BuildConfig.API_TV+ BuildConfig.API_KEY)
    fun dataTv() : Call<DataTvBaseRespon>

    @GET(BuildConfig.BASE_URL_SEARCH+BuildConfig.API_TV+ BuildConfig.API_KEY+"&language=en-US")
    fun searchDataTv(@Query("query") key:String) : Call<DataTvBaseRespon>

    @GET(BuildConfig.BASE_URL_SEARCH+BuildConfig.API_MOVIE  + BuildConfig.API_KEY+"&language=en-US")
    fun searchDataMovie(@Query("query") key:String) : Call<DataMovieBaseRespon>

    @GET(BuildConfig.BASE_URL+BuildConfig.API_MOVIE  + BuildConfig.API_KEY)
    fun updateNotif(
        @Query("primary_release_date.gte") date :String,
        @Query("primary_release_date.lte") lte :String) : Call<DataMovieBaseRespon>
}