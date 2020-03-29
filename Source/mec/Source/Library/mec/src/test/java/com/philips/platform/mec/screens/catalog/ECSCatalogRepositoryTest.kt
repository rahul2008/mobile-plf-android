package com.philips.platform.mec.screens.catalog

import com.philips.cdp.di.ecs.ECSServices
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class ECSCatalogRepositoryTest{

    lateinit var ecsCatalogRepository: ECSCatalogRepository


    @Mock
    lateinit var ecsServices: ECSServices

    @Mock
    lateinit var ecsCallback: ECSProductsCallback


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        ecsCatalogRepository = ECSCatalogRepository()
    }


    @Test
    fun shouldFetchProducts() {
        ecsCatalogRepository.getProducts(0,20,ecsCallback,ecsServices)
        Mockito.verify(ecsServices).fetchProducts(0, 20, ecsCallback)
    }


}