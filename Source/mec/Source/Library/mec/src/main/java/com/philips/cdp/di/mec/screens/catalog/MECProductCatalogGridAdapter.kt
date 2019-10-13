package com.philips.cdp.di.mec.screens.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding


class MECProductCatalogGridAdapter(private val items: MutableList<Pojo>) : MECProductCatalogBaseAbstractAdapter(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MecProductCatalogItemGridBinding.inflate(inflater)
        return MECProductCatalogHolder(true ,binding!!)
    }
}