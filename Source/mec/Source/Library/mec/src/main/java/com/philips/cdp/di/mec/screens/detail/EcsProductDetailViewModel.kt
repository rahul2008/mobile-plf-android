package com.philips.cdp.di.mec.screens.detail

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.ErrorViewModel

class EcsProductDetailViewModel : ErrorViewModel() {

    val ecsProduct = MutableLiveData<ECSProduct>()

    fun getProductDetail(ecsProduct: ECSProduct){
        ECSProductDetailRepository().getProductDetail(ecsProduct,this)
    }

}