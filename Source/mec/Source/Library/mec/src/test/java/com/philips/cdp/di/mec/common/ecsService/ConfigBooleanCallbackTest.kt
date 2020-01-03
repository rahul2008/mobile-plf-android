package com.philips.cdp.di.mec.common.ecsService

import android.arch.lifecycle.MutableLiveData
import com.philips.cdp.di.mec.common.EcsLauncherViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ConfigBooleanCallbackTest {

     @Mock
     lateinit var ecsLauncherViewModelMock: EcsLauncherViewModel

     lateinit var configBooleanCallback: ConfigBooleanCallback

     @Mock
     lateinit var mutableLiveDataMock : MutableLiveData<Boolean>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(ecsLauncherViewModelMock.isHybris).thenReturn(mutableLiveDataMock)
        configBooleanCallback = ConfigBooleanCallback(ecsLauncherViewModelMock)
    }

    @Test
    fun onResponse() {
        configBooleanCallback.onResponse(true)
    }

    @Test
    fun onFailure() {
    }
}