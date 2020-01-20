package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import android.view.View
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding

class MECProductCatalogGridHolder(override val binding: ViewDataBinding, itemClickListener: ItemClickListener) : MECProductCatalogAbstractViewHolder(binding,itemClickListener) {

    override fun bind(item: MECProductReview) {
            super.bind(item)
            val mecProductCatalogItemGridBinding = binding as MecProductCatalogItemGridBinding
            mecProductCatalogItemGridBinding.product = item
            mecProductCatalogItemGridBinding.executePendingBindings()
        if(adapterPosition == 1) {
            binding.iapRatingBar.visibility = View.GONE
        }
    }
}