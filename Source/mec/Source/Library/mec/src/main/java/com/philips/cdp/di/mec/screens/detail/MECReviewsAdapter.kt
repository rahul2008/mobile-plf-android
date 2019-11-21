package com.philips.cdp.di.mec.screens.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
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
        if(review.pros==null || review.pros == "null"){
            viewHolder.binding.mecReviewPros.visibility = View.GONE
        } else {
            viewHolder.binding.mecReviewPros.visibility = View.VISIBLE
        }
        if(review.cons==null || review.cons == "null"){
            viewHolder.binding.mecReviewCons.visibility = View.GONE
        } else {
            viewHolder.binding.mecReviewCons.visibility = View.VISIBLE
        }
        if (position % 2 == 1) {
            //viewHolder.binding.root.setBackgroundColor(R.attr.uidContentPrimaryBackgroundColor)
            viewHolder.binding.root.setBackgroundColor(getColor(viewHolder.binding.root.context, R.color.uidColorWhite))
        } else {
            //viewHolder.binding.root.setBackgroundColor(R.attr.uidContentSecondaryNeutralBackgroundColor)
            viewHolder.binding.root.setBackgroundColor(Color.parseColor("#F5F5F5"))
        }
        //android:text='@{ (mecReview.submitter+" - "+mecReview.formattedDate): (mecReview.submitter+" - "+mecReview.formattedDate + "@string/mec_has_used_this_product_for"+mecReview.useDuration) }'
      var durationUse :String =review.submitter+" - "+review.getFormattedDate();
       if(review.useDuration.toString()!=null && !review.useDuration.toString().isEmpty()){
           durationUse = durationUse + " - "+viewHolder.binding.mecRetailerItemReviewSubmitter.context.getString(R.string.mec_has_used_this_product_for)+" "+review.useDuration.toString();
       }
        viewHolder.binding.mecRetailerItemReviewSubmitter.text=durationUse;
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
