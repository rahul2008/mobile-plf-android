package com.philips.cdp.di.mec.screens.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.databinding.MecReviewRowBinding
import com.philips.cdp.di.mec.screens.reviews.MECReview
import java.text.SimpleDateFormat

class MECReviewsAdapter(private val mecReviews: List<MECReview>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(MecReviewRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = mecReviews?.get(position)
        val viewHolder = holder as ViewHolder
        viewHolder.bind(review!!)
        if (position%2==0) {
            //viewHolder.binding.root.setBackgroundColor(R.attr.uidContentPrimaryBackgroundColor)
            viewHolder.binding.root.setBackgroundColor(getColor(viewHolder.binding.root.context, R.color.uidColorWhite))
        } else {
            //viewHolder.binding.root.setBackgroundColor(R.attr.uidContentSecondaryNeutralBackgroundColor)
            viewHolder.binding.root.setBackgroundColor(Color.parseColor("#F5F5F5"))
        }
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
