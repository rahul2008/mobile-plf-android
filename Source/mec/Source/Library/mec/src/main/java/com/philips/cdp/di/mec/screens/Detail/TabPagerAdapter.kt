package com.philips.cdp.di.mec.screens.Detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.philips.cdp.di.mec.screens.detail.MECProductInfoFragment
import com.philips.cdp.di.mec.screens.detail.MECProductReviewsFragment
import com.philips.cdp.di.mec.utils.MECConstant


class TabPagerAdapter (fm: FragmentManager,var ctn: String) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MECProductInfoFragment()
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
