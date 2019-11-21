package com.ardat.moviecatalogue.baserespon

import com.ardat.moviecatalogue.model.ResultMovieModel

class DataMovieBaseRespon {
    internal var page: Int? = null
    internal var total_results: Int? = null
    internal var total_pages: Int? = null
    internal var results: List<ResultMovieModel>? = null
}