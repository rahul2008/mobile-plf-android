package com.philips.cdp.di.ecs.orderHistory

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.android.volley.NoConnectionError
import com.philips.cdp.di.ecs.Address.MockCreateAddressRequest
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.MockECSServices
import com.philips.cdp.di.ecs.StaticBlock
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.address.Addresses
import com.philips.cdp.di.ecs.model.orders.OrderDetail
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.rest.RestInterface
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.util.HashMap

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

        StaticBlock.initialize()

        ecsCallback = object: ECSCallback<OrderDetail,Exception>{

            override fun onResponse(result: OrderDetail){

            }


            override fun onFailure(error: Exception, errorCode: Int){

            }

        }
        mockGetOrderDetailRequest = MockGetOrderDetailRequest("GetOrderDetailSuccess.json",orderID,ecsCallback)
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

        mockECSServices.getOrderDetail("123",ecsCallback)

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
        mockECSServices.getOrderDetail("123",ecsCallback)

    }

    @Test
    fun isValidURL() {

        System.out.println("print url: "+mockGetOrderDetailRequest.getURL())
        //acc.us.pil.shop.philips.com/pilcommercewebservices/v2/US_Tuscany/users/current/orders/123?fields=FULL&lang=en_US
        val excepted = StaticBlock.getBaseURL() + "pilcommercewebservices" + "/v2/" + StaticBlock.getSiteID() + "/users/current/orders/"+orderID+"?fields=FULL&lang=" + StaticBlock.getLocale()
        assertEquals(excepted, mockGetOrderDetailRequest.getURL())
    }

    @Test
    fun isValidGetRequest() {
        assertEquals(0, mockGetOrderDetailRequest.getMethod().toLong())
    }

    @Test
    fun isValidHeader() {

        val expectedMap = HashMap<String, String>()
        expectedMap["Authorization"] = "Bearer " + "acceesstoken"

        val actual = mockGetOrderDetailRequest.getHeader()

        assertTrue(expectedMap == actual)
    }

    @Test
    fun verifyOnResponseError() {
        val spy1 = Mockito.spy<ECSCallback<OrderDetail, Exception>>(ecsCallback)
        mockGetOrderDetailRequest = MockGetOrderDetailRequest("GetOrderDetailSuccess.json",orderID,spy1);
        val volleyError = NoConnectionError()
        mockGetOrderDetailRequest.onErrorResponse(volleyError)
        Mockito.verify(spy1).onFailure(any<Exception>(Exception::class.java), anyInt())

    }

    @Test
    fun assertResponseSuccessListenerNotNull() {
        assertNotNull(mockGetOrderDetailRequest.getJSONSuccessResponseListener())
    }
}




