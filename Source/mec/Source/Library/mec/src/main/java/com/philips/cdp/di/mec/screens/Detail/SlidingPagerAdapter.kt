package com.philips.cdp.di.mec.screens.Detail

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SlidingPagerAdapter(fragmentManager: FragmentManager?) : FragmentPagerAdapter(fragmentManager) {

    private val TITLES = arrayOf("Info", "Reviews")
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                println("position 0");
                return MECProductInfoFragment()
            }

            1 -> {
                println("position 1");
                return MECProductReviewsFragment()
            }
            else ->
                return MECProductInfoFragment()

        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return TITLES[position]
    }

    override fun getCount(): Int {
        return TITLES.size
    }

}