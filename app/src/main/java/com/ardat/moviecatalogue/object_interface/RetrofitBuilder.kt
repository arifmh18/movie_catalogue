package com.ardat.moviecatalogue.object_interface

import android.content.Context
import com.ardat.moviecatalogue.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    fun builder(context: Context): Retrofit {
        val okhttpBuilder = OkHttpClient().newBuilder()
        okhttpBuilder.callTimeout(60, TimeUnit.SECONDS)
        okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS)
        okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS)
        okhttpBuilder.readTimeout(60, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okhttpBuilder.addInterceptor(interceptor)
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okhttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}