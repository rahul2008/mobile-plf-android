package com.philips.cdp.di.mec.screens.shoppingCart


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.philips.cdp.di.mec.databinding.MecCartSummaryLayoutBinding
import com.philips.cdp.di.mec.databinding.MecShoppingCartItemsBinding
import com.philips.cdp.di.mec.networkEssentials.NetworkImageLoader
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uid.view.widget.UIPicker

class MECCartSummaryViewHolder(val binding: MecCartSummaryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cartSummary: MECCartSummary) {
        binding.cart = cartSummary
    }
}
