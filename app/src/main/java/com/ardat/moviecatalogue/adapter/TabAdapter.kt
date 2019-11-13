package com.ardat.moviecatalogue.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ardat.moviecatalogue.fragment.MovieFragment
import com.ardat.moviecatalogue.fragment.TvFragment

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return MovieFragment()
            1 -> return TvFragment()
        }
        return null
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "Movie"
            else -> {
                return "Serial TV"
            }
        }
    }
}