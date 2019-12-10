package com.philips.cdp.di.mec.screens.specification

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecSpecItemChildBinding
import com.philips.cdp.prxclient.datamodels.specification.CsItemItem


class SpecificationChildRecyclerAdapter (val csItem: List<CsItemItem>) : RecyclerView.Adapter<SpecificationChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SpecificationChildViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MecSpecItemChildBinding.inflate(inflater)
        return SpecificationChildViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return csItem.size
    }

    override fun onBindViewHolder(viewHolder : SpecificationChildViewHolder, position: Int) {
       viewHolder.bind(csItem.get(position))
    }
}