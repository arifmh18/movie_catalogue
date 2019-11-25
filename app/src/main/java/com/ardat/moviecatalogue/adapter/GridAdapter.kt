package com.ardat.moviecatalogue.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ardat.moviecatalogue.activity.MovieDetailActivity
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.model.ResultMovieModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class GridAdapter (private val context: Context?, private val movie : ArrayList<ResultMovieModel>) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movie.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movie[position])
    }

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        private var gambarMovie = view.findViewById<ImageView>(R.id.grid_gambarMovie)
        private var item_grid = view.findViewById<ConstraintLayout>(R.id.item_grid)

        internal fun bind(movie: ResultMovieModel){
            val img = "https://image.tmdb.org/t/p/w185"+movie.poster_path
            Glide.with(context).load(img).apply(RequestOptions.skipMemoryCacheOf(true)).into(gambarMovie)

            item_grid?.setOnClickListener {
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("data",movie)
                context?.startActivity(intent)
            }
        }
    }

}