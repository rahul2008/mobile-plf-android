package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding

class MECProductCatalogGridHolder (override val binding: ViewDataBinding) : MECProductCatalogAbstractViewHolder(binding) {

    override fun bind(item: MECProduct) {
            val mecProductCatalogItemGridBinding = binding as MecProductCatalogItemGridBinding
            mecProductCatalogItemGridBinding.product = item
            mecProductCatalogItemGridBinding.executePendingBindings()
    }
}