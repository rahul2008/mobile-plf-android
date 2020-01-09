package com.philips.cdp.di.mec.screens.detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.philips.cdp.di.mec.screens.features.MECProductFeaturesFragment
import com.philips.cdp.di.mec.screens.specification.SpecificationFragment
import com.philips.cdp.di.mec.utils.MECConstant


class TabPagerAdapter (fm: FragmentManager,var ctn: String) : FragmentStatePagerAdapter(fm) {



    override fun getItem(position: Int): Fragment {

        val bundle = Bundle()
        bundle.putString(MECConstant.MEC_PRODUCT_CTN,ctn)

        return when (position) {
            0 -> {
                MECProductInfoFragment()
            }
            1 -> {
                val specificationFragment = SpecificationFragment()
                specificationFragment.arguments = bundle
                return specificationFragment
            }
            2 -> {
                val productFeaturesFragment = MECProductFeaturesFragment()
                productFeaturesFragment.arguments = bundle
                return productFeaturesFragment
            }
            else -> {

                val fragment = MECProductReviewsFragment()
                fragment.arguments = bundle
                return fragment
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Info"
            1 -> "Specs"
            2 -> "Features"
            else -> {
                return "Reviews"
            }
        }
    }
}
