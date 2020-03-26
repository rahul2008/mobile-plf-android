package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.model.address.ECSAddress
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.utils.MECDataHolder

class AddressService{


    fun setEnglishSalutation(ecsAddress: ECSAddress){

        val localizedSalutation = ecsAddress.title
        val context = MECDataHolder.INSTANCE.appinfra.appInfraContext

        if (localizedSalutation.equals(context.getString(R.string.mec_mr), ignoreCase = true)) {
            ecsAddress.title = SalutationEnum.MR.englishSalutation
        }else if (localizedSalutation.equals(context.getString(R.string.mec_mrs), ignoreCase = true)){
            ecsAddress.title = SalutationEnum.MS.englishSalutation
        }
    }
}