package com.philips.cdp.di.mec.screens.detail

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import com.bazaarvoice.bvandroidsdk.Review
import com.google.gson.internal.LinkedTreeMap
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ErrorViewModel
import com.philips.cdp.di.mec.screens.reviews.MECReview
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uid.view.widget.Label

class EcsProductDetailViewModel : ErrorViewModel() {

    val ecsProduct = MutableLiveData<ECSProduct>()

    val review = MutableLiveData<List<Review>>()

    fun getProductDetail(ecsProduct: ECSProduct){
        ECSProductDetailRepository().getProductDetail(ecsProduct,this)
    }

    fun getBazaarVoiceReview(ctn : String, pageNumber : Int, pageSize : Int){
        ECSProductDetailRepository().fetchProductReview(ctn,pageNumber,pageSize,this)
    }
    companion object DataBindingAdapter {

        @JvmStatic
        @BindingAdapter("setDisclaimer")
        fun setDisclaimer(label: Label, ecsProduct: ECSProduct?) {

            val disclaimerStringBuilder = StringBuilder()

            if (ecsProduct?.disclaimers != null) {

                for (disclaimer in ecsProduct.disclaimers?.disclaimer!!) {
                    disclaimer.disclaimerText
                    disclaimerStringBuilder.append("- ").append(disclaimer.disclaimerText).append(System.getProperty("line.separator"))
                }
                label.text = disclaimerStringBuilder.toString()
            }
        }

        @JvmStatic
        @BindingAdapter("setStockInfo")
        fun setStockInfo(stockLabel : Label, product: ECSProduct) {
            if(null!=product!!.stock ) {
                if (MECutility.isStockAvailable(product!!.stock!!.stockLevelStatus, product!!.stock!!.stockLevel)) {
                    stockLabel.text = stockLabel.context.getString(R.string.mec_in_stock)
                    stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_green_level_30))
                    // stockLabel.setTextColor(R.attr.uidContentItemSignalNormalTextSuccessColor)

                } else {
                    stockLabel.text = stockLabel.context.getString(R.string.mec_out_of_stock)
                    stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_red_level_30))
                    // stockLabel.setTextColor(R.attr.uidContentItemSignalNormalTextErrorColor)
                }
            }
        }
    }

    @BindingAdapter("review")
    fun setAdapter(recyclerView: RecyclerView, mecReviews: MutableList<MECReview>) {
        recyclerView.adapter = MECReviewsAdapter(mecReviews)
    }


    fun getValueFor(type: String, review: Review): String {
        var  reviewValue :String? = null
        var mapAdditionalFields: LinkedTreeMap<String, String>? = null
        if (review.additionalFields != null && review.additionalFields.size > 0) {
            mapAdditionalFields = review.additionalFields.get(type) as LinkedTreeMap<String, String>
            reviewValue= if (mapAdditionalFields != null && mapAdditionalFields?.get("Value") != null) mapAdditionalFields?.get("Value") else ""
        }
        if (reviewValue == null) {
            if (review.tagDimensions != null && review.tagDimensions!!.size > 0) {
                val tagD = review.tagDimensions?.get(type.substring(0,type.length-1))
                reviewValue= tagD?.values.toString()
            }
        }
        return if(reviewValue.toString()!=null) reviewValue.toString() else ""
    }

}