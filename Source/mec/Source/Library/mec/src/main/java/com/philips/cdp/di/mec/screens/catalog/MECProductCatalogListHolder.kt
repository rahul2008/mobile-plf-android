package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding

class MECProductCatalogListHolder (override var binding: ViewDataBinding) : MECProductCatalogAbstractViewHolder(binding) {

    override fun bind(item: Pojo) {
        val mecProductCatalogItemListBinding = binding as MecProductCatalogItemListBinding
        mecProductCatalogItemListBinding.product = item
        mecProductCatalogItemListBinding.executePendingBindings()
    }
}