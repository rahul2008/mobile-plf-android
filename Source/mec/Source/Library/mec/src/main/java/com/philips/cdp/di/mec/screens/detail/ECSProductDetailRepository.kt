package com.philips.cdp.di.mec.screens.detail

import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.screens.catalog.EcsProductViewModel
import com.philips.cdp.di.mec.screens.catalog.MECProductReview
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import java.text.DecimalFormat

class ECSProductDetailRepository {

    fun getProductDetail(ecsProduct: ECSProduct, ecsProductDetailViewModel: EcsProductDetailViewModel){

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


    fun fetchProductReview(ctn : String, pageNumber : Int, pageSize : Int, ecsProductViewModel: EcsProductDetailViewModel){

        val reviewsCb = object :  ConversationsDisplayCallback<ReviewResponse> {
            override fun onSuccess(response: ReviewResponse) {
                if (response.results.isEmpty()) {
                 //TODO Handle this

                } else {
                    ecsProductViewModel.review.value = response.results
                }
            }

            override fun onFailure(exception: ConversationsException) {
               //TODO HANDLE error through MECError
            }
        }
        val bvClient = MECDataHolder.INSTANCE.bvClient
        val request = ReviewsRequest.Builder(ctn.replace("/","_"), pageSize, pageNumber).addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC).addFilter(ReviewOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter("FilteredStats", "Reviews").build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)

    }

}