package com.philips.cdp.di.mec.screens.detail


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bazaarvoice.bvandroidsdk.*
import com.google.gson.internal.LinkedTreeMap
import com.philips.cdp.di.ecs.model.products.ECSProduct

import com.philips.cdp.di.mec.databinding.MecProductReviewFragmentBinding
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.screens.reviews.MECReview
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import kotlinx.android.synthetic.main.mec_main_activity.*
import kotlinx.android.synthetic.main.mec_product_details.*

/**
 * A simple [Fragment] subclass.
 */
class MECProductReviewsFragment : MecBaseFragment() {


    private var productctn: String? = null

    private lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel
    private val reviewObserver : Observer<List<Review>> = object : Observer<List<Review>> {

        override fun onChanged(reviews: List<Review>?) {

                for (review in reviews!!) {
                    val nick = if (review.userNickname != null) review.userNickname else "Anonymous"

                    mecReviews.add(MECReview(review.title, review.reviewText, review.rating.toString(), nick, review.lastModificationDate, ecsProductDetailViewModel.getValueFor("Pros", review), ecsProductDetailViewModel.getValueFor("Cons", review), ecsProductDetailViewModel.getValueForUseDuration(review)))

            }

            if(mecReviews.size>0){
                binding.mecProductReviewEmptyLabel.visibility = View.GONE
            } else{
                binding.mecProductReviewEmptyLabel.visibility = View.VISIBLE
            }
            reviewsAdapter!!.notifyDataSetChanged()
            binding.mecProgressLayout.visibility = View.GONE
            //hideProgressBar()
        }

    }

    var offset: Int = 0
    var limit: Int = 20

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
        //productctn ="HD9653_90"

        //TODO in binding
        reviewsAdapter = MECReviewsAdapter(mecReviews)
        binding.recyclerView.adapter = reviewsAdapter
        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

            this!!.productctn?.let { ecsProductDetailViewModel.getBazaarVoiceReview(it, offset, limit) }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val lay = binding.recyclerView
                        .layoutManager as LinearLayoutManager

                if(isScrollDown(lay))
                    executeRequest()

            }
        })

        return binding.root
    }


    private fun executeRequest() {
        binding.mecProgressLayout.visibility = View.VISIBLE
        //createCustomProgressBar(container, MEDIUM, RelativeLayout.ALIGN_PARENT_BOTTOM)
        offset++
        this!!.productctn?.let { ecsProductDetailViewModel.getBazaarVoiceReview(it,offset,limit) }
    }

    private fun isScrollDown(lay: LinearLayoutManager): Boolean {
        val visibleItemCount = lay.childCount
        val firstVisibleItemPosition = lay.findFirstVisibleItemPosition()
        return visibleItemCount + firstVisibleItemPosition >= lay.itemCount && firstVisibleItemPosition >= 0
    }

}
