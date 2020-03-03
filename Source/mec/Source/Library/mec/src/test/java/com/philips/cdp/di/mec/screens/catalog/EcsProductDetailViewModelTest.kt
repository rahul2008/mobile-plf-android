package com.philips.cdp.di.mec.screens.catalog

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.screens.detail.ECSProductDetailRepository
import com.philips.cdp.di.mec.screens.detail.EcsProductDetailViewModel
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class EcsProductDetailViewModelTest {


    lateinit var ecsProductDetailViewModel: EcsProductDetailViewModel

    @Mock
    lateinit var ecsServices: ECSServices

    @Mock
    lateinit var eCSCatalogRepository: ECSProductDetailRepository



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        MecHolder.INSTANCE.eCSServices = ecsServices
        ecsProductDetailViewModel = EcsProductDetailViewModel()
        ecsProductDetailViewModel.ecsProductDetailRepository = eCSCatalogRepository
    }


    @Test
    fun shouldGetRatings() {
        ecsProductDetailViewModel.getRatings("CTN")
        Mockito.verify(eCSCatalogRepository).getRatings("CTN")
    }


    @Test
    fun shouldGetProductDetail() {
        val ecsProduct = ECSProduct()
        ecsProductDetailViewModel.getProductDetail(ecsProduct)
        Mockito.verify(eCSCatalogRepository).getProductDetail(ecsProduct)
    }

    @Test
    fun shouldGetBazaarVoiceReview() {
        val ecsProduct = ECSProduct()
        ecsProductDetailViewModel.getBazaarVoiceReview("CTN",0,20)
        Mockito.verify(eCSCatalogRepository).fetchProductReview("CTN",0,20)
    }
}