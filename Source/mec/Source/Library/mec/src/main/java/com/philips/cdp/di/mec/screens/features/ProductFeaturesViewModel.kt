/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.cdp.di.mec.screens.features

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.prxclient.datamodels.features.FeaturesModel

class ProductFeaturesViewModel : CommonViewModel() {

    val features =  MutableLiveData<FeaturesModel>()

    fun fetchProductFeatures(context: Context,ctn : String){
        ProductFeaturesRepository().fetchProductFeatures(context,ctn,this)
    }
}