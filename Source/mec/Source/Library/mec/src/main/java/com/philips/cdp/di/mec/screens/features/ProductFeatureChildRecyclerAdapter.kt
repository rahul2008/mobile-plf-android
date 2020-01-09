package com.philips.cdp.di.mec.screens.features

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecSpecItemChildBinding
import com.philips.cdp.di.mec.screens.specification.SpecificationChildViewHolder
import com.philips.cdp.prxclient.datamodels.features.FeatureItem
import com.philips.cdp.prxclient.datamodels.features.FeaturesModel
import com.philips.cdp.prxclient.datamodels.specification.CsItemItem


class ProductFeatureChildRecyclerAdapter (private val featureItems: List<FeatureItem> , private val featuresModel: FeaturesModel) : RecyclerView.Adapter<ProductFeatureChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ProductFeatureChildViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MecSpecItemChildBinding.inflate(inflater)
        return ProductFeatureChildViewHolder(binding,featuresModel)
    }

    override fun getItemCount(): Int {
        return featureItems.size
    }

    override fun onBindViewHolder(viewHolder : ProductFeatureChildViewHolder, position: Int) {
       viewHolder.bind(featureItems[position])
    }
}