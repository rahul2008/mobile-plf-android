package com.philips.cdp.di.mec.screens.detail

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.integration.MecHolder
import org.junit.Assert
import org.junit.Before
import org.junit.Test


import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

import java.lang.Exception



//@RunWith(RobolectricTestRunner::class)
@RunWith(JUnit4::class)
class ECSProductDetailRepositoryTest {

    lateinit var mContext: Context

    @Mock
    lateinit var ecsProductDetailViewModel : EcsProductDetailViewModel

    @Mock
    lateinit var  eCSProductDetailRepository : ECSProductDetailRepository

    @Mock
    lateinit var ecsServices: ECSServices

    lateinit var eCSProduct : ECSProduct;


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        eCSProduct = ECSProduct()
        eCSProduct.code = "HX12345/00"
    }


}