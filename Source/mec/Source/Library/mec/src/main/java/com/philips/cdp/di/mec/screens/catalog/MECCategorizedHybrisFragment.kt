package com.philips.cdp.di.mec.screens.catalog

import android.os.Bundle
import android.view.View
import com.philips.cdp.di.mec.utils.MECConstant

class MECCategorizedHybrisFragment: MECProductCatalogFragment(){

    lateinit var  ctns: ArrayList<String>

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        ctns = bundle!!.getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS) as ArrayList<String>
    }

    override fun executeRequest() {
        ecsProductViewModel.initCategorized(currentPage,pageSize,ctns)
    }
}