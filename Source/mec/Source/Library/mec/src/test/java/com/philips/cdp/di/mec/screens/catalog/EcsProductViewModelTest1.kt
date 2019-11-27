package com.philips.cdp.di.mec.screens.catalog

import android.arch.lifecycle.Observer
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse
import com.bazaarvoice.bvandroidsdk.ConversationsDisplayCallback
import com.bazaarvoice.bvandroidsdk.ConversationsException
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.integration.MecHolder
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.rest.RestInterface
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

//@RunWith(JUnit4::class)
@RunWith(RobolectricTestRunner::class)
class EcsProductViewModelTest1{

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
    fun shouldTestFetchProduct() {
        ecsProductViewModel.init(0,20)
        Mockito.verify(ECSCatalogRepository()).getProducts(0,20,ecsProductViewModel)

    }

    @Test
    fun fetchfetchProductReviewSuccess() {
        // Mock API response
        val reviewsCb = object : ConversationsDisplayCallback<BulkRatingsResponse>{
            override fun onFailure(exception: ConversationsException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSuccess(response: BulkRatingsResponse) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        }

        val bulkRatingsResponse  = BulkRatingsResponse()

        Mockito.`when`(this.eCSCatalogRepository.fetchProductReview(ArgumentMatchers.anyList<ECSProduct>(), ecsProductViewModel)).thenAnswer {
            reviewsCb.onSuccess(bulkRatingsResponse)
            //return@thenAnswer   ArgumentMatchers.anyList<MECProductReview>()  //Maybe.just(ArgumentMatchers.anyList<MECProductReview>())


        }

        // Attacch fake observer
        val observer = mock(Observer::class.java) as Observer<MutableList<MECProductReview>>
        this.ecsProductViewModel.ecsProductsReviewList.observeForever(observer)


        // Invoke
        this.ecsProductViewModel.fetchProductReview(ArgumentMatchers.anyList())

        // Verify
        assertNotNull(this.ecsProductViewModel.ecsProductsReviewList.value)
    }

    @Test
    fun fetchProductListInit(){



    }


}


