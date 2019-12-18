package com.philips.cdp.di.mec.screens.detail

import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback
import com.bazaarvoice.bvandroidsdk.ConversationsException
import com.bazaarvoice.bvandroidsdk.ReviewResponse
import com.philips.cdp.di.mec.common.MecError

class MECReviewConversationsDisplayCallback(private val ecsProductDetailViewModel: EcsProductDetailViewModel) : ConversationsDisplayCallback<ReviewResponse> {
    override fun onSuccess(response: ReviewResponse) {
        ecsProductDetailViewModel.review.value = response
    }

    override fun onFailure(exception: ConversationsException) {
        //TODO
        val mecError = MecError(null, null)
        ecsProductDetailViewModel.mecError.value = mecError
    }
}