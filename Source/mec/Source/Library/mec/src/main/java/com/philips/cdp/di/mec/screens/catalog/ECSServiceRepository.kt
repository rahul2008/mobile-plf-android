package com.philips.cdp.di.mec.screens.catalog

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.products.ECSProducts
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

    fun getCategorizedProductsforRetailer(ctn: MutableList<String>, ecsProductViewModel: EcsProductViewModel) {

        val ecsServices = ECSServices("IAP_MOB_DKA", appInfra);
        ecsServices.fetchProductSummaries(ctn, object : ECSCallback<List<ECSProduct>, Exception> {
            override fun onResponse(result: List<ECSProduct>) {
                val mutableLiveData = ecsProductViewModel.ecsProductsList

                var value = mutableLiveData.value;

                if (value == null) value = mutableListOf<ECSProducts>()

                val ecsProducts = ECSProducts()
                ecsProducts.products = result

                value?.add(ecsProducts)
                mutableLiveData.value = value
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsProductViewModel.mecError.value = mecError
            }

        })
    }

    fun getCategorizedProducts(pageNumber: Int, pageSize: Int, ctns: List<String>, ecsProductViewModel: EcsProductViewModel) {
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

                //add logic
                val ecsProductList = mutableListOf<ECSProduct>()

                for (ctn in ctns){

                    for(ecsProduct in ecsProducts.products){

                        if(ecsProduct.code.equals(ctn,false)){
                            ecsProductList.add(ecsProduct)
                        }
                    }
                }

                ecsProducts.products = ecsProductList
                value?.add(ecsProducts)


                if(shouldBreakTheLoop(pageNumber, ecsProducts, ctns)){
                    mutableLiveData.value = value
                }else{
                    var newPageNumber :Int = pageNumber+1
                    getCategorizedProducts(newPageNumber,pageSize,ctns,ecsProductViewModel)
                }

            }

        })
    }

    /*
    *   These are the below conditions to break the loop
    *
    *   1- Searched for 5 pages
    *   2- Searched for all the pages completed
    *   3- ALl CTNs are found
    *   4- Only show products of page size at a time .
    *
    * */

    private fun shouldBreakTheLoop(pageNumber: Int, ecsProducts: ECSProducts, ctns: List<String>) =
            pageNumber % 5 == 0 || pageNumber == ecsProducts.pagination.totalPages - 1 || ctns.size == ecsProducts.products.size || ecsProducts.products.size == ecsProducts.pagination.pageSize

}

