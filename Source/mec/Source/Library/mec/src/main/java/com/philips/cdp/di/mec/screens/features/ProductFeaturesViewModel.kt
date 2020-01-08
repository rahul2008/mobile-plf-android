package com.philips.cdp.di.mec.screens.features

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.prxclient.datamodels.features.FeaturesModel

class ProductFeaturesViewModel : CommonViewModel() {

    val features =  MutableLiveData<FeaturesModel>()

    fun fetchProductFeatures(context: Context,ctn : String){
        ProductFeaturesRepository().fetchProductFeatures(context,ctn,this)
    }
}