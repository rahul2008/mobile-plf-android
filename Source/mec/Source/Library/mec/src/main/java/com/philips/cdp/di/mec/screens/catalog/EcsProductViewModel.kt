package com.philips.cdp.di.mec.screens.catalog

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.products.ECSProducts
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.platform.uid.view.widget.Label

class EcsProductViewModel : CommonViewModel() {

    var ecsProductsList = MutableLiveData<MutableList<ECSProducts>>()

    val ecsProductsReviewList = MutableLiveData<MutableList<MECProductReview>>()

    val ecsServices = MecHolder.INSTANCE.eCSServices

    var ecsCatalogRepository = ECSCatalogRepository()

    var ecsProductsCallback = ECSProductsCallback(this)

    var ecsProductListCallback = ECSProductListCallback(this)


    fun init(pageNumber: Int, pageSize: Int) {
        ecsCatalogRepository.getProducts(pageNumber, pageSize,ecsProductsCallback,ecsServices)
    }

    fun initCategorizedRetailer(ctn: MutableList<String>) {
        ecsCatalogRepository.getCategorizedProductsForRetailer(ctn,ecsProductListCallback ,ecsServices)
    }


    fun initCategorized(pageNumber: Int, pageSize: Int, ctns: List<String>) {
        ecsCatalogRepository.getCategorizedProducts(pageNumber, pageSize, ctns, this)
    }

    fun fetchProductReview(products: List<ECSProduct>) {
        ecsCatalogRepository.fetchProductReview(products, this)
    }


    companion object DataBindingAdapter {


        @JvmStatic
        @BindingAdapter("setPriceInfo")
        fun setPriceInfo(priceLabel: Label, product: ECSProduct) {
            val textSize16 = priceLabel.context.getResources().getDimensionPixelSize(com.philips.cdp.di.mec.R.dimen.mec_product_detail_discount_price_label_size);
            val textSize12 = priceLabel.context.getResources().getDimensionPixelSize(com.philips.cdp.di.mec.R.dimen.mec_product_detail_price_label_size);
            if (product.discountPrice.formattedValue != null && product.discountPrice.formattedValue.length > 0 && (product.price.value - product.discountPrice.value) > 0) {
                val price = SpannableString(product.price.formattedValue);
                price.setSpan(AbsoluteSizeSpan(textSize12), 0, product.price.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                price.setSpan(StrikethroughSpan(), 0, product.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                price.setSpan(ForegroundColorSpan(R.attr.uidContentItemTertiaryNormalTextColor), 0, product.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                val discountPrice = SpannableString(product.discountPrice.formattedValue);
                discountPrice.setSpan(AbsoluteSizeSpan(textSize16), 0, product.discountPrice.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                val CharSequence = TextUtils.concat(price, "  ", discountPrice);
                priceLabel.text = CharSequence;
            } else {
                priceLabel.text = product.price.formattedValue;
            }
        }
    }

}