package com.philips.cdp.di.mec.screens.catalog

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding


class MECProductCatalogListAdapter(private val items: MutableList<Pojo>) : MECProductCatalogBaseAbstractAdapter(items) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MecProductCatalogItemListBinding.inflate(inflater)
        return MECProductCatalogHolder(false ,binding!!)
    }

}