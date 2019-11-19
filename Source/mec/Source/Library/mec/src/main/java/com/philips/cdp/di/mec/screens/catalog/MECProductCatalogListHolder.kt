package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding

class MECProductCatalogListHolder(override var binding: ViewDataBinding, override var itemClickListener: ItemClickListener) : MECProductCatalogAbstractViewHolder(binding, itemClickListener) {

    override fun bind(item: MECProductReview) {
        super.bind(item)
        val mecProductCatalogItemListBinding = binding as MecProductCatalogItemListBinding
        mecProductCatalogItemListBinding.product = item
        mecProductCatalogItemListBinding.executePendingBindings()
    }
}