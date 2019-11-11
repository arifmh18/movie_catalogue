package com.ardat.moviecatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieModel(
    var gambarMovie : Int?,
    var judulMovie : String?,
    var deskripsiMovie : String?,
    var tahunMovie : String?,
    var scoreMovie : String?
) : Parcelable