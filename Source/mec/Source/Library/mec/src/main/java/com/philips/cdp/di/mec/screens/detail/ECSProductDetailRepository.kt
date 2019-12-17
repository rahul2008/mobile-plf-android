package com.philips.cdp.di.mec.screens.detail

import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder

class ECSProductDetailRepository(val ecsProductDetailViewModel: EcsProductDetailViewModel) {

    fun getProductDetail(ecsProduct: ECSProduct){

        val eCSServices = MecHolder.INSTANCE.eCSServices

        eCSServices.fetchProductDetails(ecsProduct,object : ECSCallback<ECSProduct, Exception>{

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsProductDetailViewModel.mecError.value = mecError
            }

            override fun onResponse(ecsProduct: ECSProduct?) {
                ecsProductDetailViewModel.ecsProduct.value = ecsProduct
            }
        })
    }


    fun fetchProductReview(ctn: String, pageNumber: Int, pageSize: Int){

        val noData = mutableListOf<Review>()

        val reviewsCb = object :  ConversationsDisplayCallback<ReviewResponse> {
            override fun onSuccess(response: ReviewResponse) {

                    ecsProductDetailViewModel.review.value = response

            }

            override fun onFailure(exception: ConversationsException) {
                //TODO
                val mecError = MecError(null, null)
                ecsProductDetailViewModel.mecError.value = mecError
            }
        }
        val bvClient = MECDataHolder.INSTANCE.bvClient
        val request = ReviewsRequest.Builder(ctn.replace("/","_"), pageSize, pageNumber).addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC).addFilter(ReviewOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter("FilteredStats", "Reviews").build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)

    }

    fun getRatings(ctn: String) {

        val reviewsCb = object : ConversationsDisplayCallback<BulkRatingsResponse> {
            override fun onSuccess(response: BulkRatingsResponse) {
                if (response.results.isEmpty()) {
                    //Util.showMessage(this@DisplayOverallRatingActivity, "Empty results", "No ratings found for this product")
                } else {
                   // updateData(response.results)
                    ecsProductDetailViewModel.bulkRatingResponse.value = response
                }
            }

            override fun onFailure(exception: ConversationsException) {
                //Util.showMessage(this@DisplayOverallRatingActivity, "Error occurred", Util.bvErrorsToString(exception.errors))
                //TODO Handle errors
            }
        }

        val bvClient = MECDataHolder.INSTANCE.bvClient
        var ctns = mutableListOf(ctn)
        val request = BulkRatingsRequest.Builder(ctns, BulkRatingOptions.StatsType.All).addFilter(BulkRatingOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)

    }

}