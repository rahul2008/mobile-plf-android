package com.philips.cdp.di.mec.screens.detail


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.analytics.MECAnalytics
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant

import com.philips.cdp.di.mec.databinding.MecProductReviewFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.reviews.MECReview
import com.philips.cdp.di.mec.utils.MECConstant


/**
 * A simple [Fragment] subclass.
 */
class MECProductReviewsFragment : MecBaseFragment() {


    private var productctn: String? = null
    var offset: Int = 0
    var limit: Int = 20
    var totalReview: Int = 0

    private lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel
    private val reviewObserver : Observer<ReviewResponse> = object : Observer<ReviewResponse> {

        override fun onChanged(reviewResponse:  ReviewResponse?) {

            val reviews = reviewResponse?.results

            totalReview = reviewResponse?.totalResults ?: 0
                for (review in reviews!!) {
                    val nick = if (review.userNickname != null) review.userNickname else getString(R.string.mec_anonymous)

                    mecReviews.add(MECReview(review.title, review.reviewText, review.rating.toString(), nick, review.lastModificationDate, ecsProductDetailViewModel.getValueFor("Pros", review), ecsProductDetailViewModel.getValueFor("Cons", review), ecsProductDetailViewModel.getValueForUseDuration(review)))

            }

            if(mecReviews.size>0){
                binding.mecProductReviewEmptyLabel.visibility = View.GONE
                binding.mecBvTrustmark.visibility = View.VISIBLE
            } else{
                binding.mecProductReviewEmptyLabel.visibility = View.VISIBLE
                binding.mecBvTrustmark.visibility = View.GONE
            }
            reviewsAdapter!!.notifyDataSetChanged()
            binding.mecProgressLayout.visibility = View.GONE
        }

    }

    private var reviewsAdapter: MECReviewsAdapter? = null
    private lateinit var mecReviews: MutableList<MECReview>

    private lateinit var binding: MecProductReviewFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = MecProductReviewFragmentBinding.inflate(inflater, container, false)

        ecsProductDetailViewModel = this.let { ViewModelProviders.of(it).get(EcsProductDetailViewModel::class.java) }

        ecsProductDetailViewModel.review.observe(this, reviewObserver)
        ecsProductDetailViewModel.mecError.observe(this,this)

        mecReviews = mutableListOf<MECReview>()

        val bundle = arguments
        productctn = bundle!!.getString(MECConstant.MEC_PRODUCT_CTN,"INVALID")

        //TODO in binding
        reviewsAdapter = MECReviewsAdapter(mecReviews)
        binding.recyclerView.adapter = reviewsAdapter
        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

            this!!.productctn?.let { ecsProductDetailViewModel.getBazaarVoiceReview(it, offset, limit) }

        binding.mecNestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                val lay = binding.recyclerView
                        .layoutManager as LinearLayoutManager

                if(isScrollDown(lay))
                    if (isAllFetched()) {
                        executeRequest()
                    }
            }
        })

        return binding.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            productctn?.let { tagActions(it) }
        }
    }

    private fun tagActions(ctn : String) {
        var map = HashMap<String, String>()
        map.put(MECAnalyticsConstant.specialEvents, MECAnalyticsConstant.userReviewsViewed)
        map.put(MECAnalyticsConstant.mecProducts,ctn)
        MECAnalytics.trackMultipleActions(MECAnalyticsConstant.sendData,map)
    }

    private fun isAllFetched() = totalReview != 0 && reviewsAdapter!!.itemCount < totalReview


    private fun executeRequest() {
        binding.mecProgressLayout.visibility = View.VISIBLE
        offset += limit
        this!!.productctn?.let { ecsProductDetailViewModel.getBazaarVoiceReview(it,offset,limit) }
    }

    private fun isScrollDown(lay: LinearLayoutManager): Boolean {
        val visibleItemCount = lay.childCount
        val firstVisibleItemPosition = lay.findFirstVisibleItemPosition()
        return visibleItemCount + firstVisibleItemPosition >= lay.itemCount && firstVisibleItemPosition >= 0
    }

}
