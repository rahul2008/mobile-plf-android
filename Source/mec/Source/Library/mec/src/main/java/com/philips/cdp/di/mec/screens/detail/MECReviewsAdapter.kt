package com.philips.cdp.di.mec.screens.detail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import com.bazaarvoice.bvandroidsdk.Review
import com.philips.cdp.di.mec.R
import java.util.*

class MECReviewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val reviews = ArrayList<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val reviewRow = LayoutInflater.from(parent.context).inflate(R.layout.mec_review_row, parent, false)
        return ViewHolder(reviewRow)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = reviews[position]
        val viewHolder = holder as ViewHolder
        viewHolder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun updateData(results: List<Review>?) {
        if (results != null) {
            this.reviews.clear()
            this.reviews.addAll(results)
            notifyDataSetChanged()
        }
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.mec_retailerItem_productName_lebel)
        private val reviewBody: TextView = itemView.findViewById(R.id.mec_review_description)
        private val rating: TextView = itemView.findViewById(R.id.mec_rating_lebel)
        private val authorNick: TextView = itemView.findViewById(R.id.mec_retailerItem_review_submitter)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.mec_rating)
//        private val submissionDate: TextView = itemView.findViewById(R.id.submissionDate)
//        private val pros: TextView = itemView.findViewById(R.id.pros)
//        private val cons: TextView = itemView.findViewById(R.id.cons)

        fun bind(review: Review) {
            review.badgeList
            title.text = String.format("%s", review.title)
            reviewBody.text = String.format("%s", review.reviewText)
            rating.text = String.format(Locale.US, "%d", review.rating)

            authorNick.text = String.format("%s", review.userNickname + " - " +review.lastModificationDate)
            ratingBar.setRating((review.rating).toFloat())
//            submissionDate.text = String.format("Submission Date: %s", review.submissionDate)
//            pros.text = String.format("Pros: %s", review.additionalFields.get("Pros"))
//            cons.text = String.format("Cons: %s", review.additionalFields.get("Cons"))
        }
    }
}
