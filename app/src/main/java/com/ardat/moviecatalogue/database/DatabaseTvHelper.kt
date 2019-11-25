package com.ardat.moviecatalogue.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ardat.moviecatalogue.database.DatabaseContract.MovieColoum.Companion.TABLE_NAME
import com.ardat.moviecatalogue.database.DatabaseContract.MovieColoum.Companion.TABLE_NAME_TV
import com.ardat.moviecatalogue.database.Utils.URL.DATABASE_NAME
import com.ardat.moviecatalogue.database.Utils.URL.DATABASE_VERSION

internal class DatabaseTvHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Utils.URL.SQL_CREATE_TABLE_TV)
        db?.execSQL(Utils.URL.SQL_CREATE_TABLE_MOVIE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_TV")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}