package com.ardat.favoritemoviecatalogue.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.ardat.moviecatalogue"
    const val SCHEME = "content"

    class MovieColoum : BaseColumns {
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

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

            val CONTENT_URI_TV: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME_TV)
                .build()
        }
    }
}