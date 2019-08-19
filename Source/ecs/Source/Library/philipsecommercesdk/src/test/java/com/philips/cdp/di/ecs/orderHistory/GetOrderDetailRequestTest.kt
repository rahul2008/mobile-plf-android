        package com.philips.cdp.di.ecs.orderHistory

        import android.content.Context
        import com.philips.cdp.di.ecs.ECSServices
        import com.philips.cdp.di.ecs.MockECSServices
        import com.philips.platform.appinfra.AppInfra
        import com.philips.platform.appinfra.rest.RestInterface
        import org.junit.Before
        import org.junit.runner.RunWith
        import org.mockito.Mock
        import org.robolectric.RobolectricTestRunner
        import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation

        @RunWith(RobolectricTestRunner::class)
        class GetOrderDetailRequestTest

        private var mContext: Context? = null


        lateinit var mockECSServices: MockECSServices
        lateinit var ecsServices: ECSServices


        private var appInfra: AppInfra? = null


        @Mock
        internal var mockRestInterface: RestInterface? = null

            @Before
            @Throws(Exception::class)
            fun setUp() {


                mContext = getInstrumentation().getContext()
                appInfra = AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext)
                appInfra!!.serviceDiscovery.homeCountry = "DE"


                mockECSServices = MockECSServices("", appInfra!!)
                ecsServices = ECSServices("", appInfra!!)

            }


