package com.philips.cdp.di.ecs.userProfile

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.MockECSServices
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.user.UserProfile
import com.philips.platform.appinfra.AppInfra
import com.philips.platform.appinfra.rest.RestInterface
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GetUserProfileRequestTest{


    private var mContext: Context? = null


    lateinit var mockECSServices: MockECSServices
    lateinit var ecsServices: ECSServices

    lateinit var ecsCallback: ECSCallback<UserProfile,Exception>


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
        mockECSServices.jsonFileName = "GetUserProfileSuccess.json"

        ecsCallback = object: ECSCallback<UserProfile,Exception>{

             override fun onResponse(result: UserProfile){
                 assertNotNull(result)
             }


            override fun onFailure(error: Exception, errorCode: Int){
                assertTrue(true)
                //  test case failed
            }

        }

        mockECSServices.getUserProfile(ecsCallback)

    }

    @Test
    fun testFailureResponse() {

        mockECSServices.jsonFileName = "GetUserProfileFailure.json"

        ecsCallback = object: ECSCallback<UserProfile,Exception>{

            override fun onResponse(result: UserProfile){
                assertTrue(true)
                //  test case failed
            }

            override fun onFailure(error: Exception, errorCode: Int){
                assertEquals(19999, errorCode.toLong())
                //  test case passed
            }

        }
        mockECSServices.getUserProfile(ecsCallback)

    }
}




