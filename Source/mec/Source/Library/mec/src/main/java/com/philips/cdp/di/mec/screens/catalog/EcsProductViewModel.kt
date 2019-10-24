package com.philips.cdp.di.mec.screens.catalog

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.products.ECSProducts
import com.philips.cdp.di.mec.activity.MecError

class EcsProductViewModel : ViewModel() {

    val ecsProductsList = MutableLiveData<MutableList<ECSProducts>>()
    val ecsCategorizedProducts = MutableLiveData<MutableList<List<ECSProduct>>>()
    val mecError = MutableLiveData<MecError>()

    fun init (pageNumber:Int , pageSize:Int){
        ECSServiceRepository.INSTANCE.getProducts(pageNumber,pageSize,this)
    }

    fun initCategorizedRetailer (ctn: MutableList<String>){
        ECSServiceRepository.INSTANCE.getCategorizedProductsforRetailer(ctn,this)
    }



    fun initCategorized(pageNumber:Int , pageSize:Int,ctns :List<String>){
        ECSServiceRepository.INSTANCE.getCategorizedProducts(pageNumber,pageSize,ctns,this)
    }

}