package com.philips.cdp.di.mec.screens.detail

import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder

class ECSProductDetailRepository(private val ecsProductDetailViewModel: EcsProductDetailViewModel, val ecsServices: ECSServices) {

    var ecsProductDetailCallBack= ECSProductDetailCallback(ecsProductDetailViewModel)
    var ecsShoppingCartCallback = ECSShoppingCartCallback(ecsProductDetailViewModel)

    var bvClient = MECDataHolder.INSTANCE.bvClient
    var reviewsCb = MECReviewConversationsDisplayCallback(ecsProductDetailViewModel)
    var ratingCb = MECDetailBulkRatingConversationsDisplayCallback(ecsProductDetailViewModel)

    fun getProductDetail(ecsProduct: ECSProduct){
        ecsServices.fetchProductDetails(ecsProduct,ecsProductDetailCallBack)
    }

    fun fetchProductReview(ctn: String, pageNumber: Int, pageSize: Int){
        val request = ReviewsRequest.Builder(ctn.replace("/","_"), pageSize, pageNumber).addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC).addFilter(ReviewOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter("FilteredStats", "Reviews").build()
        val prepareCall = bvClient!!.prepareCall(request)
        prepareCall.loadAsync(reviewsCb)
    }

    fun getRatings(ctn: String) {
        var ctns = mutableListOf(ctn)
        val request = BulkRatingsRequest.Builder(ctns, BulkRatingOptions.StatsType.All).addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).build()
        val prepareCall = bvClient!!.prepareCall(request)
        prepareCall.loadAsync(ratingCb)
    }

    fun addTocart(ecsProduct: ECSProduct){
        ecsServices.addProductToShoppingCart(ecsProduct,ecsShoppingCartCallback)
    }

}