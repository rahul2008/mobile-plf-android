package com.philips.cdp.di.mec.screens.specification

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.philips.cdp.di.mec.common.CommonViewModel
import com.philips.cdp.prxclient.datamodels.specification.CsItemItem
import com.philips.cdp.prxclient.datamodels.specification.CsValueItem
import com.philips.cdp.prxclient.datamodels.specification.SpecificationModel
import com.philips.platform.uid.view.widget.Label

class SpecificationViewModel : CommonViewModel() {

    val specification = MutableLiveData<SpecificationModel>()


    fun fetchSpecification(context: Context,ctn :String){
        SpecificationRepository().fetchSpecification(context,ctn,this)
    }

}