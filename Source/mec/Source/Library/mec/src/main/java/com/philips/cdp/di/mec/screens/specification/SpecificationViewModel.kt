/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.cdp.di.mec.screens.specification

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.prxclient.datamodels.specification.SpecificationModel

class SpecificationViewModel : CommonViewModel() {

    val specification = MutableLiveData<SpecificationModel>()


    fun fetchSpecification(context: Context,ctn :String){
        SpecificationRepository().fetchSpecification(context,ctn,this)
    }

}