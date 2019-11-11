package com.ardat.moviecatalogue.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ardat.moviecatalogue.model.MovieModel
import com.ardat.moviecatalogue.R

class MovieAdapter internal constructor( private val context: Context) : BaseAdapter() {
    internal var movie = arrayListOf<MovieModel>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = convertView
        if (v == null){
            v = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        }

        val viewHolder = ViewHolder(v as View)

        val movie = getItem(position) as MovieModel
        viewHolder.bind(movie)
        return v
    }

    private inner class ViewHolder internal constructor(view: View){
        private var gambarMovie = view.findViewById<ImageView>(R.id.gambarMovie)
        private var judulMovie = view.findViewById<TextView>(R.id.judulMovie)

        internal fun bind(movie: MovieModel){
            gambarMovie.setImageResource(movie.gambarMovie!!)
            judulMovie.setText(movie.judulMovie)
        }
    }

    override fun getItem(position: Int): Any {
        return movie[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return movie.size
    }

}