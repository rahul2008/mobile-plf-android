package com.philips.cdp.di.mec.screens.catalog

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecProductCatalogItemBinding


class MECProductCatalogAdapter(private val items: MutableList<Pojo>) : RecyclerView.Adapter<MECProductCatalogAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MecProductCatalogItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(val binding: MecProductCatalogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pojo) {
            binding.product = item
            binding.executePendingBindings()
        }
    }
}