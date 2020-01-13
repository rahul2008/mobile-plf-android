package com.philips.cdp.di.mec.screens.catalog

import com.google.gson.Gson
import com.philips.cdp.di.mec.integration.MECFlowConfigurator
import com.philips.cdp.di.mec.utils.MECConstant

class MECProductCatalogCategorizedFragment : MECProductCatalogFragment() {

    override fun executeRequest(){
        val bundle = arguments
        val str = bundle?.getString(MECConstant.FLOW_INPUT)
        val isCategorized = Gson().fromJson(str, MECFlowConfigurator::class.java)
        ecsProductViewModel.initCategorized(currentPage, pageSize, isCategorized.productCTNs!!)
    }

    override fun isPaginationSupported(): Boolean {
        return true
    }
}

