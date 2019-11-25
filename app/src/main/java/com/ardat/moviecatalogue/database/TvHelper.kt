package com.ardat.moviecatalogue.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ardat.moviecatalogue.database.DatabaseContract.MovieColoum.Companion.TABLE_NAME_TV
import com.ardat.moviecatalogue.database.DatabaseContract.MovieColoum.Companion._ID
import com.ardat.moviecatalogue.database.DatabaseContract.MovieColoum.Companion.idMovie

class TvHelper (context: Context){

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME_TV
        private lateinit var dataBaseHelper: DatabaseTvHelper
        private var INSTANCE: TvHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): TvHelper{
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = TvHelper(context)
                    }
                }
            }
            return INSTANCE as TvHelper
        }
    }

    init {
        dataBaseHelper = DatabaseTvHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null)
    }

    fun queryById(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$idMovie = ?", arrayOf(id), null, null, null, null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$idMovie = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$idMovie = '$id'", null)
    }
}