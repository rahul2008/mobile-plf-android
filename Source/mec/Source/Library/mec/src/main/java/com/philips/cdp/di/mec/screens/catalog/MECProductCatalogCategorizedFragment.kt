package com.philips.cdp.di.mec.screens.catalog

import com.philips.cdp.di.mec.utils.MECConstant
import kotlinx.android.synthetic.main.mec_main_activity.*

class MECProductCatalogCategorizedFragment : MECProductCatalogFragment() {

    override fun executeRequest(){
        createCustomProgressBar(container, MEDIUM)
        val bundle = arguments
        var ctns = bundle!!.getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS)
        ecsProductViewModel.initCategorized(currentPage, pageSize,ctns)
    }

    override fun isPaginationSupported(): Boolean {
        return true
    }
}

