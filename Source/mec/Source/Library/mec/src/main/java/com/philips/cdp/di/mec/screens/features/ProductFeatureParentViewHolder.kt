package com.philips.cdp.di.mec.screens.features

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import com.philips.cdp.di.mec.databinding.MecFeaturesItemParentBinding
import com.philips.cdp.prxclient.datamodels.features.Data
import com.philips.cdp.prxclient.datamodels.features.FeaturesModel
import com.philips.cdp.prxclient.datamodels.features.KeyBenefitAreaItem


class ProductFeatureParentViewHolder (val binding: ViewDataBinding,val featuresModel: FeaturesModel) : RecyclerView.ViewHolder(binding.root){

    fun bind(item: KeyBenefitAreaItem) {
        val mecFeaturesItemParentBinding = binding as MecFeaturesItemParentBinding

        val mLayoutManager = object : LinearLayoutManager(binding.root.context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        mecFeaturesItemParentBinding.recyclerChild.layoutManager = mLayoutManager
        mecFeaturesItemParentBinding.keyBenefitAreaItem = item
        mecFeaturesItemParentBinding.featureModel = featuresModel
    }
}