package com.philips.cdp.di.mec.screens.shoppingCart


import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecCartSummaryLayoutBinding

class MECCartSummaryViewHolder(val binding: MecCartSummaryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cartSummary: MECCartSummary) {
        binding.cart = cartSummary
    }
}
