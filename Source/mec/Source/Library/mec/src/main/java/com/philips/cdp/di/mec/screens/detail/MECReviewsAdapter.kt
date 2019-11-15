package com.philips.cdp.di.mec.screens.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.databinding.MecReviewRowBinding
import com.philips.cdp.di.mec.screens.reviews.MECReview

class MECReviewsAdapter(private val mecReviews: List<MECReview>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(MecReviewRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = mecReviews?.get(position)
        val viewHolder = holder as ViewHolder
        viewHolder.bind(review!!)
    }

    override fun getItemCount(): Int {
        return mecReviews?.size!!
    }

    private class ViewHolder(val binding: MecReviewRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: MECReview) {
            binding.mecReview = review
        }
    }
}
