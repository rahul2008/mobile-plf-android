package com.philips.cdp.di.mec.screens.catalog

import android.content.Context
import android.content.Intent
import com.android.volley.toolbox.NetworkImageView
import android.databinding.BindingAdapter
import android.view.View
import com.philips.cdp.di.mec.networkEssentials.NetworkImageLoader


class MECProduct(val code: String, val price: String, val imageUrl: String, val name: String){


    companion object DataBindingAdapter {
        @BindingAdapter("image_url")
        @JvmStatic
        fun loadImage(imageView: View?, image_url: String?) {
            val imageView = imageView as NetworkImageView
            imageView.setImageUrl(image_url!!, NetworkImageLoader.getInstance(imageView.context).imageLoader)
        }
    }

    fun onClick(view: View)
    {
        Utils.startNewActivity(view.context,DetailsActivity::class.java)
    }

    object Utils {

        fun startNewActivity(context: Context, clazz: Class<*>) {

            val intent = Intent(context, clazz)
// To pass any data to next activity
//            intent.putExtra("keyIdentifier", value)
// start your next activity
            context.startActivity(intent)

        }


    }
}
