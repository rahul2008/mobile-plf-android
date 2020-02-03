package com.philips.cdp.di.mec.screens.shoppingCart

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import com.philips.cdp.di.ecs.model.cart.ECSEntries
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.platform.uid.view.widget.Label

open class EcsShoppingCartViewModel : CommonViewModel() {

    var ecsShoppingCart = MutableLiveData<ECSShoppingCart>()

    val ecsProductsReviewList = MutableLiveData<MutableList<MECCartProductReview>>()

    var ecsServices = MecHolder.INSTANCE.eCSServices

    private var ecsShoppingCartRepository = ECSShoppingCartRepository(this,ecsServices)

    fun getShoppingCart(){
        this.ecsShoppingCartRepository.fetchShoppingCart()
    }

    fun updateQuantity(entries: ECSEntries, quantity: Int) {
        this.ecsShoppingCartRepository.updateShoppingCart(entries,quantity)
    }

    fun fetchProductReview(entries: MutableList<ECSEntries>) {
        this.ecsShoppingCartRepository.fetchProductReview(entries, this)
    }


    companion object {
        @JvmStatic
        @BindingAdapter("setPrice")
        fun setPrice(priceLabel: Label, product: ECSProduct) {
            val textSize16 = priceLabel.context.getResources().getDimensionPixelSize(R.dimen.mec_product_detail_discount_price_label_size);
            val textSize12 = priceLabel.context.getResources().getDimensionPixelSize(R.dimen.mec_product_detail_price_label_size);
            if (product!=null && product.discountPrice!=null && product.discountPrice.formattedValue != null && product.discountPrice.formattedValue.length > 0 && (product.price.value - product.discountPrice.value) > 0) {
                val price = SpannableString(product.price.formattedValue);
                price.setSpan(AbsoluteSizeSpan(textSize12), 0, product.price.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                price.setSpan(StrikethroughSpan(), 0, product.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                price.setSpan(ForegroundColorSpan(R.attr.uidContentItemTertiaryNormalTextColor), 0, product.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                val discountPrice = SpannableString(product.discountPrice.formattedValue)
                discountPrice.setSpan(AbsoluteSizeSpan(textSize16), 0, product.discountPrice.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                val CharSequence = TextUtils.concat(price, "  ", discountPrice)
                priceLabel.text = CharSequence;
            } else {
                if(product.price!=null)
                    priceLabel.text = product.price.formattedValue;
            }
    }
    }

}