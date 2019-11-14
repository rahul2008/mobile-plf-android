package com.philips.cdp.di.mec.screens.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding


class MECProductCatalogListAdapter(private val items: MutableList<ECSProduct> ,private val itemClickListener : ItemClickListener) :MECProductCatalogBaseAbstractAdapter(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogAbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MecProductCatalogItemListBinding.inflate(inflater)
        return MECProductCatalogListHolder( binding!! ,itemClickListener!!)
    }
}