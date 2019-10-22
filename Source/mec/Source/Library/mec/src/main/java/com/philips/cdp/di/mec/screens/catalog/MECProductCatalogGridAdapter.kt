package com.philips.cdp.di.mec.screens.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding


class MECProductCatalogGridAdapter(private val items: MutableList<MECProduct>) :MECProductCatalogBaseAbstractAdapter(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogAbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = MecProductCatalogItemGridBinding.inflate(inflater)
        return MECProductCatalogGridHolder( binding!!)
    }
}