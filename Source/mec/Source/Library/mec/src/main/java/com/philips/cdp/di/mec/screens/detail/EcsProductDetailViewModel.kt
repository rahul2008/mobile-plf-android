package com.philips.cdp.di.mec.screens.detail

import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.widget.RatingBar
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ErrorViewModel
import com.philips.cdp.prxclient.datamodels.Disclaimer.Disclaimer
import com.philips.platform.uid.view.widget.Label

class EcsProductDetailViewModel : ErrorViewModel() {

    val ecsProduct = MutableLiveData<ECSProduct>()

    fun getProductDetail(ecsProduct: ECSProduct){
        ECSProductDetailRepository().getProductDetail(ecsProduct,this)
    }

    companion object DataBindingAdapter {

        @JvmStatic
        @BindingAdapter("setDisclaimer")
        fun setDisclaimer(label: Label, disclaimers: Disclaimers) {

            val disclaimerStringBuilder = StringBuilder()

            for( disclaimer in disclaimers?.disclaimer){
                 disclaimer.disclaimerText
                 disclaimerStringBuilder.append("- ").append(disclaimer.disclaimerText).append(System.getProperty("line.separator"))
            }
            label.text = disclaimerStringBuilder.toString()
        }
    }

}