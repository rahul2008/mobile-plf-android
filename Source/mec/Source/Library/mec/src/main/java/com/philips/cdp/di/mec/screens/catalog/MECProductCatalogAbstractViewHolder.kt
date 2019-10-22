package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

abstract class MECProductCatalogAbstractViewHolder (open val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: MECProduct)
}