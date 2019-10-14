package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding

class MECProductCatalogGridHolder (override val binding: ViewDataBinding) : MECProductCatalogAbstractViewHolder(binding) {

    override fun bind(item: Pojo) {
            val mecProductCatalogItemGridBinding = binding as MecProductCatalogItemGridBinding
            mecProductCatalogItemGridBinding.product = item
            mecProductCatalogItemGridBinding.executePendingBindings()
    }
}