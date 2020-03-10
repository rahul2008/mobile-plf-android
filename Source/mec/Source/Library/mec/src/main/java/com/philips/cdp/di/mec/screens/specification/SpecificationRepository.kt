package com.philips.cdp.di.mec.screens.specification

import android.content.Context
import com.philips.cdp.di.ecs.constants.NetworkConstants
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.prxclient.PRXDependencies
import com.philips.cdp.prxclient.PrxConstants
import com.philips.cdp.prxclient.RequestManager
import com.philips.cdp.prxclient.request.ProductSpecificationRequest

class SpecificationRepository {

    fun fetchSpecification(context: Context , ctn: String , prxSpecificationViewModel: SpecificationViewModel){
        // "SCF251/02"
        var productSpecificationRequest  = ProductSpecificationRequest(ctn,null)
        productSpecificationRequest.sector = PrxConstants.Sector.B2C;
        productSpecificationRequest.catalog = PrxConstants.Catalog.CONSUMER;
        productSpecificationRequest.requestTimeOut = NetworkConstants.DEFAULT_TIMEOUT_MS;

        val mRequestManager = RequestManager()
        val prxDependencies = PRXDependencies(context,MECDataHolder.INSTANCE.appinfra, MECConstant.COMPONENT_NAME)
        mRequestManager.init(prxDependencies)
        mRequestManager.executeRequest(productSpecificationRequest,PRXSpecificationResponseCallback(prxSpecificationViewModel))
    }
}