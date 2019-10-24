package com.philips.cdp.di.mec.screens.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.model.products.ECSProducts
import com.philips.cdp.di.mec.utils.MECConstant

class MECCategorizedRetailerFragment : MECProductCatalogFragment(),Pagination {

    override fun addPagination(isHybris: Boolean?) {
        addPagination(false)
    }


    override fun executeRequest(){
        val bundle = arguments
        var ctns = bundle!!.getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS)
        ecsProductViewModel.initCategorizedRetailer(ctns)
    }
}
