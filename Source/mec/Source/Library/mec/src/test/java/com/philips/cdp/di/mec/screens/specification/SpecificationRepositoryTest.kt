package com.philips.cdp.di.mec.screens.specification

import android.content.Context
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.prxclient.RequestManager
import com.philips.cdp.prxclient.request.ProductSpecificationRequest
import com.philips.platform.appinfra.AppInfra
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SpecificationRepositoryTest {


    lateinit var specificationRepository: SpecificationRepository
    lateinit var specificationViewModel : SpecificationViewModel
    lateinit var requestManager : RequestManager
    lateinit var  productSpecificationRequest :ProductSpecificationRequest

    @Mock
    lateinit var context : Context

    @Mock
    lateinit var appInfra :AppInfra

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(MECDataHolder.INSTANCE.appinfra).thenReturn(appInfra)
        specificationRepository = SpecificationRepository()
        specificationViewModel = SpecificationViewModel()
        requestManager = RequestManager()
         productSpecificationRequest  = ProductSpecificationRequest("HD3456/02",null)

    }

    @Test
    fun fetchSpecificationTest() {
        specificationRepository.fetchSpecification(context,"ctn",specificationViewModel)
        Mockito.verify(requestManager).executeRequest(productSpecificationRequest,PRXSpecificationResponseCallback(specificationViewModel))
    }
}