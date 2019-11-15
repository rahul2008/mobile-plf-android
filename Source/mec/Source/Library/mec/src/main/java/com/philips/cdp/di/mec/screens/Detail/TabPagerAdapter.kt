package com.philips.cdp.di.mec.screens.Detail


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.philips.cdp.di.mec.screens.detail.MECProductInfoFragment
import com.philips.cdp.di.mec.screens.detail.MECProductReviewsFragment


class TabPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MECProductInfoFragment()
            }
            else -> {
                return MECProductReviewsFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Info"
            else -> {
                return "Reviews"
            }
        }
    }
}
