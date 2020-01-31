package com.philips.cdp.di.mec.screens.shoppingCart

import android.support.v7.widget.RecyclerView
import android.view.View
import com.philips.cdp.di.mec.databinding.MecShoppingCartItemsBinding
import com.philips.platform.uid.view.widget.UIPicker

class MECCartViewHolder(val binding: MecShoppingCartItemsBinding, var mecShoppingCartFragment: MECShoppingCartFragment) : RecyclerView.ViewHolder(binding.root) {

    private var mPopupWindow: UIPicker? = null
    fun bind(cartSummary: MECCartProductReview) {
        binding.cart = cartSummary
        bindCountView(binding.mecQuantityVal, cartSummary)
    }

    private fun bindCountView(view: View, cartSummary: MECCartProductReview) {

        view.setOnClickListener { v ->
            val data = cartSummary
            val stockLevel = cartSummary.entries.product.stock.stockLevel
            /*if (stockLevel > 50) {
                stockLevel = 50
            }*/

            val countPopUp = MecCountDropDown(v, v.context, stockLevel, data.entries.quantity
                    , object : MecCountDropDown.CountUpdateListener {
                override fun countUpdate(oldCount: Int, newCount: Int) {
                    if (newCount != oldCount) {
                        mecShoppingCartFragment.updateCartRequest(cartSummary.entries, newCount)
                    }

                }
            })
            countPopUp.createPopUp(v, stockLevel)
            mPopupWindow = countPopUp.popUpWindow
            countPopUp.show()
        }
    }
}
