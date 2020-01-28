package com.philips.cdp.di.mec.screens.shoppingCart


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecShoppingCartFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.platform.uid.view.widget.UIPicker


/**
 * A simple [Fragment] subclass.
 */
class MECShoppingCartFragment : MecBaseFragment() {

    private lateinit var binding: MecShoppingCartFragmentBinding
    private lateinit var mPopupWindow: UIPicker
    private lateinit var ecsShoppingCart: ECSShoppingCart
    lateinit var ecsShoppingCartViewModel: EcsShoppingCartViewModel
    private var productsAdapter: MECProductsAdapter? = null
    private lateinit var productReviewList: MutableList<MECCartProductReview>

    private val cartObserver: Observer<ECSShoppingCart> = object : Observer<ECSShoppingCart> {
        override fun onChanged(ecsShoppingCart: ECSShoppingCart?) {
            binding.shoppingCart = ecsShoppingCart

            if (ecsShoppingCart!!.entries.size != 0) {
                ecsShoppingCartViewModel.fetchProductReview(ecsShoppingCart!!.entries)
            }
            hideProgressBar()
        }
    }

    private val productReviewObserver: Observer<MutableList<MECCartProductReview>> = Observer { mecProductReviews ->
        productReviewList.clear()
        mecProductReviews?.let { productReviewList.addAll(it) }
        productsAdapter?.notifyDataSetChanged()
        hideProgressBar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = MecShoppingCartFragmentBinding.inflate(inflater, container, false)
        binding.fragment = this
        ecsShoppingCartViewModel = activity!!?.let { ViewModelProviders.of(it).get(EcsShoppingCartViewModel::class.java) }!!

        ecsShoppingCartViewModel.ecsShoppingCart.observe(this, cartObserver)
        ecsShoppingCartViewModel.ecsProductsReviewList.observe(this, productReviewObserver)

        val bundle = arguments
        ecsShoppingCart = bundle?.getSerializable(MECConstant.MEC_SHOPPING_CART) as ECSShoppingCart

        productReviewList = mutableListOf()

        productsAdapter = MECProductsAdapter(productReviewList,ecsShoppingCartViewModel)

        binding.mecCartSummaryRecyclerView.adapter = productsAdapter

        binding.mecCartSummaryRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        setTitleAndBackButtonVisibility(R.string.mec_shopping_cart, true)
    }

    override fun onStart() {
        super.onStart()
        if (ecsShoppingCart!!.entries.size != 0) {
            ecsShoppingCartViewModel.fetchProductReview(ecsShoppingCart!!.entries)
        }
        //executeRequest()
    }

    fun executeRequest() {
        //createCustomProgressBar(container, MEDIUM)
        ecsShoppingCartViewModel.getShoppingCart()
    }

    override fun onStop() {
        super.onStop()
        if(productsAdapter!=null){
            MECProductsAdapter.CloseWindow(mPopupWindow).onStop()
        }

    }
}
