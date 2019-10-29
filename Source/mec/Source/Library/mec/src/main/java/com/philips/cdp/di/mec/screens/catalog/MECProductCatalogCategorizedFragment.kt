package com.philips.cdp.di.mec.screens.catalog

import com.philips.cdp.di.mec.utils.MECConstant

class MECProductCatalogCategorizedFragment : MECProductCatalogFragment() {

    override fun executeRequest(){
        val bundle = arguments
        var ctns = bundle!!.getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS)
        ecsProductViewModel.initCategorized(currentPage, pageSize,ctns)
    }

    override fun isPaginationSupported(): Boolean {
        return true
    }
}

