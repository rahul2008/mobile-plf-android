package com.philips.cdp.di.mec.screens.detail

import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder

class ECSProductDetailRepository(val ecsProductDetailViewModel: EcsProductDetailViewModel, val ecsServices: ECSServices) {

    fun getProductDetail(ecsProduct: ECSProduct){
        ecsServices.fetchProductDetails(ecsProduct,ECSProductDetailCallback(ecsProductDetailViewModel))
    }

    fun fetchProductReview(ctn: String, pageNumber: Int, pageSize: Int){

        val noData = mutableListOf<Review>()
        val reviewsCb = MECReviewConversationsDisplayCallback(ecsProductDetailViewModel)
        val bvClient = MECDataHolder.INSTANCE.bvClient
        val request = ReviewsRequest.Builder(ctn.replace("/","_"), pageSize, pageNumber).addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC).addFilter(ReviewOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter("FilteredStats", "Reviews").build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)
    }

    fun getRatings(ctn: String) {
        val reviewsCb = MECDetailBulkRatingConversationsDisplayCallback(ecsProductDetailViewModel)
        val bvClient = MECDataHolder.INSTANCE.bvClient
        var ctns = mutableListOf(ctn)
        val request = BulkRatingsRequest.Builder(ctns, BulkRatingOptions.StatsType.All).addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)
    }

}