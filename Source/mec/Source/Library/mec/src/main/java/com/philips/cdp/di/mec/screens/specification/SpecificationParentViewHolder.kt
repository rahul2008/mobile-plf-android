package com.philips.cdp.di.mec.screens.specification

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecSpecItemParentBinding
import com.philips.cdp.prxclient.datamodels.specification.CsChapterItem
import android.support.v7.widget.LinearLayoutManager




class SpecificationParentViewHolder (val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(item: CsChapterItem) {
        val mecSpecItemParentBinding = binding as MecSpecItemParentBinding

        val mLayoutManager = object : LinearLayoutManager(binding.root.context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        mecSpecItemParentBinding.recyclerChild.layoutManager = mLayoutManager
        mecSpecItemParentBinding.csChapterItem = item
    }
}