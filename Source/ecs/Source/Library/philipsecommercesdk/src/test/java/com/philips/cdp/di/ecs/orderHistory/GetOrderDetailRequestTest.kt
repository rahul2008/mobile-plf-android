package com.philips.cdp.di.ecs.orderHistory

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.MockECSServices
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.orders.OrderDetail
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.rest.RestInterface
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GetOrderDetailRequestTest{


    private var mContext: Context? = null


    lateinit var mockECSServices: MockECSServices
    lateinit var ecsServices: ECSServices

    lateinit var ecsCallback: ECSCallback<OrderDetail,Exception>


    private var appInfra: AppInfra? = null

    @Mock
    internal var mockRestInterface: RestInterface? = null

    lateinit var mockGetOrderDetailRequest: MockGetOrderDetailRequest
    val orderID : String = "123"

    @Before
    @Throws(Exception::class)
    fun setUp() {

        mContext = InstrumentationRegistry.getInstrumentation().getContext()
        appInfra = AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext)
        appInfra!!.serviceDiscovery.homeCountry = "DE"


        mockECSServices = MockECSServices("", appInfra!!)
        ecsServices = ECSServices("", appInfra!!)


        ecsCallback = object: ECSCallback<OrderDetail,Exception>{

            override fun onResponse(result: OrderDetail){
                assertNotNull(result)
                assertNotNull(result.deliveryOrderGroups?.get(0)?.entries)
            }


            override fun onFailure(error: Exception, errorCode: Int){
                assertTrue(true)
                //  test case failed
            }

        }
        mockGetOrderDetailRequest = MockGetOrderDetailRequest("GetOrderDetailSuccess.json",orderID,ecsCallback);
    }

    @Test
    fun testSuccessResponse() {
        mockECSServices.jsonFileName = "GetOrderDetailSuccess.json"

        ecsCallback = object: ECSCallback<OrderDetail,Exception>{

             override fun onResponse(result: OrderDetail){
                 assertNotNull(result)
                 assertNotNull(result.deliveryOrderGroups?.get(0)?.entries)
             }


            override fun onFailure(error: Exception, errorCode: Int){
                assertTrue(true)
                //  test case failed
            }

        }

        mockECSServices.getOrderDetail("1234",ecsCallback)

    }

    @Test
    fun testFailureResponse() {

        mockECSServices.jsonFileName = "GetOrderDetailFailure.json"

        ecsCallback = object: ECSCallback<OrderDetail,Exception>{

            override fun onResponse(result: OrderDetail){
                assertTrue(true)
                //  test case failed
            }

            override fun onFailure(error: Exception, errorCode: Int){
                assertEquals(19999, errorCode.toLong())
                //  test case passed
            }

        }
        mockECSServices.getOrderDetail("1234",ecsCallback)

    }
}




