package com.philips.cdp.di.mec.screens.detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bazaarvoice.bvandroidsdk.*

import com.philips.cdp.di.mec.databinding.MecProductReviewFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.reviews.Constants
import com.philips.cdp.di.mec.screens.reviews.MECReview
import com.philips.cdp.di.mec.utils.MECDataHolder

/**
 * A simple [Fragment] subclass.
 */
class MECProductReviewsFragment : MecBaseFragment() {

    private var reviewsAdapter: MECReviewsAdapter? = null
    private lateinit var mecReviews:MutableList<MECReview>

    private val reviewsCb = object : ConversationsDisplayCallback<ReviewResponse> {
        override fun onSuccess(response: ReviewResponse) {

            val reviews = response.results

            if (reviews.isEmpty()) {
                //showMessage(this@DisplayReviewsActivity, "Empty results", "No reviews found for this product")
            } else {

                for(review in reviews){
                    mecReviews.add(MECReview(review.title,review.reviewText,review.rating.toString(),review.lastModificationDate.toString(),"pros","cons"))

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


        //TODO in binding
        reviewsAdapter = MECReviewsAdapter(mecReviews)
        binding.recyclerView.adapter = reviewsAdapter
        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        val bvClient = MECDataHolder.INSTANCE.bvClient
        val request = ReviewsRequest.Builder(Constants.PRODUCT_ID, 20, 0).addSort(ReviewOptions.Sort.SubmissionTime,SortOrder.DESC).addFilter(ReviewOptions.Filter.ContentLocale,EqualityOperator.EQ,"en_US").addCustomDisplayParameter("Locale","de_DE").addCustomDisplayParameter("FilteredStats","Reviews").build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)
        /*val request = ReviewsRequest.Builder(Constants.PRODUCT_ID, 20, 0).build()
        bvClient!!.prepareCall(request).loadAsync(reviewsCb)*/
        return binding.root
    }


}
