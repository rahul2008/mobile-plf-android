package com.philips.cdp.di.mec.screens.catalog

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.model.products.ECSProducts
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ECSProductsCallbackTest {


    lateinit var callback: ECSProductsCallback

    @Mock
    lateinit var ecsProductViewModel:EcsProductViewModel
    @Mock
    lateinit var mutableLiveData: MutableLiveData<MutableList<ECSProducts>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(ecsProductViewModel.ecsProductsList).thenReturn(mutableLiveData)
        callback = ECSProductsCallback(ecsProductViewModel)
    }

    @Test
    fun onResponse() {

        callback.onResponse(ECSProducts())
        assertEquals(mutableLiveData.value,mutableListOf<ECSProducts>())
    }

    @Test
    fun onFailure() {
    }
}