package com.philips.cdp.di.mec.screens.features

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecFeaturesItemParentBinding
import com.philips.cdp.prxclient.datamodels.features.FeaturesModel

class ProductFeatureParentRecyclerAdapter (val featuresModel:FeaturesModel) : RecyclerView.Adapter<ProductFeatureParentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ProductFeatureParentViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = MecFeaturesItemParentBinding.inflate(inflater)
        return ProductFeatureParentViewHolder(binding,featuresModel)
    }


    override fun getItemCount(): Int {
        return featuresModel.data.keyBenefitArea.size
    }

    override fun onBindViewHolder(viewHolder : ProductFeatureParentViewHolder, position: Int) {
        viewHolder.bind(featuresModel.data.keyBenefitArea[position])
    }
}