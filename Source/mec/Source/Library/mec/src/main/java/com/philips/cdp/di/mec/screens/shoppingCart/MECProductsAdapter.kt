package com.philips.cdp.di.mec.screens.shoppingCart

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.philips.cdp.di.mec.databinding.MecReviewRowBinding
import com.philips.cdp.di.mec.databinding.MecShoppingCartItemsBinding
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogGridHolder
import com.philips.cdp.di.mec.screens.detail.MECReviewsAdapter
import com.philips.cdp.di.mec.screens.reviews.MECReview


class MECProductsAdapter(private val mecCart: MutableList<MECCartProductReview>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(MecShoppingCartItemsBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return mecCart.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartSummary = mecCart?.get(position)
        val viewHolder = holder as ViewHolder
        viewHolder.bind(cartSummary!!)
    }

    private class ViewHolder(val binding: MecShoppingCartItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartSummary: MECCartProductReview) {
            binding.cart = cartSummary
            binding.mecQuantityVal.text = cartSummary.entries.quantity.toString()
        }
    }
}