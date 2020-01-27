package com.philips.cdp.di.mec.screens.specification

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecSpecItemChildBinding
import com.philips.cdp.prxclient.datamodels.specification.CsItemItem

class SpecificationChildViewHolder( val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root){

     fun bind(item: CsItemItem) {
         val mecSpecItemChildBinding = binding as MecSpecItemChildBinding
         mecSpecItemChildBinding.csItemItem = item
     }
}