package com.ardat.favoritemoviecatalogue.database

class Utils {
    object URL {
        const val DATABASE_NAME = "dbmovieapp"
        const val DATABASE_VERSION = 1
        const val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE ${DatabaseContract.MovieColoum.TABLE_NAME}" +
                " (${DatabaseContract.MovieColoum._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.MovieColoum.idMovie} INT NOT NULL," +
                " ${DatabaseContract.MovieColoum.judulMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.gambarMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.deskripsiMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.tahunMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.scoreMovie} TEXT NOT NULL)"

        const val SQL_CREATE_TABLE_TV = "CREATE TABLE ${DatabaseContract.MovieColoum.TABLE_NAME_TV}" +
                " (${DatabaseContract.MovieColoum._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.MovieColoum.idMovie} INT NOT NULL," +
                " ${DatabaseContract.MovieColoum.judulMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.gambarMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.deskripsiMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.tahunMovie} TEXT NOT NULL," +
                " ${DatabaseContract.MovieColoum.scoreMovie} TEXT NOT NULL)"
    }

}