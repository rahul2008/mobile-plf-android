package com.philips.cdp.di.mec.screens.shoppingCart

import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback
import com.bazaarvoice.bvandroidsdk.ConversationsException
import com.bazaarvoice.bvandroidsdk.Statistics
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.model.cart.ECSEntries
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.screens.catalog.MECProductReview
import java.text.DecimalFormat

class MECBulkRatingCallback(val ecsProducts: MutableList<ECSEntries>, val ecsShoppingCartViewModel: EcsShoppingCartViewModel) : ConversationsDisplayCallback<BulkRatingsResponse> {


    override fun onSuccess(response: BulkRatingsResponse) {

        if (response.results.isEmpty()) {
            //ecsProductViewModel.mecError = //Util.showMessage(this@DisplayOverallRatingActivity, "Empty results", "No ratings found for this product")
        } else {
            createMECProductReviewObject(ecsProducts,response.results)
        }
    }

    override fun onFailure(exception: ConversationsException) {

        val ecsError = ECSError(1000,exception.localizedMessage)
        val mecError = MecError(exception, ecsError)
        ecsShoppingCartViewModel.mecError.value = mecError
    }


    private fun createMECProductReviewObject(ecsProducts: MutableList<ECSEntries>, statisticsList: List<Statistics>) {

        var mecProductReviewList :MutableList<MECCartProductReview> = mutableListOf()

        for(ecsProduct in ecsProducts){

            for(statistics in statisticsList){

                if(ecsProduct.product.code isEqualsTo statistics.productStatistics.productId){

                    mecProductReviewList.add (MECCartProductReview(ecsProduct, DecimalFormat("#.#").format(statistics.productStatistics.reviewStatistics.averageOverallRating), " ("+statistics.productStatistics.reviewStatistics.totalReviewCount.toString()+ " reviews)"))
                }
            }
        }

        ecsShoppingCartViewModel.ecsProductsReviewList.value = mecProductReviewList
    }

    infix fun String.isEqualsTo(value: String): Boolean = this.replace("/", "_").equals(value)
}