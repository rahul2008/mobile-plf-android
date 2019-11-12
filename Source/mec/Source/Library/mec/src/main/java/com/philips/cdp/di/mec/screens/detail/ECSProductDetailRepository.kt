package com.philips.cdp.di.mec.screens.detail

import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.integration.MecHolder
import java.util.*

class ECSProductDetailRepository {

    fun getProductDetail(ecsProduct: ECSProduct, ecsProductDetailViewModel: EcsProductDetailViewModel){

        val eCSServices = MecHolder.INSTANCE.eCSServices

        eCSServices.fetchProductDetails(ecsProduct,object : ECSCallback<ECSProduct, Exception>{

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsProductDetailViewModel.mecError.value = mecError
            }

            override fun onResponse(ecsProduct: ECSProduct?) {
                ecsProductDetailViewModel.ecsProduct.value = ecsProduct
            }
        })
    }

    fun getProductDetailForCtn(ctn: String, ecsProductDetailViewModel: EcsProductDetailViewModel) {

        val eCSServices = MecHolder.INSTANCE.eCSServices

        eCSServices.fetchProduct(ctn,object:ECSCallback<ECSProduct,Exception>{
            override fun onResponse(result: ECSProduct) {
                getProductDetail(result,ecsProductDetailViewModel)
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsProductDetailViewModel.mecError.value = mecError
            }
        })
    }

    fun getRetailerProductDetailForCtn(ctn: String, ecsProductDetailViewModel: EcsProductDetailViewModel) {

        val eCSServices = MecHolder.INSTANCE.eCSServices

        eCSServices.fetchProductSummaries(Arrays.asList(ctn),object:ECSCallback<List<ECSProduct>,Exception>{
            override fun onResponse(result: List<ECSProduct>) {
                getProductDetail(result.get(0),ecsProductDetailViewModel)
            }

            override fun onFailure(error: Exception?, ecsError: ECSError?) {
                val mecError = MecError(error, ecsError)
                ecsProductDetailViewModel.mecError.value = mecError
            }
        })

    }
}