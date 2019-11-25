package com.ardat.moviecatalogue.database

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class MovieColoum : BaseColumns {
        companion object {
            const val TABLE_NAME = "movieCatalogue"
            const val TABLE_NAME_TV = "tvCatalogue"
            const val _ID = "_id"
            const val idMovie = "id_movie"
            const val judulMovie = "judul"
            const val gambarMovie = "gambar"
            const val deskripsiMovie = "deskripsi"
            const val tahunMovie = "tahun"
            const val scoreMovie = "score"
        }
    }
}