package com.philips.cdp.di.mec.screens.shoppingCart

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecShoppingCartItemsBinding
import com.philips.platform.uid.view.widget.UIPicker


class MECProductsAdapter(private val mecCart: MutableList<MECCartProductReview>, private var mecShoppingCartFragment: MECShoppingCartFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MECCartViewHolder(MecShoppingCartItemsBinding.inflate(LayoutInflater.from(parent.context)), mecShoppingCartFragment)
    }

    override fun getItemCount(): Int {
        return mecCart.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartSummary = mecCart.get(position)
        val viewHolder = holder as MECCartViewHolder
        viewHolder.bind(cartSummary)
    }

}

