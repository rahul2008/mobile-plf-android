package com.philips.cdp.di.mec.screens.detail

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.ErrorViewModel
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uid.view.widget.Label

class EcsProductDetailViewModel : ErrorViewModel() {

    val ecsProduct = MutableLiveData<ECSProduct>()

    fun getProductDetail(ecsProduct: ECSProduct){
        ECSProductDetailRepository().getProductDetail(ecsProduct,this)
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

}