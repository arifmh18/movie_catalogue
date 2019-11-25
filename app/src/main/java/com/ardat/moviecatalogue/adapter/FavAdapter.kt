package com.ardat.moviecatalogue.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.fragment.FavoriteMovieFragment
import com.ardat.moviecatalogue.fragment.FavoriteTvFragment
import com.ardat.moviecatalogue.fragment.TvFragment

class FavAdapter(mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var mv = mContext.resources.getString(R.string.movie)
    private var tv = mContext.resources.getString(R.string.tv)

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return FavoriteMovieFragment()
            1 -> return FavoriteTvFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return when (position){
            0 -> mv
            else -> {
                return tv
            }
        }
    }
}