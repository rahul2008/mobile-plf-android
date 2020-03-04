package com.philips.cdp.di.mec.screens.detail


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.screens.features.MECProductFeaturesFragment
import com.philips.cdp.di.mec.screens.specification.SpecificationFragment
import com.philips.cdp.di.mec.utils.MECConstant


class TabPagerAdapter(var fm: FragmentManager, var ctn: String, var context: Context) : FragmentPagerAdapter(fm) {



    override fun getItem(position: Int): Fragment {

        val bundle = Bundle()
        bundle.putString(MECConstant.MEC_PRODUCT_CTN,ctn)



        return when (position) {
            0 -> {
                MECProductInfoFragment()
            }
            1 -> {
                val productFeaturesFragment = MECProductFeaturesFragment()
                productFeaturesFragment.arguments = bundle
                return productFeaturesFragment
            }
            2 -> {


                val specificationFragment = SpecificationFragment()
                specificationFragment.arguments = bundle
                return specificationFragment
            }

            3 -> {

                val fragment = MECProductReviewsFragment()
                fragment.arguments = bundle
                return fragment
            }
            else ->{
                MECProductInfoFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> context.getString(R.string.mec_info)
            1 -> context.getString(R.string.mec_features)
            2 -> context.getString(R.string.mec_specs)
            3 -> context.getString(R.string.mec_reviews)
            else -> {
                context.getString(R.string.mec_info)
            }
        }
    }
}
