package com.philips.cdp.di.mec.utils

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.mec.networkEssentials.NetworkImageLoader
import com.philips.cdp.di.mec.screens.detail.ImageAdapter
import com.philips.cdp.di.mec.screens.specification.SpecificationChildRecyclerAdapter
import com.philips.cdp.di.mec.screens.specification.SpecificationParentRecyclerAdapter
import com.philips.cdp.prxclient.datamodels.specification.CsItemItem
import com.philips.cdp.prxclient.datamodels.specification.CsValueItem
import com.philips.cdp.prxclient.datamodels.specification.SpecificationModel
import com.philips.platform.uid.view.widget.Label
import android.widget.TextView




class DataBindingUtility {

    companion object DataBindingAdapter {


        @BindingAdapter("image_url")
        @JvmStatic
        fun loadImage(imageView: View?, image_url: String?) {

            val imageView = imageView as NetworkImageView
            val imageLoader = NetworkImageLoader.getInstance(imageView.context).imageLoader
            imageLoader.get(image_url, ImageLoader.getImageListener(imageView, 0,com.philips.cdp.di.mec.R.drawable.no_icon))

            imageView.setImageUrl(image_url!!, imageLoader)
        }

        @JvmStatic
        @BindingAdapter("assets")
        fun setAdapter(pager: ViewPager, assets: List<Asset>) {
            pager.adapter = ImageAdapter(assets)
        }

        @JvmStatic
        @BindingAdapter("addView")
        fun addView(layout: LinearLayout, hack: Int) {

            val mecBannerEnabler = MECDataHolder.INSTANCE.mecBannerEnabler

            if (mecBannerEnabler != null && mecBannerEnabler.bannerView != null) {

                if (mecBannerEnabler.bannerView.parent != null) {
                    val viewGroup = mecBannerEnabler.bannerView.parent as ViewGroup
                    viewGroup.removeAllViews()
                }

                layout.addView(mecBannerEnabler.bannerView)
                layout.visibility = View.VISIBLE
            }
        }

        @JvmStatic
        @BindingAdapter("setRating")
        fun setRating(ratingBar: RatingBar, rating: String) {
            ratingBar.rating = rating.toFloat()
        }



        //For specification
        @JvmStatic
        @BindingAdapter("items")
        fun setAdapter(recyclerView: RecyclerView, csItemItems: List<CsItemItem>?) {
            if(csItemItems!=null)
            recyclerView.adapter = SpecificationChildRecyclerAdapter(csItemItems)
        }
        @JvmStatic
        @BindingAdapter("specification")
        fun setSpecificationAdapter(recyclerView: RecyclerView, specificationModel: SpecificationModel?) {
            if(specificationModel!=null)
            recyclerView.adapter = SpecificationParentRecyclerAdapter(specificationModel)
        }

        @JvmStatic
        @BindingAdapter("setCsValueItems")
        fun setCSItem(label: Label,csItemItem: CsItemItem) {

            val csValueItems = csItemItem.csValue
            var unit =""

            if(csItemItem.unitOfMeasure!=null){
                unit = csItemItem.unitOfMeasure.unitOfMeasureSymbol
            }

            val disclaimerStringBuilder = StringBuilder()

            if (!csValueItems.isNullOrEmpty()) {

                if(csValueItems.size == 1){
                    label.text = csValueItems.get(0).csValueName +" "+unit
                    return
                }

                for (csValueItem in csValueItems) {
                    disclaimerStringBuilder.append("- ").append(csValueItem.csValueName).append(System.getProperty("line.separator"))
                }
                label.text = disclaimerStringBuilder.toString()+unit
            }
        }


    }

}