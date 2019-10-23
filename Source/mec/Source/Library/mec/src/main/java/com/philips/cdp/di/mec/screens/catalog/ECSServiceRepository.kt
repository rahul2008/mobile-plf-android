package com.philips.cdp.di.mec.screens.catalog

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.products.ECSProducts
import com.philips.cdp.di.ecs.util.ECSConfiguration
import com.philips.cdp.di.mec.activity.MecError
import com.philips.platform.appinfra.AppInfra

enum class ECSServiceRepository {

    INSTANCE;

    lateinit var appInfra: AppInfra


    fun getProducts(pageNumber: Int, pageSize: Int, ecsProductViewModel: EcsProductViewModel) {

        val ecsServices = ECSServices("IAP_MOB_DKA", appInfra);
        ecsServices.fetchProducts(pageNumber, pageSize, object : ECSCallback<ECSProducts, Exception> {

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsProductViewModel.mecError.value = mecError
            }

            override fun onResponse(ecsProducts: ECSProducts) {

                val mutableLiveData = ecsProductViewModel.ecsProductsList

                var value = mutableLiveData.value;

                if (value == null) value = mutableListOf<ECSProducts>()

                value?.add(ecsProducts)
                mutableLiveData.value = value
            }

        })
    }

    fun getCategorizedProducts(ctn :ArrayList<String>, ecsProductViewModel: EcsProductViewModel) {

        val ecsServices = ECSServices("IAP_MOB_DKA", appInfra);
        ecsServices.fetchProductSummaries(ctn, object : ECSCallback<List<ECSProduct>, Exception> {
            override fun onResponse(result: List<ECSProduct>?) {
                val mutableLiveData = ecsProductViewModel.ecsCategorizedProducts

                var value = mutableLiveData.value;

                if (value == null) value = mutableListOf<List<ECSProduct>>()

                value?.add(result!!)
                mutableLiveData.value = value
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsProductViewModel.mecError.value = mecError
            }

        })
    }

}

