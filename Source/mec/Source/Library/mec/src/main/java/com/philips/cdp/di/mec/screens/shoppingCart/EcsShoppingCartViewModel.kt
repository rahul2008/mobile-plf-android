package com.philips.cdp.di.mec.screens.shoppingCart

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.cart.ECSEntries
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.integration.MecHolder

open class EcsShoppingCartViewModel : CommonViewModel() {

    var ecsShoppingCart = MutableLiveData<ECSShoppingCart>()

    val ecsProductsReviewList = MutableLiveData<MutableList<MECCartProductReview>>()

    var ecsServices = MecHolder.INSTANCE.eCSServices

    private var ecsShoppingCartRepository = ECSShoppingCartRepository(this,ecsServices)

    fun getShoppingCart(){
        this.ecsShoppingCartRepository.fetchShoppingCart()
    }

    fun updateQuantity(entries: ECSEntries, quantity: Int) {
        this.ecsShoppingCartRepository.updateShoppingCart(entries,quantity)
    }

    fun fetchProductReview(entries: MutableList<ECSEntries>) {
        this.ecsShoppingCartRepository.fetchProductReview(entries, this)
    }
}