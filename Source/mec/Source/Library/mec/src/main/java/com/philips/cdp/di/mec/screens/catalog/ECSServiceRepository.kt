package com.philips.cdp.di.mec.screens.catalog

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProducts
import com.philips.cdp.di.ecs.util.ECSConfiguration
import com.philips.cdp.di.mec.activity.MecError
import com.philips.platform.appinfra.AppInfra

enum class ECSServiceRepository {

    INSTANCE;

     lateinit var appInfra: AppInfra



    fun configECS(){
        val ecsServices = ECSServices("Tuscany2016", appInfra);

        ecsServices.configureECS(object : ECSCallback<Boolean,Exception>{
            override fun onFailure(error: Exception?, ecsError: ECSError?) {

                System.out.println("Config success")
            }

            override fun onResponse(result: Boolean?) {
                System.out.println("Config failed")
            }

        })
    }

    fun getProducts(pageNumber :Int ,pageSize :Int,  ecsProductViewModel :EcsProductViewModel){

        val locale = ECSConfiguration.INSTANCE.locale
        if(locale ==null){
            configECS();
        }else {
            val ecsServices = ECSServices("IAP_MOB_DKA", appInfra);
            ecsServices.fetchProducts(pageNumber, pageSize, object : ECSCallback<ECSProducts, Exception> {

                override fun onFailure(error: Exception?, ecsError: ECSError?) {
                    val mecError = MecError(error, ecsError)
                    ecsProductViewModel.mecError.value = mecError
                }

                override fun onResponse(ecsProducts: ECSProducts) {

                    val mutableLiveData = ecsProductViewModel.ecsProductsList;

                    var value = mutableLiveData.value;
                    value?.add(ecsProducts)
                    mutableLiveData.value = value
                }

            })
        }

    }


}