package com.philips.platform.mec.screens.retailers

import com.philips.cdp.di.ecs.ECSServices
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ECSRetailersRepositoryTest {


    lateinit var  ecsRetailersRepository: ECSRetailersRepository


    @Mock
    lateinit var  ecsRetailerViewModelMock: ECSRetailerViewModel

    @Mock
    lateinit var  ecsServicesMock: ECSServices

    @Mock
    lateinit var eCSRetailerListCallback : ECSRetailerListCallback

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        ecsRetailersRepository = ECSRetailersRepository(ecsServicesMock,ecsRetailerViewModelMock)
        ecsRetailersRepository.eCSRetailerListCallback = eCSRetailerListCallback
    }


    @Test
    fun getRetailersShouldCallFetchRetailers() {

        ecsRetailersRepository.getRetailers("CTN")
        Mockito.verify(ecsServicesMock).fetchRetailers("CTN",eCSRetailerListCallback)

    }
}