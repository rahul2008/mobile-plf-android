package com.philips.cdp.di.mec.screens.detail

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.common.MecError
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ECSProductDetailCallbackTest {


    lateinit var ecsProductDetailCallback :ECSProductDetailCallback

    @Mock
    lateinit var ecsProductDetailViewModel:EcsProductDetailViewModel

    @Mock
    lateinit var mutableLiveDataMock : MutableLiveData<ECSProduct>

    @Mock
    lateinit var mutableLiveDataMecErrorMock : MutableLiveData<MecError>

     val ecsProduct= ECSProduct()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(ecsProductDetailViewModel.ecsProduct).thenReturn(mutableLiveDataMock)
        Mockito.`when`(ecsProductDetailViewModel.mecError).thenReturn(mutableLiveDataMecErrorMock)
        ecsProductDetailCallback = ECSProductDetailCallback(ecsProductDetailViewModel)
    }


    @Test
    fun onResponse() {
        Mockito.`when`(ecsProductDetailViewModel.ecsProduct).thenReturn(mutableLiveDataMock)
        ecsProductDetailCallback.onResponse(ecsProduct)

        assertNotNull(ecsProductDetailViewModel.ecsProduct)
        //TODO check value
    }

    @Test
    fun onFailure() {

        val exception = Exception()
        val ecsError = ECSError(1000,"UNKNOWN")
        ecsProductDetailCallback.onFailure(exception,ecsError)
        assertNotNull(ecsProductDetailViewModel.mecError)
        //TODO check value
    }
}