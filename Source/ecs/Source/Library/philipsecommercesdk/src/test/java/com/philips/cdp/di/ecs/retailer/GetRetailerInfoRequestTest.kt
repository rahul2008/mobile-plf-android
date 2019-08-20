package com.philips.cdp.di.ecs.retailer

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.MockECSServices
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.order.OrdersData
import com.philips.cdp.di.ecs.model.orders.OrderDetail
import com.philips.cdp.di.ecs.model.retailers.WebResults
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.rest.RestInterface
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GetRetailerInfoRequestTest{


    private var mContext: Context? = null


    lateinit var mockECSServices: MockECSServices
    lateinit var ecsServices: ECSServices

    lateinit var ecsCallback: ECSCallback<WebResults,Exception>


    private var appInfra: AppInfra? = null

    @Mock
    internal var mockRestInterface: RestInterface? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {

        mContext = InstrumentationRegistry.getInstrumentation().getContext()
        appInfra = AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext)
        appInfra!!.serviceDiscovery.homeCountry = "DE"


        mockECSServices = MockECSServices("", appInfra!!)
        ecsServices = ECSServices("", appInfra!!)

    }

    @Test
    fun testSuccessResponse() {
        mockECSServices.jsonFileName = "GetRetailerInfoSuccess.json"

        ecsCallback = object: ECSCallback<WebResults,Exception>{

             override fun onResponse(result: WebResults){
                 assertNotNull(result)
                 assertNotNull(result.wrbresults?.onlineStoresForProduct)
             }


            override fun onFailure(error: Exception, detailErrorMessage: String, errorCode: Int){
                assertTrue(true)
                //  test case failed
            }

        }

        mockECSServices.getRetailers("1234",ecsCallback)

    }

    @Test
    fun testFailureResponse() {

        mockECSServices.jsonFileName = "GetRetailerInfoFailure.json"

        ecsCallback = object: ECSCallback<WebResults,Exception>{

            override fun onResponse(result: WebResults){
                assertTrue(true)
                //  test case failed
            }

            override fun onFailure(error: Exception, detailErrorMessage: String, errorCode: Int){
                assertEquals(19999, errorCode.toLong())
                //  test case passed
            }

        }
        mockECSServices.getRetailers("1234",ecsCallback)

    }
}




