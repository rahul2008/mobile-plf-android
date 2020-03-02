package com.philips.cdp.di.mec.screens.catalog

import androidx.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.products.ECSProducts
import com.philips.cdp.di.mec.common.MECRequestType
import com.philips.cdp.di.mec.common.MecError

class ECSProductListCallback(private var ecsProductViewModel:EcsProductViewModel) :ECSCallback<List<ECSProduct>, Exception> {
    lateinit var mECRequestType : MECRequestType
    override fun onResponse(ecsProductList: List<ECSProduct>?) {

        val mutableLiveData = ecsProductViewModel.ecsProductsList

        val value = mutableList(mutableLiveData as MutableLiveData<MutableList<Any>>) as  MutableList<ECSProducts>

        val ecsProducts = ECSProducts()
        ecsProducts.products = ecsProductList

        value.add(ecsProducts)
        mutableLiveData.value = value as MutableList<Any>
    }

    override fun onFailure(error: Exception?, ecsError: ECSError?) {
        val mecError = MecError(error, ecsError,mECRequestType)
        ecsProductViewModel.mecError.value = mecError
    }
}