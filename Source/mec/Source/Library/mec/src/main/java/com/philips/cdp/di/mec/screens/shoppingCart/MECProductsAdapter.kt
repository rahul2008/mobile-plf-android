package com.philips.cdp.di.mec.screens.shoppingCart


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecShoppingCartItemsBinding
import com.philips.platform.uid.view.widget.UIPicker


class MECProductsAdapter(private val mecCart: MutableList<MECCartProductReview>,  var mEcsShoppingCartViewModel: EcsShoppingCartViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(MecShoppingCartItemsBinding.inflate(LayoutInflater.from(parent.context)), mEcsShoppingCartViewModel)
    }

    override fun getItemCount(): Int {
        return mecCart.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartSummary = mecCart?.get(position)
        val viewHolder = holder as ViewHolder
        viewHolder.bind(cartSummary!!, position)
    }

    open class ViewHolder(val binding: MecShoppingCartItemsBinding, var ecsShoppingCartViewModel: EcsShoppingCartViewModel) : RecyclerView.ViewHolder(binding.root) {

        var mPopupWindow: UIPicker? = null
        fun bind(cartSummary: MECCartProductReview, position: Int) {
            binding.cart = cartSummary
            //binding.mecQuantityVal.text = cartSummary.entries.quantity.toString()
            bindCountView(binding.mecQuantityVal, cartSummary)
        }

        fun bindCountView(view: View, cartSummary: MECCartProductReview) {

            view.setOnClickListener { v ->
                val data = cartSummary
                var stockLevel = cartSummary.entries.product.stock.stockLevel
                if (stockLevel > 50) {
                    stockLevel = 50
                }

                val countPopUp = MecCountDropDown(v, v.context, stockLevel, data.entries.quantity
                        , object : MecCountDropDown.CountUpdateListener {
                    override fun countUpdate(oldCount: Int, newCount: Int) {
                        if (newCount > oldCount) {
                            ecsShoppingCartViewModel.updateQuantity(cartSummary.entries, newCount)
                        }

                    }
                })
                countPopUp.createPopUp(v, stockLevel)
                mPopupWindow = countPopUp.popUpWindow
                countPopUp.show()
            }
        }
    }

    open class CloseWindow(mPopupWindow: UIPicker?) {
        var mPopupWindow: UIPicker? = mPopupWindow
        fun onStop() {
            if (mPopupWindow != null) {
                mPopupWindow!!.dismiss()
            }
        }
    }

}

