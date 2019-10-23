package com.philips.cdp.di.mec.screens.catalog

class MECProductCatalogCategorizedFragment : MECProductCatalogFragment() {



    override fun executeRequest(){
        var ctns = mutableListOf<String>()
        ecsProductViewModel.initCategorized(currentPage, pageSize,ctns)
    }
}

