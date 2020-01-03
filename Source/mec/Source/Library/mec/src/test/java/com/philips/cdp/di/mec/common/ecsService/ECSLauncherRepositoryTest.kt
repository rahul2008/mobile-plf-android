package com.philips.cdp.di.mec.common.ecsService


import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.mec.common.EcsLauncherViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(JUnit4::class)
class ECSLauncherRepositoryTest {

    lateinit var ecsLauncherRepository: ECSLauncherRepository

    @Mock
    lateinit var configBooleanCallback: ConfigBooleanCallback

    @Mock
    lateinit var configurationCallback: ConfigurationCallback

    @Mock
    lateinit var ecsProductCallback: ECSProductCallback

    @Mock
    lateinit var ecsProductListCallback: ECSProductListCallback

    @Mock
    lateinit var ecsLauncherViewModelMock: EcsLauncherViewModel

    @Mock
    lateinit var ecsServicesMock: ECSServices

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        ecsLauncherRepository = ECSLauncherRepository(ecsServicesMock,ecsLauncherViewModelMock)
        ecsLauncherRepository.configBooleanCallback = configBooleanCallback
        ecsLauncherRepository.configurationCallback = configurationCallback
        ecsLauncherRepository.ecsProductCallback = ecsProductCallback
        ecsLauncherRepository.ecsProductListCallback = ecsProductListCallback
    }

    @Test
    fun configECSShouldCallConfigureECS() {
        ecsLauncherRepository.configECS()
        Mockito.verify(ecsServicesMock).configureECS(configBooleanCallback)
    }

    @Test
    fun configECSToGetConfigShouldGetConfig() {
        ecsLauncherRepository.configECSToGetConfig()
        Mockito.verify(ecsServicesMock).configureECSToGetConfiguration(configurationCallback)
    }

    @Test
    fun getProductDetailForCtnShouldCallFetchProduct() {
        ecsLauncherRepository.getProductDetailForCtn("CTN")
        Mockito.verify(ecsServicesMock).fetchProduct("CTN",ecsProductCallback)
    }

    @Test
    fun getRetailerProductDetailForCtnShouldFetchProductSummaries() {
        ecsLauncherRepository.getRetailerProductDetailForCtn("CTN")
        Mockito.verify(ecsServicesMock).fetchProductSummaries(Arrays.asList("CTN"),ecsProductListCallback)
    }
}