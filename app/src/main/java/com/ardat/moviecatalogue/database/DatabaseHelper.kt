package com.ardat.moviecatalogue.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ardat.moviecatalogue.database.DatabaseContract.MovieColoum.Companion.TABLE_NAME

internal class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbnoteapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.MovieColoum._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.MovieColoum.idMovie} INT NOT NULL," +
                " ${DatabaseContract.MovieColoum.judulMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.gambarMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.deskripsiMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.tahunMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.scoreMovie} TEXT NOT NULL)"
    }
}