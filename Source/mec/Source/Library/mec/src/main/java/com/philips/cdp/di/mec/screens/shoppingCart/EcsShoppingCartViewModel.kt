package com.philips.cdp.di.mec.screens.shoppingCart

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.cart.ECSEntries
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.integration.MecHolder

class EcsShoppingCartViewModel : CommonViewModel() {

    var ecsShoppingCart = MutableLiveData<ECSShoppingCart>()

    val ecsProductsReviewList = MutableLiveData<MutableList<MECCartProductReview>>()

    var ecsServices = MecHolder.INSTANCE.eCSServices

    var ecsShoppingCartRepository = ECSShoppingCartRepository(this,ecsServices)

    fun getShoppingCart(){
        ecsShoppingCartRepository.fetchShoppingCart()
    }

    fun fetchProductReview(entries: MutableList<ECSEntries>) {
        ecsShoppingCartRepository.fetchProductReview(entries, this)
    }
}