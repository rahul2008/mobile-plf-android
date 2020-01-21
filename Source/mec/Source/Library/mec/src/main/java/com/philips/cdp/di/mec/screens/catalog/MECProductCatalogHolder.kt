package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import android.util.Log
import android.view.View
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding

class MECProductCatalogHolder(override var binding: ViewDataBinding, override var itemClickListener: ItemClickListener) : MECProductCatalogAbstractViewHolder(binding, itemClickListener) {

    override fun bind(item: MECProductReview) {
        super.bind(item)

        if(binding is  MecProductCatalogItemListBinding) {
            val mecProductCatalogItemListBinding = binding as MecProductCatalogItemListBinding
            mecProductCatalogItemListBinding.product = item
            mecProductCatalogItemListBinding.executePendingBindings()
        }else{
            val mecProductCatalogItemGridBinding = binding as MecProductCatalogItemGridBinding
            mecProductCatalogItemGridBinding.product = item
            mecProductCatalogItemGridBinding.executePendingBindings()
            if (adapterPosition % 2 == 0) {
                    (binding as MecProductCatalogItemGridBinding).verticleView.visibility = View.VISIBLE
            } else {
                (binding as MecProductCatalogItemGridBinding).verticleView.visibility = View.GONE
            }
        }
    }
}