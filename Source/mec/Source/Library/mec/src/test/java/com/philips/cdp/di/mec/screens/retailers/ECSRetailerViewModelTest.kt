package com.philips.cdp.di.mec.screens.retailers

import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.mec.utils.MECDataHolder
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ECSRetailerViewModelTest {


    lateinit var ecsRetailerViewModel: ECSRetailerViewModel

    @Mock
    lateinit var ecsServices: ECSServices

    @Mock
    lateinit var ecsRetailersRepository: ECSRetailersRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        MECDataHolder.INSTANCE.eCSServices = ecsServices
        ecsRetailerViewModel = ECSRetailerViewModel()

        ecsRetailerViewModel.ecsServices = ecsServices
        ecsRetailerViewModel.ecsRetailersRepository = ecsRetailersRepository
    }

    @Test
    fun getRetailersShouldCallRetailersForCTN() {

        ecsRetailerViewModel.getRetailers("CTN")
        Mockito.verify(ecsRetailersRepository).getRetailers("CTN")
    }





    @Test
    fun setAdapter() {
    }

    @Test
    fun setStock() {
    }
}