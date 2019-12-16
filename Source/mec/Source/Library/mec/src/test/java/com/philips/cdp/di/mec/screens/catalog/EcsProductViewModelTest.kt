package com.philips.cdp.di.mec.screens.catalog

import android.arch.lifecycle.Observer
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.util.ECSConfiguration
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.rest.RestInterface
import junit.framework.Assert
import org.hamcrest.core.AnyOf
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.net.SocketException

@RunWith(JUnit4::class)
class EcsProductViewModelTest {
    lateinit var mContext : Context

    lateinit var ecsProductViewModel: EcsProductViewModel

    @Mock
    internal var mockRestInterface: RestInterface? = null

    @Mock
    internal var ecsCatalogRepository : ECSCatalogRepository ?=null

    lateinit var  appInfra: AppInfra

    @Mock
    lateinit var ecsServices: ECSServices

    lateinit var eCSCatalogRepository: ECSCatalogRepository



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        MecHolder.INSTANCE.eCSServices = ecsServices

        ecsProductViewModel = EcsProductViewModel()
        eCSCatalogRepository = ECSCatalogRepository()
    }


    @Test
    fun fetchProductReview_Success() {
        ecsProductViewModel.init(0,20)
        Mockito.verify(ecsCatalogRepository)?.getProducts(0,20,ecsProductViewModel)
    }
}