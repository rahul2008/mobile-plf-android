package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ItemClickListener

abstract class MECProductCatalogAbstractViewHolder(open val binding: ViewDataBinding, open val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(binding.root) {

    open fun bind(item: ECSProduct){

        binding.root.setOnClickListener(object : View.OnClickListener{

            override fun onClick(v: View?) {
                itemClickListener.onItemClick(item as Object)
            }
        })
     }
}