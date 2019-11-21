package com.ardat.moviecatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultTvModel(
    var vote_average : Double?,
    var vote_count : Int?,
    var poster_path : String?,
    var id : Int?,
    var name : String?,
    var overview : String?,
    var first_air_date : String?
) : Parcelable