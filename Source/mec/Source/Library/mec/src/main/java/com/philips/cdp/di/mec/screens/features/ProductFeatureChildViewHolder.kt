package com.philips.cdp.di.mec.screens.features

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecFeatureItemChildBinding
import com.philips.cdp.di.mec.databinding.MecSpecItemChildBinding
import com.philips.cdp.prxclient.datamodels.features.FeatureItem
import com.philips.cdp.prxclient.datamodels.features.FeaturesModel
import com.philips.cdp.prxclient.datamodels.specification.CsItemItem

class ProductFeatureChildViewHolder(val binding: ViewDataBinding,private val featuresModel: FeaturesModel) : RecyclerView.ViewHolder(binding.root){

     fun bind(item: FeatureItem) {
         val mecFeatureItemChildBinding = binding as MecFeatureItemChildBinding
         mecFeatureItemChildBinding.featureItem = item
         mecFeatureItemChildBinding.data = featuresModel.data
     }
}