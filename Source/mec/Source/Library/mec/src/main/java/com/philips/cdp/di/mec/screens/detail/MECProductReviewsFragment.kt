package com.philips.cdp.di.mec.screens.detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bazaarvoice.bvandroidsdk.*
import com.google.gson.internal.LinkedTreeMap

import com.philips.cdp.di.mec.databinding.MecProductReviewFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.reviews.MECReview
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder

/**
 * A simple [Fragment] subclass.
 */
class MECProductReviewsFragment : MecBaseFragment() {

    var offset: Int = 0
    var limit: Int = 20

    private var reviewsAdapter: MECReviewsAdapter? = null
    var productctn = null
    private lateinit var mecReviews: MutableList<MECReview>

    private val reviewsCb = object : ConversationsDisplayCallback<ReviewResponse> {
        override fun onSuccess(response: ReviewResponse) {

            val reviews = response.results

            if (reviews.isEmpty()) {
                binding.mecProductCatalogEmptyLabel.visibility = View.VISIBLE
            } else {

                for(review in reviews){
                    binding.mecProductCatalogEmptyLabel.visibility = View.GONE
                    val nick = if (review.userNickname!=null) review.userNickname else "Anonymous"

                    mecReviews.add(MECReview(review.title, review.reviewText, review.rating.toString(), nick, review.lastModificationDate, getValueFor("Pros", review), getValueFor("Cons", review),getValueForUseDuration(review)))

                }

                reviewsAdapter!!.notifyDataSetChanged()
            }
        }

        override fun onFailure(exception: ConversationsException) {

        }
    }

    private lateinit var binding: MecProductReviewFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MecProductReviewFragmentBinding.inflate(inflater, container, false)

        mecReviews = mutableListOf<MECReview>()

        val bundle = arguments
        var productctn = bundle!!.getString(MECConstant.MEC_PRODUCT_CTN)


        //TODO in binding
        reviewsAdapter = MECReviewsAdapter(mecReviews)
        binding.recyclerView.adapter = reviewsAdapter
        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        val bvClient = MECDataHolder.INSTANCE.bvClient
        val request = ReviewsRequest.Builder("HD9653_90", limit, offset).addSort(ReviewOptions.Sort.SubmissionTime, SortOrder.DESC).addFilter(ReviewOptions.Filter.ContentLocale, EqualityOperator.EQ, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter(MECConstant.KEY_BAZAAR_LOCALE, MECDataHolder.INSTANCE.locale).addCustomDisplayParameter("FilteredStats", "Reviews").build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)
        /*val request = ReviewsRequest.Builder(Constants.PRODUCT_ID, 20, 0).build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)*/
        return binding.root
    }

    private fun getValueFor(type: String, review: Review): String {
        var reviewValue: String? = null
        var mapAdditionalFields: LinkedTreeMap<String, String>? = null
        if (review.additionalFields != null && review.additionalFields.size > 0) {
            mapAdditionalFields = review.additionalFields.get(type) as LinkedTreeMap<String, String>
            reviewValue = if (mapAdditionalFields != null && mapAdditionalFields?.get("Value") != null) mapAdditionalFields?.get("Value") else ""
        }
        if (reviewValue == null) {
            if (review.tagDimensions != null && review.tagDimensions!!.size > 0) {
                val tagD = review.tagDimensions?.get(type.substring(0, type.length - 1))
                reviewValue = tagD?.values.toString()
            }
        }
        return if (reviewValue.toString() != null) reviewValue.toString() else ""
    }

    private fun getValueForUseDuration( review: Review): String {
        var useDurationValue: String? = ""
        if (review.contextDataValues != null && review.contextDataValues!!.size>0 && (null!=review.contextDataValues!!.get("HowLongHaveYouBeenUsingThisProduct"))) {
            val mapUseDuration: ContextDataValue = review.contextDataValues!!.get("HowLongHaveYouBeenUsingThisProduct") as ContextDataValue
            if(null!=mapUseDuration){
                useDurationValue=mapUseDuration.valueLabel
            }
        }
        return useDurationValue.toString()
    }


}
