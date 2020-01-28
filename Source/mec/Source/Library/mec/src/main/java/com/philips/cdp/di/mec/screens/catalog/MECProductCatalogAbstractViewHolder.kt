package com.philips.cdp.di.mec.screens.catalog

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.mec.common.ItemClickListener

abstract class MECProductCatalogAbstractViewHolder(open val binding: ViewDataBinding, open val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(binding.root) {

    open fun bind(item: MECProductReview){

        binding.root.setOnClickListener { itemClickListener.onItemClick(item as Object) }
    }
}