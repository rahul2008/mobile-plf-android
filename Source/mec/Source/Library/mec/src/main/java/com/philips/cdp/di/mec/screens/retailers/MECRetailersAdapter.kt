package com.philips.cdp.di.mec.screens.retailers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.retailers.ECSRetailer
import com.philips.cdp.di.mec.databinding.MecRetailersItemBinding
import com.philips.cdp.di.mec.screens.reviews.MECReview

class MECRetailersAdapter (private val mecRetailers: List<ECSRetailer>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(MecRetailersItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val retailers = mecRetailers?.get(position)
        val viewHolder = holder as ViewHolder
        viewHolder.bind(retailers!!)
    }

    private class ViewHolder(val binding: MecRetailersItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(retailer: ECSRetailer) {
            binding.mecRetailers = retailer
            binding.iapRetailerItemOnlineStoreNameLebel.text = retailer.name
        }
    }

    override fun getItemCount(): Int {
        return mecRetailers?.size!!
    }
}