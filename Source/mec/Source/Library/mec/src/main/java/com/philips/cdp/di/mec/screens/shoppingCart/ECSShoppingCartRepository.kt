package com.philips.cdp.di.mec.screens.shoppingCart

import com.bazaarvoice.bvandroidsdk.BulkRatingOptions
import com.bazaarvoice.bvandroidsdk.BulkRatingsRequest
import com.bazaarvoice.bvandroidsdk.EqualityOperator
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.cart.ECSEntries
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder

class ECSShoppingCartRepository(ecsShoppingCartViewModel: EcsShoppingCartViewModel, val ecsServices: ECSServices)
{
    private var ecsShoppingCartCallback= ECSShoppingCartCallback(ecsShoppingCartViewModel)

     fun fetchShoppingCart() {
         this.ecsServices.fetchShoppingCart(ecsShoppingCartCallback)
    }

    fun updateShoppingCart(entries: ECSEntries, quantity: Int) {
        this.ecsServices.updateShoppingCart(quantity,entries,ecsShoppingCartCallback)
    }

    fun fetchProductReview(ecsEntries: MutableList<ECSEntries>, ecsShoppingCartViewModel: EcsShoppingCartViewModel){

        val mecConversationsDisplayCallback = MECBulkRatingCallback(ecsEntries, ecsShoppingCartViewModel)
        val ctnList: MutableList<String> = mutableListOf()

        for(ecsEntry in ecsEntries){
            ctnList.add(ecsEntry.product.code.replace("/","_"))
        }
        val bvClient = MECDataHolder.INSTANCE.bvClient
        val request = MECConstant.KEY_BAZAAR_LOCALE?.let { BulkRatingsRequest.Builder(ctnList, BulkRatingOptions.StatsType.All).addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(it, MECDataHolder.INSTANCE.locale).build() }
        bvClient!!.prepareCall(request).loadAsync(mecConversationsDisplayCallback)

    }

    fun applyVoucher(voucherCode: String, ecsVoucherCallback: ECSVoucherCallback){
        ecsServices.applyVoucher(voucherCode,ecsVoucherCallback)
    }

    fun removeVoucher(voucherCode: String, ecsVoucherCallback: ECSVoucherCallback){
        ecsServices.removeVoucher(voucherCode,ecsVoucherCallback)
    }

    fun createCart(createShoppingCartCallback: ECSCallback<ECSShoppingCart, Exception>){
        ecsServices.createShoppingCart(createShoppingCartCallback)
    }

}