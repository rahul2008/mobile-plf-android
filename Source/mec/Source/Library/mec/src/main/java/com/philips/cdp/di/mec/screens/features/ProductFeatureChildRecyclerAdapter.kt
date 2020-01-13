package com.philips.cdp.di.mec.screens.features

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecFeaturesItemChildBinding
import com.philips.cdp.prxclient.datamodels.features.FeatureItem


class ProductFeatureChildRecyclerAdapter(private val featureItems: List<FeatureItem>) : RecyclerView.Adapter<ProductFeatureChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ProductFeatureChildViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MecFeaturesItemChildBinding.inflate(inflater)
        return ProductFeatureChildViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return featureItems.size
    }

    override fun onBindViewHolder(viewHolder : ProductFeatureChildViewHolder, position: Int) {
       viewHolder.bind(featureItems[position])
    }
}