package com.ardat.favoritemoviecatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultMovieModel(
    var vote_average : Double?,
    var poster_path : String?,
    var id : Int?,
    var title : String?,
    var overview : String?,
    var release_date : String?
) : Parcelable