package com.philips.cdp.di.mec.screens.catalog

import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemGridBinding
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemListBinding


class MECProductCatalogAdapter(private val isGrid: Boolean,private val items: MutableList<Pojo>) : MECProductCatalogBaseAbstractAdapter(items) {

    private lateinit var binding :ViewDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MECProductCatalogAbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        if(isGrid){
            binding = MecProductCatalogItemGridBinding.inflate(inflater)
            return MECProductCatalogGridHolder( binding!!)
        }else{
            binding = MecProductCatalogItemListBinding.inflate(inflater)
            return MECProductCatalogListHolder( binding!!)
        }
    }

}