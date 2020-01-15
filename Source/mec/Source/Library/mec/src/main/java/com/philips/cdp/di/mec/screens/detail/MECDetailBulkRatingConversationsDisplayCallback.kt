package com.philips.cdp.di.mec.screens.detail

import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback
import com.bazaarvoice.bvandroidsdk.ConversationsException
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.mec.common.MecError

class MECDetailBulkRatingConversationsDisplayCallback(private val ecsProductDetailViewModel: EcsProductDetailViewModel) : ConversationsDisplayCallback<BulkRatingsResponse> {

    override fun onSuccess(response: BulkRatingsResponse) {
            ecsProductDetailViewModel.bulkRatingResponse.value = response
    }

    override fun onFailure(exception: ConversationsException) {

        val exception = Exception("Fetch Rating failed")
        val ecsError = ECSError(1000,"Fetch Rating failed")
        val mecError = MecError(exception, ecsError)
        ecsProductDetailViewModel.mecError.value =mecError
    }
}