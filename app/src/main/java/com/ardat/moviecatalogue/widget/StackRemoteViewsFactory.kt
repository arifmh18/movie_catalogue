package com.ardat.moviecatalogue.widget

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.adapter.MovieAdapter
import com.ardat.moviecatalogue.database.DatabaseContract
import com.ardat.moviecatalogue.database.DatabaseContract.MovieColoum.Companion.CONTENT_URI
import com.ardat.moviecatalogue.database.MappingHelper
import com.ardat.moviecatalogue.model.ResultMovieModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException


class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()
    private var rmm : ArrayList<ResultMovieModel>? = null
    private var list: Cursor? = null

    override fun onCreate() {
        list = mContext.contentResolver.query(
            CONTENT_URI, null, null, null, null
        )
//        println("paap widget create")
//        val handlerThread = HandlerThread("DataObserver")
//        handlerThread.start()
//        val handler = Handler(handlerThread.looper)
//        val myObserver = object : ContentObserver(handler) {
//            override fun onChange(self: Boolean) {
//            }
//        }
//
//        mContext.contentResolver?.registerContentObserver(DatabaseContract.MovieColoum.CONTENT_URI, true, myObserver)
//
//        GlobalScope.launch(Dispatchers.Main) {
//            val deferredMovie = async(Dispatchers.IO) {
//                val cursor = mContext.contentResolver?.query(DatabaseContract.MovieColoum.CONTENT_URI, null, null, null, null) as Cursor
//                MappingHelper.mapCursorMovie(cursor)
//            }
//            rmm = deferredMovie.await()
//        }
    }

    override fun onDataSetChanged() {
//        if (list != null)
//            list?.close()
//
//        val token = Binder.clearCallingIdentity()
//
//        try {
//            println("paap widget size "+rmm?.size)
//            for (i in 0 until rmm!!.size){
//                val poster = rmm!!.get(i).poster_path!!
//                var bitmap: Bitmap? = null
//                bitmap = Glide.with(mContext)
//                    .asBitmap()
//                    .load("\"https://image.tmdb.org/t/p/w154" + poster)
//                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get()
//                mWidgetItems.add(bitmap)
//            }
//        } catch (e: ExecutionException){
//            e.printStackTrace()
//        }
//        Binder.restoreCallingIdentity(token)
//
        //Ini berfungsi untuk melakukan refresh saat terjadi perubahan.
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.darth_vader))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.star_wars_logo))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.storm_trooper))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.starwars))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.falcon))
    }

    override fun onDestroy() {

    }

    private fun getItem(position: Int): ResultMovieModel {
        println("paap "+position)
        check(list!!.moveToPosition(position)) { "Position invalid!" }
        val helper = MappingHelper.mapCursorMovieToObject(list!!)

        return ResultMovieModel(helper.vote_average,helper.poster_path,helper.id, helper.title, helper.overview, helper.release_date)
    }

    override fun getCount(): Int = list!!.count

    override fun getViewAt(position: Int): RemoteViews {
        val item = getItem(position)

        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        var bitmap: Bitmap? = null
        try {
            bitmap = Glide.with(mContext)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w154" + item.poster_path)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        rv.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(
            WidgetFavouriteMovie.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false


}
