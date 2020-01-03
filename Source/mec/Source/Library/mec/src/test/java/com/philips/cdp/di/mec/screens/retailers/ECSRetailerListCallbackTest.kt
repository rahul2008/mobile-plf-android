package com.philips.cdp.di.mec.screens.retailers

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.model.retailers.ECSRetailer
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.mec.common.MecError
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class ECSRetailerListCallbackTest {


    lateinit var ecsRetailerListCallback: ECSRetailerListCallback

    @Mock
    lateinit var ecsRetailerViewModelMock: ECSRetailerViewModel

    @Mock
    lateinit var ecsRetailerList: ECSRetailerList

    @Mock
    lateinit var mutableLiveDataMock : MutableLiveData<ECSRetailerList>

    @Mock
    lateinit var mutableLiveDataMecErrorMock : MutableLiveData<MecError>


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(ecsRetailerViewModelMock.ecsRetailerList).thenReturn(mutableLiveDataMock)
        Mockito.`when`(ecsRetailerViewModelMock.mecError).thenReturn(mutableLiveDataMecErrorMock)
        ecsRetailerListCallback = ECSRetailerListCallback(ecsRetailerViewModelMock)
    }


    @Test
    fun onResponse() {
        ecsRetailerListCallback.onResponse(ecsRetailerList)
        assertNotNull(ecsRetailerViewModelMock.ecsRetailerList)
    }

    @Test
    fun shouldRemovePhilipsStoreIfHybrisIsOn() {

        val ecsRetailerList = ECSRetailerList()

        val ecsRetailer = ECSRetailer()
        ecsRetailer.isPhilipsStore = "Y"

        val list = ArrayList<ECSRetailer>()
        list.add(ecsRetailer)

        assertEquals(0,ecsRetailerListCallback.removePhilipsStoreForHybris(ecsRetailerList).retailers.size)
    }

    @Test
    fun onFailure() {

        val exception = Exception()
        val ecsError = ECSError(1000,"UNKNOWN")

        ecsRetailerListCallback.onFailure(exception,ecsError)
        assertNotNull(ecsRetailerViewModelMock.mecError)
    }
}