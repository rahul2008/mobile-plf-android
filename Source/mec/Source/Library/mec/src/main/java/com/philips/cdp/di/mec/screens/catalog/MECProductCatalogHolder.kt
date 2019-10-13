package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding

class MECProductCatalogHolder (val isGrid: Boolean, val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    // TODO Use constructor overloading instead of boolean check

    fun bind(item: Pojo) {

        if(isGrid){
            val mecProductCatalogItemGridBinding = binding as MecProductCatalogItemGridBinding
            mecProductCatalogItemGridBinding.product = item
            mecProductCatalogItemGridBinding.executePendingBindings()
        }else {
            val mecProductCatalogItemListBinding = binding as MecProductCatalogItemListBinding
            mecProductCatalogItemListBinding.product = item
            mecProductCatalogItemListBinding.executePendingBindings()
        }
    }
}