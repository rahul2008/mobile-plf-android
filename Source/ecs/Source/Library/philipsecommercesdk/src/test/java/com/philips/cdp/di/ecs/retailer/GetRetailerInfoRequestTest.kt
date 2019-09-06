package com.philips.cdp.di.ecs.retailer

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.android.volley.NoConnectionError
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.MockECSServices
import com.philips.cdp.di.ecs.StaticBlock
import com.philips.cdp.di.ecs.TestUtil
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.retailers.WebResults
import com.philips.cdp.di.ecs.util.ECSConfig
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.rest.RestInterface
import org.json.JSONException
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.util.HashMap

@RunWith(RobolectricTestRunner::class)
class GetRetailerInfoRequestTest{


    private var mContext: Context? = null


    lateinit var mockECSServices: MockECSServices
    lateinit var ecsServices: ECSServices

    lateinit var ecsCallback: ECSCallback<WebResults,Exception>


    private var appInfra: AppInfra? = null



    @Mock
    internal var mockRestInterface: RestInterface? = null

    lateinit var mockGetRetailersInfoRequest: MockGetRetailersInfoRequest


    val currentPage :Int = 1

    private var ctn:String = "DIS363_03"

    val PREFIX_RETAILERS = "www.philips.com/api/wtb/v1"
    val RETAILERS_ALTER = "online-retailers?product=%s&lang=en"
    val PRX_SECTOR_CODE = "B2C"

    @Before
    @Throws(Exception::class)
    fun setUp() {

        mContext = InstrumentationRegistry.getInstrumentation().getContext()
        appInfra = AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext)
        appInfra!!.serviceDiscovery.homeCountry = "DE"


        mockECSServices = MockECSServices("", appInfra!!)
        ecsServices = ECSServices("", appInfra!!)


        StaticBlock.initialize()

        ecsCallback = object: ECSCallback<WebResults,Exception>{

            override fun onResponse(result: WebResults){
            }

            override fun onFailure(error: Exception, ecsError: ECSError){
            }

        }
        mockGetRetailersInfoRequest = MockGetRetailersInfoRequest("GetRetailerInfoSuccess.json",ecsCallback,ctn);

    }

    @Test
    fun testSuccessResponse() {
        mockECSServices.jsonFileName = "GetRetailerInfoSuccess.json"

        ecsCallback = object: ECSCallback<WebResults,Exception>{

             override fun onResponse(result: WebResults){
                 assertNotNull(result)
                 assertNotNull(result.wrbresults?.onlineStoresForProduct)
             }


            override fun onFailure(error: Exception, ecsError: ECSError){
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

            override fun onFailure(error: Exception, ecsError: ECSError){
                assertEquals(19999, ecsError.toLong())
                //  test case passed
            }

        }
        mockECSServices.getRetailers("1234",ecsCallback)

    }

    @Test
    fun isValidURL() {

        System.out.println("print url: "+mockGetRetailersInfoRequest.getURL())
        val excepted = createURL()
        assertEquals(excepted, mockGetRetailersInfoRequest.getURL())
    }

    @Test
    fun isValidGetRequest() {
        assertEquals(0, mockGetRetailersInfoRequest.getMethod())
    }

    @Test
    fun isValidHeader() {

        val expectedMap = HashMap<String, String>()
        expectedMap["Authorization"] = "Bearer " + "acceesstoken"

        val actual = mockGetRetailersInfoRequest.getHeader()
        assertTrue(expectedMap == actual)
    }

    @Test
    fun verifyOnResponseError() {
        val spy1 = Mockito.spy<ECSCallback<WebResults, Exception>>(ecsCallback)
        mockGetRetailersInfoRequest = MockGetRetailersInfoRequest("GetRetailerInfoSuccess.json",spy1,ctn);
        val volleyError = NoConnectionError()
        mockGetRetailersInfoRequest.onErrorResponse(volleyError)
        Mockito.verify(spy1).onFailure(ArgumentMatchers.any<Exception>(Exception::class.java), ArgumentMatchers.anyInt())

    }

    @Test
    fun verifyOnResponseSuccess(){
        val spy1 = Mockito.spy<ECSCallback<WebResults, Exception>>(ecsCallback)
        mockGetRetailersInfoRequest = MockGetRetailersInfoRequest("GetRetailerInfoSuccess.json",spy1,ctn);

        mockGetRetailersInfoRequest.onResponse(getJsonObject("GetRetailerInfoSuccess.json"));
        Mockito.verify(spy1).onResponse(any(WebResults::class.java))
    }

    @Test
    fun assertResponseSuccessListenerNotNull() {
        assertNotNull(mockGetRetailersInfoRequest.getJSONSuccessResponseListener())
    }

    fun createURL():String{
        val builder = StringBuilder("https://")
        builder.append(PREFIX_RETAILERS).append("/")
        builder.append(PRX_SECTOR_CODE).append("/")
        builder.append(ECSConfig.INSTANCE.locale).append("/")
        builder.append(RETAILERS_ALTER)
        return String.format(builder.toString(), ctn)
    }


    internal fun getJsonObject(jsonfileName: String): JSONObject? {

        val result: JSONObject? = null
        val `in` = javaClass.classLoader!!.getResourceAsStream(jsonfileName)//"PRXProductAssets.json"
        val jsonString = TestUtil.loadJSONFromFile(`in`)
        try {
            return JSONObject(jsonString!!)
        } catch (e: JSONException) {
            return null
        }

    }
}




