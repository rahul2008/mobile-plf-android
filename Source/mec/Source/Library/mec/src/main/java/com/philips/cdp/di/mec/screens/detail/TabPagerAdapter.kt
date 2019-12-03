package com.philips.cdp.di.mec.screens.detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.philips.cdp.di.mec.utils.MECConstant


class TabPagerAdapter (fm: FragmentManager,var ctn: String) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MECProductInfoFragment()
            }
            1 -> {
                MECProductSpecsFragment()
            }
            else -> {
                val bundle = Bundle()
                bundle.putString(MECConstant.MEC_PRODUCT_CTN,ctn)
                val fragment = MECProductReviewsFragment()
                fragment.arguments = bundle
                return fragment
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Info"
            1 -> "Specs"
            else -> {
                return "Reviews"
            }
        }
    }
}
