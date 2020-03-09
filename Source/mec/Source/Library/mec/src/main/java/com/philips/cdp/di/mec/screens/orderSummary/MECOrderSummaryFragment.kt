package com.philips.cdp.di.mec.screens.orderSummary


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.ecs.model.payment.ECSPayment
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ItemClickListener
import com.philips.cdp.di.mec.databinding.MecOrderSummaryFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.shoppingCart.MECCartSummary
import com.philips.cdp.di.mec.screens.shoppingCart.MECCartSummaryAdapter
import com.philips.cdp.di.mec.utils.MECConstant


/**
 * A simple [Fragment] subclass.
 */
class MECOrderSummaryFragment : MecBaseFragment(), ItemClickListener {
    companion object {
        val TAG = "MECOrderSummaryFragment"
    }

    private lateinit var binding: MecOrderSummaryFragmentBinding
    private lateinit var ecsShoppingCart: ECSShoppingCart
    private lateinit var ecsAddress: ECSAddress
    private lateinit var ecsPayment: ECSPayment
    private var cartSummaryAdapter: MECCartSummaryAdapter? = null
    private var productsAdapter: MECOrderSummaryProductsAdapter? = null
    private lateinit var cartSummaryList: MutableList<MECCartSummary>
    override fun onItemClick(item: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFragmentTag(): String {
        return "MECOrderSummaryFragment"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MecOrderSummaryFragmentBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.shoppingCart

        ecsAddress = arguments?.getSerializable(MECConstant.KEY_ECS_ADDRESS) as ECSAddress
        ecsShoppingCart = arguments?.getSerializable(MECConstant.KEY_ECS_SHOPPING_CART) as ECSShoppingCart
        binding.ecsAddressShipping = ecsAddress
        binding.shoppingCart = ecsShoppingCart
//        binding.ecsPaymentMode = ecsPayment
        cartSummaryList = mutableListOf()
        cartSummaryAdapter = MECCartSummaryAdapter(addCartSummaryList(ecsShoppingCart))
        productsAdapter = MECOrderSummaryProductsAdapter(ecsShoppingCart)
        binding.mecCartSummaryRecyclerView.adapter = productsAdapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_checkout, true)
    }

    private fun addCartSummaryList(ecsShoppingCart: ECSShoppingCart): MutableList<MECCartSummary> {
        cartSummaryList.clear()
        var name: String
        var price: String
        for (i in 0 until ecsShoppingCart.entries.size) {
            name = ecsShoppingCart.entries[i].quantity.toString() + "x " + ecsShoppingCart.entries.get(i).product.summary.productTitle
            price = ecsShoppingCart.entries.get(i).totalPrice.formattedValue
            cartSummaryList.add(MECCartSummary(name, price))
        }

        if (ecsShoppingCart.deliveryCost != null) {
            name = getString(R.string.mec_delivery_cost)
            price = ecsShoppingCart.deliveryCost.formattedValue
            cartSummaryList.add(MECCartSummary(name, price))
        }

        for (i in 0 until ecsShoppingCart.appliedVouchers.size) {
            if (ecsShoppingCart.appliedVouchers.get(i).name == null) {
                name = " "
            } else {
                name = ecsShoppingCart.appliedVouchers.get(i).name
            }
            price = "-" + ecsShoppingCart.appliedVouchers?.get(i)?.appliedValue?.formattedValue
            cartSummaryList.add(MECCartSummary(name, price))

        }
/*
        for (i in 0..shoppingCart.appliedOrderPromotions.size - 1) {
            name = shoppingCart.appliedOrderPromotions.get(i).promotion.description
            price = "-" + shoppingCart.appliedOrderPromotions.get(i).promotion.promotionDiscount.formattedValue
            cartSummaryList.add(MECCartSummary(name, price))
        }*/

        /*for (i in 0..shoppingCart.appliedProductPromotions.size - 1) {
            name = shoppingCart.appliedProductPromotions.get(i).promotion.description
            price = "-" + shoppingCart.appliedProductPromotions.get(i).promotion.promotionDiscount.formattedValue
            cartSummaryList.add(MECCartSummary(name, price))
        }*/

        cartSummaryAdapter?.notifyDataSetChanged()
        return cartSummaryList
    }
}
