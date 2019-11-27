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
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

//@RunWith(JUnit4::class)
@RunWith(RobolectricTestRunner::class)
class EcsProductViewModelTest1 {

    lateinit var mContext: Context

    lateinit var ecsProductViewModel: EcsProductViewModel

    @Mock
    internal var mockRestInterface: RestInterface? = null

    lateinit var appInfra: AppInfra

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
        ecsProductViewModel.init(0, 20)
        Mockito.verify(ECSCatalogRepository()).getProducts(0, 20, ecsProductViewModel)

    }

    @Test
    fun fetchfetchProductReviewSuccess() {
        // Mock API response
        val reviewsCb = object : ConversationsDisplayCallback<BulkRatingsResponse> {
            override fun onFailure(exception: ConversationsException) {
                //failed test case
                assertFalse(false,exception.toString())

            }

            override fun onSuccess(response: BulkRatingsResponse) {
                //passed test case
                assertNotNull(response)
                assertNotNull(ecsProductViewModel.ecsProductsReviewList)
            }

        }

        val bulkRatingsResponse = BulkRatingsResponse()
        var productList: MutableList<ECSProduct> = mutableListOf()
        var product1: ECSProduct = ECSProduct()
        product1.code = "HR2100/00"
        var product2: ECSProduct = ECSProduct()
        product2.code = "HR3555/00"
        productList.add(product1)
        productList.add(product2)


        Mockito.`when`(this.eCSCatalogRepository.fetchProductReview(productList, ecsProductViewModel)).thenAnswer {
            reviewsCb.onSuccess(bulkRatingsResponse)

        }

        // Attacch fake observer
        val observer = mock(Observer::class.java) as Observer<MutableList<MECProductReview>>
        this.ecsProductViewModel.ecsProductsReviewList.observeForever(observer)

        // Invoke
        this.eCSCatalogRepository.fetchProductReview(productList, ecsProductViewModel)

    }

    @Test
    fun fetchProductListInit() {


    }


}


