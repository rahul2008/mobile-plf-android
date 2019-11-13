package com.philips.cdp.di.mec.screens.Detail

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecImagePagerItemBinding

class ImageAdapter(val assets: List<MECAsset>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return assets.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        var binding = MecImagePagerItemBinding.inflate(inflater)
        binding.mecAsset = assets.get(position)
        binding.executePendingBindings()
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return assets.get(position % assets.size).assetUrl
    }
}