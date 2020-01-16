package com.philips.cdp.di.mec.screens.catalog

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.ecs.model.products.ECSProducts
import com.philips.cdp.di.mec.common.MecError
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception

@RunWith(JUnit4::class)
class ECSProductsCallbackTest {


    lateinit var callback: ECSProductsCallback

    @Mock
    lateinit var ecsProductViewModel:EcsProductViewModel
    @Mock
    lateinit var mutableLiveData: MutableLiveData<MutableList<ECSProducts>>


    @Mock
    lateinit var mutableMECError : MutableLiveData<MecError>

    @Mock
    lateinit var exception: Exception

    @Mock
    lateinit var ecsError: ECSError


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(ecsProductViewModel.ecsProductsList).thenReturn(mutableLiveData)
        Mockito.`when`(ecsProductViewModel.mecError).thenReturn(mutableMECError)
        callback = ECSProductsCallback(ecsProductViewModel)
    }

    @Test
    fun onResponse() {

        //TODO
        val ecsProducts = ECSProducts()
        val ecsProduct = ECSProduct()
        ecsProducts.products = listOf(ecsProduct)

        callback.onResponse(ecsProducts)

       // assertEquals(ecsProductViewModel.ecsProductsList.value,mutableListOf<ECSProducts>())
    }

    @Test
    fun onFailure() {
        //TODO
        callback.onFailure(exception,ecsError)
       // assertNotNull(ecsProductViewModel.mecError.value)
    }
}