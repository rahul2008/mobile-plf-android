package com.philips.cdp.di.mec.utils

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.networkEssentials.NetworkImageLoader
import com.philips.cdp.di.mec.screens.detail.ImageAdapter
import com.philips.cdp.di.mec.screens.detail.MECAsset

class DataBindingUtility {

    companion object DataBindingAdapter {


        @BindingAdapter("image_url")
        @JvmStatic
        fun loadImage(imageView: View?, image_url: String?) {

            val imageView = imageView as NetworkImageView
            val imageLoader = NetworkImageLoader.getInstance(imageView.context).imageLoader
            imageLoader.get(image_url, ImageLoader.getImageListener(imageView,
                    R.drawable
                            .no_icon, R.drawable
                    .no_icon))

            imageView.setImageUrl(image_url!!, imageLoader)
        }

        @JvmStatic
        @BindingAdapter("assets")
        fun setAdapter(pager: ViewPager,assets :List<MECAsset>) {
            pager.adapter = ImageAdapter(assets)
        }
    }

}