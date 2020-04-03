package com.philips.platform.mec.integration

import com.philips.platform.uappframework.uappinput.UappSettings
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.powermock.modules.junit4.PowerMockRunner


@RunWith(PowerMockRunner::class)
class MECInterfaceTest{


    private lateinit var mecInterface: MECInterface

    @Mock
    private lateinit var uappDependencies : MECDependencies

    @Mock
    private lateinit var uappSettings: UappSettings

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mecInterface = MECInterface()
    }

    @Test
    fun testInit() {
        mecInterface.init(uappDependencies,uappSettings)
    }

    @Test
    fun testLaunch() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}