package com.philips.cdp.di.mec.screens.features

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecFeaturesItemChildBinding
import com.philips.cdp.prxclient.datamodels.features.FeatureItem

class ProductFeatureChildViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root){

     fun bind(item: FeatureItem) {

         val mecFeatureItemChildBinding = binding as MecFeaturesItemChildBinding
         mecFeatureItemChildBinding.featureItem = item
     }
}