package com.philips.cdp.di.mec.screens.catalog

import android.arch.lifecycle.Observer
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.rest.RestInterface
import junit.framework.Assert
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
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

    lateinit var  appInfra: AppInfra

    @Mock
    lateinit var ecsServices: ECSServices

    @Mock
    lateinit var eCSCatalogRepository: ECSCatalogRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mContext = InstrumentationRegistry.getInstrumentation().getContext()
        //  appInfra = AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext)
        //val ecsServices = ECSServices("iAP_MOB_DKA", appInfra)
        MecHolder.INSTANCE.eCSServices = ecsServices

        ecsProductViewModel = EcsProductViewModel()
    }


    @Test
    fun fetchProductReview_Success() {
        // Mock API response
        Mockito.`when`(this.eCSCatalogRepository.fetchProductReview(ArgumentMatchers.anyList(), ecsProductViewModel)).thenAnswer {
            return@thenAnswer  ArgumentMatchers.anyList<MECProductReview>() //Maybe.just(ArgumentMatchers.anyList<MECProductReview>())

            // Attacch fake observer
            val observer = Mockito.mock(Observer::class.java) as Observer<MutableList<MECProductReview>>
            this.ecsProductViewModel.ecsProductsReviewList.observeForever(observer)


            // Invoke
            this.ecsProductViewModel.fetchProductReview(ArgumentMatchers.anyList())

            // Verify
            Assert.assertNotNull(this.ecsProductViewModel.ecsProductsReviewList.value)
            /*  assertEquals(LiveDataResult.Status.SUCCESS, this.mainViewModel.repositoriesLiveData.value?.status)

              assertN(this.ecsProductViewModel.ecsProductsReviewList.value?.isEmpty())
              assertEquals(MutableList.     SUCCESS, this.ecsProductViewModel.repositoriesLiveData.value?.status)*/

        }
    }

    @Test
    fun fetchProductReview_error() {
        // Mock response with error
        Mockito.`when`(this.eCSCatalogRepository.fetchProductReview(ArgumentMatchers.anyList(), ecsProductViewModel)).thenAnswer {
            return@thenAnswer   SocketException(SocketException("No network here").toString())
        }

        // Attacch fake observer
        val observer = Mockito.mock(Observer::class.java) as Observer<MutableList<MECProductReview>>
        this.ecsProductViewModel.ecsProductsReviewList.observeForever(observer)

        // Invoke
        this.ecsProductViewModel.fetchProductReview(ArgumentMatchers.anyList())

        // Assertions
        assertNotNull(this.ecsProductViewModel.ecsProductsReviewList.value)
       // assertEquals(LiveDataResult.Status.ERROR, this.mainViewModel.repositoriesLiveData.value?.status)
        //assert(this.ecsProductViewModel.ecsProductsReviewList.value?.err is Throwable)
    }
}