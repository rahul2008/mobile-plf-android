package com.philips.cdp.di.mec.screens.detail

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse
import com.bazaarvoice.bvandroidsdk.ContextDataValue
import com.bazaarvoice.bvandroidsdk.Review
import com.bazaarvoice.bvandroidsdk.ReviewResponse
import com.google.gson.internal.LinkedTreeMap
import com.philips.cdp.di.ecs.model.asset.Asset
import com.philips.cdp.di.ecs.model.asset.Assets
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.retailers.ECSRetailer
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.screens.detail.MECProductDetailsFragment.Companion.tagOutOfStockActions
import com.philips.cdp.di.mec.screens.reviews.MECReview
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uid.view.widget.Label
import java.util.*

class EcsProductDetailViewModel : CommonViewModel() {

    var ecsProduct = MutableLiveData<ECSProduct>()

    val bulkRatingResponse= MutableLiveData<BulkRatingsResponse>()

    val review = MutableLiveData<ReviewResponse>()

    var ecsServices = MecHolder.INSTANCE.eCSServices

    var ecsProductDetailRepository = ECSProductDetailRepository(this,ecsServices)

    fun getRatings(ctn :String){
        ecsProductDetailRepository.getRatings(ctn)
    }

    fun getProductDetail(ecsProduct: ECSProduct){
        ecsProductDetailRepository.getProductDetail(ecsProduct)
    }

    fun getBazaarVoiceReview(ctn : String, pageNumber : Int, pageSize : Int){
        ecsProductDetailRepository.fetchProductReview(ctn, pageNumber, pageSize)
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
        fun setStockInfo(stockLabel : Label, product: ECSProduct?) {
            if(null!=product && null!= product.stock) {
                if (MECutility.isStockAvailable(product.stock!!.stockLevelStatus, product.stock!!.stockLevel)) {
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
        if (review.additionalFields != null && review.additionalFields.get(type)!=null && review.additionalFields.size > 0 ) {
            mapAdditionalFields = review.additionalFields.get(type) as LinkedTreeMap<String, String>
            reviewValue= if (mapAdditionalFields.get("Value") != null) mapAdditionalFields?.get("Value") else ""
        }
        if (reviewValue == null) {
            if (review.tagDimensions != null && review.tagDimensions!!.size > 0) {
                val tagD = review.tagDimensions?.get(type.substring(0,type.length-1))
                reviewValue= tagD?.values.toString()
            }
        }
        return reviewValue.toString()
    }


     fun getValueForUseDuration( review: Review): String {
        var useDurationValue: String? = ""
        if (review.contextDataValues != null && review.contextDataValues!!.size>0 && (null!=review.contextDataValues!!.get("HowLongHaveYouBeenUsingThisProduct"))) {
            val mapUseDuration: ContextDataValue = review.contextDataValues!!.get("HowLongHaveYouBeenUsingThisProduct") as ContextDataValue
            useDurationValue=mapUseDuration.valueLabel
        }
        return useDurationValue.toString()
    }

    fun removeBlacklistedRetailers(ecsRetailers: ECSRetailerList): ECSRetailerList {
        val list = MECDataHolder.INSTANCE.blackListedRetailers
        if(list == null){
            return ecsRetailers
        }

        for (name in list!!) {

            val iterator = ecsRetailers.retailers.iterator()

            while (iterator.hasNext()) {

                val retailerName = iterator.next().getName().replace("\\s+".toRegex(), "")
                if (name.equals(retailerName, true)) {

                    if (MECutility.indexOfSubString(true, retailerName, name) >= 0) {
                        iterator.remove()

                    }
                }
            }

        }

        return ecsRetailers
    }

    fun uuidWithSupplierLink(buyURL: String ,param :String): String {

        val propositionId = MECDataHolder.INSTANCE.propositionId

        val supplierLinkWithUUID = "$buyURL&wtbSource=mobile_$propositionId&$param="

        return supplierLinkWithUUID + UUID.randomUUID().toString()
    }

    fun isPhilipsShop(retailer: ECSRetailer): Boolean {
        return retailer.isPhilipsStore.equals("Y", ignoreCase = true)
    }

    fun setStockInfoWithRetailer(stockLabel : Label, product: ECSProduct? ,ecsRetailers: ECSRetailerList) {
            if(!MECDataHolder.INSTANCE.hybrisEnabled) {
                if (ecsRetailers.retailers.size>0) {
                    var availability=false
                    for (i in 0..ecsRetailers.retailers.size) {
                        if(ecsRetailers.retailers.get(i).availability.contains("YES")){
                            availability=true
                            break
                        }
                    }
                    if (availability) {
                        stockLabel.text = stockLabel.context.getString(R.string.mec_in_stock)
                        stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_green_level_30))
                    } else {
                        stockLabel.text = stockLabel.context.getString(R.string.mec_out_of_stock)
                        stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_red_level_30))
                        product?.let { tagOutOfStockActions(it) }
                    }

                } else if (ecsRetailers.retailers.size==0) {
                    stockLabel.text = stockLabel.context.getString(R.string.mec_out_of_stock)
                    stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_red_level_30))
                    product?.let { tagOutOfStockActions(it) }
                }
            }
            else if(MECDataHolder.INSTANCE.hybrisEnabled){
                if(ecsRetailers.retailers.size==0) {
                    if (null != product && null != product.stock) {
                        if (MECutility.isStockAvailable(product.stock!!.stockLevelStatus, product.stock!!.stockLevel)) {
                            stockLabel.text = stockLabel.context.getString(R.string.mec_in_stock)
                            stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_green_level_30))
                            // stockLabel.setTextColor(R.attr.uidContentItemSignalNormalTextSuccessColor)
                        } else {
                            stockLabel.text = stockLabel.context.getString(R.string.mec_out_of_stock)
                            stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_red_level_30))
                            // stockLabel.setTextColor(R.attr.uidContentItemSignalNormalTextErrorColor)
                            tagOutOfStockActions(product)
                        }
                    }
                }

             else if (ecsRetailers.retailers.size>0) {
                    var availability=false
                    for (i in 0..ecsRetailers.retailers.size) {
                        if(ecsRetailers.retailers.get(i).availability.contains("YES")){
                            availability=true
                            break
                        } else if(ecsRetailers.retailers.get(i).availability.contains("NO")) {
                            availability=false
                            if (!availability) {
                                if (null != product && null != product.stock) {
                                    if (MECutility.isStockAvailable(product.stock!!.stockLevelStatus, product.stock!!.stockLevel)) {
                                        stockLabel.text = stockLabel.context.getString(R.string.mec_in_stock)
                                        stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_green_level_30))
                                    } else {
                                        stockLabel.text = stockLabel.context.getString(R.string.mec_out_of_stock)
                                        stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_red_level_30))
                                        product?.let { tagOutOfStockActions(it) }
                                    }
                                }
                            }
                        }
                    }
                    if (availability) {
                        stockLabel.text = stockLabel.context.getString(R.string.mec_in_stock)
                        stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_green_level_30))
                    } else {
                        stockLabel.text = stockLabel.context.getString(R.string.mec_out_of_stock)
                        stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_red_level_30))
                        product?.let { tagOutOfStockActions(it) }
                }
            }  else  {
                stockLabel.text = stockLabel.context.getString(R.string.mec_out_of_stock)
                stockLabel.setTextColor(stockLabel.context.getColor(R.color.uid_signal_red_level_30))
                    product?.let { tagOutOfStockActions(it) }
            }
        }
    }

    fun addNoAsset(product: ECSProduct) {
        var asset = Asset()
        asset.asset = "xyz"
        asset.type = "UNKNOWN"

        var assets = Assets()
        assets.asset = Arrays.asList(asset)
        product.assets = assets

    }

}