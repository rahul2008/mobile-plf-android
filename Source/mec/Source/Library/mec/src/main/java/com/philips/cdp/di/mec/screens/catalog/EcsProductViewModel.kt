package com.philips.cdp.di.mec.screens.catalog

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.products.ECSProducts
import com.philips.cdp.di.mec.common.CommonViewModel

class EcsProductViewModel : CommonViewModel() {

    val ecsProductsList = MutableLiveData<MutableList<ECSProducts>>()

    val ecsProductsReviewList = MutableLiveData<MutableList<MECProductReview>>()


    fun init (pageNumber:Int , pageSize:Int){
        ECSCatalogRepository.INSTANCE.getProducts(pageNumber,pageSize,this)
    }

    fun initCategorizedRetailer (ctn: MutableList<String>){
        ECSCatalogRepository.INSTANCE.getCategorizedProductsforRetailer(ctn,this)
    }


    fun initCategorized(pageNumber:Int , pageSize:Int,ctns :List<String>){
        ECSCatalogRepository.INSTANCE.getCategorizedProducts(pageNumber,pageSize,ctns,this)
    }

    fun fetchProductReview(products :List<ECSProduct>){
        ECSCatalogRepository.INSTANCE.fetchProductReview(products,this)
    }

}