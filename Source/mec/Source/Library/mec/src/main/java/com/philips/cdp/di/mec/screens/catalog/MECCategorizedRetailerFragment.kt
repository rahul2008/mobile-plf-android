package com.philips.cdp.di.mec.screens.catalog
import com.philips.cdp.di.mec.utils.MECConstant

class MECCategorizedRetailerFragment : MECProductCatalogFragment(){

    override fun isPaginationSupported(): Boolean {
        return false
    }


    override fun executeRequest(){
        val bundle = arguments
        var ctns = bundle!!.getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS)
        ecsProductViewModel.initCategorizedRetailer(ctns)
    }

    override fun isCategorizedHybrisPagination(): Boolean {
        return false
    }

}
