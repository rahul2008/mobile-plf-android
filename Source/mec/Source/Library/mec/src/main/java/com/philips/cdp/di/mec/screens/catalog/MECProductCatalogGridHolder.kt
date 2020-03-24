/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.cdp.di.mec.screens.catalog

import androidx.databinding.ViewDataBinding
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding

class MECProductCatalogGridHolder(override val binding: ViewDataBinding, itemClickListener: ItemClickListener) : MECProductCatalogAbstractViewHolder(binding,itemClickListener) {

    override fun bind(item: MECProductReview) {
            super.bind(item)
            val mecProductCatalogItemGridBinding = binding as MecProductCatalogItemGridBinding
            mecProductCatalogItemGridBinding.product = item
            mecProductCatalogItemGridBinding.executePendingBindings()
    }
}