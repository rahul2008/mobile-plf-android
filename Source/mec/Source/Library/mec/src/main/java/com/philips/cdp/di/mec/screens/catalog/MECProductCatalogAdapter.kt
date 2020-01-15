package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding


class MECProductCatalogAdapter(items: MutableList<MECProductReview>, private val itemClickListener : ItemClickListener) :MECProductCatalogBaseAbstractAdapter(items) {

    private lateinit var binding: ViewDataBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogAbstractViewHolder {


        val inflater = LayoutInflater.from(parent.context)
        if(catalogView == CatalogView.LIST){
            binding = MecProductCatalogItemListBinding.inflate(inflater)
        }else{
            binding = MecProductCatalogItemGridBinding.inflate(inflater)
        }

        return MECProductCatalogHolder( binding ,itemClickListener!!)
    }


}