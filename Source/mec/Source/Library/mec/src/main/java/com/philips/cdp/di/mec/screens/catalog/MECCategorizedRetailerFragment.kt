package com.philips.cdp.di.mec.screens.catalog

class MECCategorizedRetailerFragment : MECProductCatalogFragment(),Pagination {

    override fun addPagination(isHybris: Boolean?) {
        addPagination(false)
    }

    override fun executeRequest(){
        var ctns = mutableListOf<String>()
        ecsProductViewModel.initCategorizedRetailer(ctns)
    }
}
