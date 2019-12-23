package com.ardat.favoritemoviecatalogue.database

import android.database.Cursor
import com.ardat.favoritemoviecatalogue.model.ResultMovieModel
import com.ardat.favoritemoviecatalogue.model.ResultTvModel

object MappingHelper {

    fun mapCursorMovie(cursor: Cursor): ArrayList<ResultMovieModel> {
        val movieList = ArrayList<ResultMovieModel>()
        cursor.moveToFirst()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum._ID))
            val idMovie = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.idMovie))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.judulMovie))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.deskripsiMovie))
            val score = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.scoreMovie))
            val tahun = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.tahunMovie))
            val poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.gambarMovie))
            movieList.add(ResultMovieModel(score.toDouble(),poster,idMovie,title,description,tahun))
        }
        return movieList
    }

    fun mapCursorTv(cursor: Cursor): ArrayList<ResultTvModel> {
        val tvList = ArrayList<ResultTvModel>()
        cursor.moveToFirst()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum._ID))
            val idMovie = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.idMovie))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.judulMovie))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.deskripsiMovie))
            val score = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.scoreMovie))
            val tahun = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.tahunMovie))
            val poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.gambarMovie))
            tvList.add(ResultTvModel(score.toDouble(),poster,idMovie,title,description,tahun))
        }
        return tvList
    }

    fun mapCursorMovieToObject(cursor: Cursor): ResultMovieModel {
        cursor.moveToNext()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum._ID))
        val idMovie = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.idMovie))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.judulMovie))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.deskripsiMovie))
        val score = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.scoreMovie))
        val tahun = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.tahunMovie))
        val poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieColoum.gambarMovie))
        return ResultMovieModel(score.toDouble(),poster,idMovie,title,description,tahun)
    }
}