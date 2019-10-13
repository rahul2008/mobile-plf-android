package com.philips.cdp.di.mec.screens.catalog

import com.android.volley.toolbox.NetworkImageView
import android.databinding.BindingAdapter
import android.view.View
import com.philips.cdp.di.mec.networkEssentials.NetworkImageLoader


class Pojo (val name: String, val price: String ,val imageUrl: String){


    companion object DataBindingAdapter {
        @BindingAdapter("image_url")
        @JvmStatic
        fun loadImage(imageView: View?, image_url: String?) {
            val imageView = imageView as NetworkImageView
            imageView.setImageUrl(image_url!!, NetworkImageLoader.getInstance(imageView.context).imageLoader)
        }
    }
}
