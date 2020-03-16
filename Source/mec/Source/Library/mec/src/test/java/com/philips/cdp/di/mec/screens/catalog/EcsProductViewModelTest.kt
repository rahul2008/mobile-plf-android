package com.philips.cdp.di.mec.screens.catalog

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.utils.MECDataHolder
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.ArrayList

@RunWith(JUnit4::class)
class EcsProductViewModelTest {


    lateinit var ecsProductViewModel: EcsProductViewModel

    @Mock
    lateinit var ecsServices: ECSServices

    @Mock
    lateinit var eCSCatalogRepository: ECSCatalogRepository

    @Mock
    lateinit var ecsProductsCallback: ECSProductsCallback



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        MECDataHolder.INSTANCE.eCSServices = ecsServices
        ecsProductViewModel = EcsProductViewModel()
        ecsProductViewModel.ecsCatalogRepository = eCSCatalogRepository
        ecsProductViewModel.ecsProductsCallback = ecsProductsCallback
    }


    @Test
    fun initShouldCallGetProducts() {
        ecsProductViewModel.init(0,20)
        Mockito.verify(eCSCatalogRepository).getProducts(0,20,ecsProductsCallback,ecsServices)
    }

    @Test
    fun initCategorizedRetailerShouldGetCategorizedProductsforRetailer() {
        val arrayList = ArrayList<String>()
        arrayList.add("CTN")
        ecsProductViewModel.initCategorizedRetailer(arrayList)
        //Mockito.verify(eCSCatalogRepository).getCategorizedProductsForRetailer(arrayList,ecsProductViewModel,ecsServices)
    }

    @Test
    fun initCategorizedShouldGetCategorizedProducts() {
        val arrayList = ArrayList<String>()
        arrayList.add("CTN")
        ecsProductViewModel.initCategorized(0,20,arrayList)
        Mockito.verify(eCSCatalogRepository).getCategorizedProducts(0,20,1,arrayList,null,ecsProductViewModel)
    }

    @Test
    fun shouldFetchProductReview() {
        val arrayList = ArrayList<ECSProduct>()
        arrayList.add(ECSProduct())
        ecsProductViewModel.fetchProductReview(arrayList)
        Mockito.verify(eCSCatalogRepository).fetchProductReview(arrayList,ecsProductViewModel)
    }
}