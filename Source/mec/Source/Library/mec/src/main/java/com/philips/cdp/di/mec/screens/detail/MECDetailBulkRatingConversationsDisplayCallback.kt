package com.philips.cdp.di.mec.screens.detail

import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback
import com.bazaarvoice.bvandroidsdk.ConversationsException

class MECDetailBulkRatingConversationsDisplayCallback(private val ecsProductDetailViewModel: EcsProductDetailViewModel) : ConversationsDisplayCallback<BulkRatingsResponse> {

    override fun onSuccess(response: BulkRatingsResponse) {
        if (response.results.isEmpty()) {
            //TODO
        } else {
            ecsProductDetailViewModel.bulkRatingResponse.value = response
        }
    }

    override fun onFailure(exception: ConversationsException) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}