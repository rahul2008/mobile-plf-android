package com.philips.cdp.di.mec.screens.shoppingCart


import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.ecs.model.cart.BasePriceEntity
import com.philips.cdp.di.ecs.model.cart.ECSEntries
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart
import com.philips.cdp.di.ecs.model.cart.TotalPriceEntity
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.cdp.di.mec.screens.address.ECSFetchAddressesCallback
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uid.view.widget.Label

open class EcsShoppingCartViewModel : CommonViewModel() {

    var ecsShoppingCart = MutableLiveData<ECSShoppingCart>()

    var ecsVoucher = MutableLiveData<List<ECSVoucher>>()

    val ecsProductsReviewList = MutableLiveData<MutableList<MECCartProductReview>>()



    var ecsServices = MecHolder.INSTANCE.eCSServices

     lateinit  var updateQuantityEntries :ECSEntries
     var updateQuantityNumber:Int = 0
     lateinit  var addVoucherString :String
     lateinit var deleteVoucherString :String

    private var ecsShoppingCartRepository = ECSShoppingCartRepository(this,ecsServices)



    var ecsVoucherCallback = ECSVoucherCallback(this)

    fun getShoppingCart(){
        ecsShoppingCartRepository.fetchShoppingCart()
    }





    fun createShoppingCart(request: String){
        val createShoppingCartCallback=  object: ECSCallback<ECSShoppingCart, Exception> {
            override fun onResponse(result: ECSShoppingCart?) {
                getShoppingCart()
            }
            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mECError = MecError(error, ecsError,null)
                mecError.value = mECError
            }
        }
        ecsShoppingCartRepository.createCart(createShoppingCartCallback)
    }

    fun updateQuantity(entries: ECSEntries, quantity: Int) {
        updateQuantityEntries=entries
        updateQuantityNumber=quantity
        ecsShoppingCartRepository.updateShoppingCart(entries,quantity)
    }

    fun fetchProductReview(entries: MutableList<ECSEntries>) {
        ecsShoppingCartRepository.fetchProductReview(entries, this)
    }

    fun addVoucher(voucherCode : String, mECRequestType :MECRequestType){
        ecsVoucherCallback.mECRequestType = mECRequestType
        addVoucherString=voucherCode
        ecsShoppingCartRepository.applyVoucher(voucherCode,ecsVoucherCallback)
    }

    fun removeVoucher(voucherCode : String){
        ecsVoucherCallback.mECRequestType = MECRequestType.MEC_REMOVE_VOUCHER
        deleteVoucherString=voucherCode
        ecsShoppingCartRepository.removeVoucher(voucherCode,ecsVoucherCallback)
    }

    fun selectAPIcall(mecRequestType: MECRequestType):() -> Unit{

        lateinit  var APIcall: () -> Unit
        when(mecRequestType) {
            MECRequestType.MEC_FETCH_SHOPPING_CART  -> APIcall = { getShoppingCart() }
            MECRequestType.MEC_UPDATE_SHOPPING_CART -> APIcall = { updateQuantity(updateQuantityEntries,updateQuantityNumber) }
            MECRequestType.MEC_APPLY_VOUCHER        -> APIcall = { addVoucher(addVoucherString,ecsVoucherCallback.mECRequestType) }
            MECRequestType.MEC_REMOVE_VOUCHER       -> APIcall = { removeVoucher(deleteVoucherString) }

        }
        return APIcall
    }

    fun retryAPI(mecRequestType: MECRequestType) {
        var retryAPI = selectAPIcall(mecRequestType)
        authAndCallAPIagain(retryAPI,authFailCallback)
    }





    companion object {
        @JvmStatic
        @BindingAdapter("setPrice", "totalPriceEntity")
        fun setPrice(priceLabel: Label, product: ECSProduct?, basePriceEntity: BasePriceEntity?) {
            val textSize16 = priceLabel.context.getResources().getDimensionPixelSize(R.dimen.mec_product_detail_discount_price_label_size)
            val textSize12 = priceLabel.context.getResources().getDimensionPixelSize(R.dimen.mec_product_detail_price_label_size);
            if (product != null && basePriceEntity!!.formattedValue != null && basePriceEntity?.formattedValue!!.length > 0 && (product.price.value - basePriceEntity.value) > 0) {
                val price = SpannableString(product.price.formattedValue);
                price.setSpan(AbsoluteSizeSpan(textSize12), 0, product.price.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                price.setSpan(StrikethroughSpan(), 0, product.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                price.setSpan(ForegroundColorSpan(R.attr.uidContentItemTertiaryNormalTextColor), 0, product.price.formattedValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                val discountPrice = SpannableString(basePriceEntity.formattedValue)
                discountPrice.setSpan(AbsoluteSizeSpan(textSize16), 0, basePriceEntity.formattedValue.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                val CharSequence = TextUtils.concat(price, "  ", discountPrice)
                priceLabel.text = CharSequence;
            } else {
                if (product!!.price != null)
                    priceLabel.text = product.price.formattedValue
            }
        }

        @JvmStatic
        @BindingAdapter("setDiscountPrice", "totalPriceEntity")
        fun setDiscountPrice(discountPriceLabel: Label, product: ECSProduct?, basePriceEntity: BasePriceEntity?) {
            val discount = (product!!.price!!.value - basePriceEntity!!.value) / product.price!!.value * 100

            if(discount!=null) {
                val discountRounded: String = String.format("%.2f", discount).toString()
                discountPriceLabel.text = "-" + discountRounded + "%"
                if (discountRounded.equals("0.00")) {
                    discountPriceLabel.visibility = View.GONE
                } else {
                    discountPriceLabel.visibility = View.VISIBLE
                }
            }
        }

        @JvmStatic
        @BindingAdapter("setStock","setQuantity")
        fun setStock(stockLabel : Label , product: ECSProduct?, quantity: Int) {
            if (null != product && null != product.stock) {
                if ((!MECutility.isStockAvailable(product.stock!!.stockLevelStatus, product.stock!!.stockLevel)) || (product.stock.stockLevel==0)) {
                    stockLabel.text = stockLabel.context.getString(R.string.mec_out_of_stock)
                }
                if(product.stock.stockLevel<=5 && product.stock.stockLevel!=0){
                    stockLabel.text = "Only " + product.stock.stockLevel + " items left"
                }
                if(quantity>product.stock!!.stockLevel && product.stock.stockLevel!=0) {
                    stockLabel.text = "Only " + product.stock.stockLevel + " items left"
                }
                stockLabel.visibility = View.VISIBLE
            }
        }
    }

}