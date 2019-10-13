package com.philips.cdp.di.mec.screens.catalog

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


abstract class MECProductCatalogBaseAbstractAdapter(private val items: MutableList<Pojo>) : RecyclerView.Adapter<MECProductCatalogHolder>() {

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogHolder ;

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MECProductCatalogHolder, position: Int) = holder.bind(items[position])

}