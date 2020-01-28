package com.philips.cdp.di.mec.screens.shoppingCart

import com.bazaarvoice.bvandroidsdk.BulkRatingOptions
import com.bazaarvoice.bvandroidsdk.BulkRatingsRequest
import com.bazaarvoice.bvandroidsdk.EqualityOperator
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.model.cart.ECSEntries
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder

class ECSShoppingCartRepository(private val ecsShoppingCartViewModel: EcsShoppingCartViewModel, val ecsServices: ECSServices)
{
    var ecsShoppingCartCallback= ECSShoppingCartCallback(ecsShoppingCartViewModel)

     fun fetchShoppingCart() {
         ecsServices.fetchShoppingCart(ecsShoppingCartCallback)
    }

    fun updateShoppingCart(entries: ECSEntries, quantity: Int) {
        ecsServices.updateShoppingCart(quantity,entries,ecsShoppingCartCallback)
    }

    fun fetchProductReview(ecsEntries: MutableList<ECSEntries>, ecsShoppingCartViewModel: EcsShoppingCartViewModel){

        val mecConversationsDisplayCallback = MECBulkRatingCallback(ecsEntries, ecsShoppingCartViewModel)
        var ctnList: MutableList<String> = mutableListOf()

        for(ecsEntry in ecsEntries){
            ctnList.add(ecsEntry.product.code.replace("/","_"))
        }
        val bvClient = MECDataHolder.INSTANCE.bvClient
        val request = BulkRatingsRequest.Builder(ctnList, BulkRatingOptions.StatsType.All).addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).build()
        bvClient!!.prepareCall(request).loadAsync(mecConversationsDisplayCallback)

    }

}