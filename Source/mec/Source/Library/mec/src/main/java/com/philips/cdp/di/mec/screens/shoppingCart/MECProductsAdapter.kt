package com.philips.cdp.di.mec.screens.shoppingCart


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.philips.cdp.di.mec.databinding.MecShoppingCartItemsBinding


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

