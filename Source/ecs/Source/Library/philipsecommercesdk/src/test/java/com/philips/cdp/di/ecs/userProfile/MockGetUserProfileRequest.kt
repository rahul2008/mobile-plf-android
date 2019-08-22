package com.philips.cdp.di.ecs.userProfile

import com.android.volley.VolleyError
import com.philips.cdp.di.ecs.TestUtil
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.user.UserProfile
import com.philips.cdp.di.ecs.request.GetUserProfileRequest
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class MockGetUserProfileRequest(jsonFileName:String, ecsCallback: ECSCallback<UserProfile, Exception>) : GetUserProfileRequest(ecsCallback) {

    internal var jsonFile: String = jsonFileName

    override fun executeRequest() {
        var result: JSONObject? = null
        val `in` = javaClass.classLoader!!.getResourceAsStream(jsonFile)
        val jsonString = TestUtil.loadJSONFromFile(`in`)
        try {
            result = JSONObject(jsonString)
        } catch (e: JSONException) {
            e.printStackTrace()
            val volleyError = VolleyError(e.message)
            onErrorResponse(volleyError)
        }

        onResponse(result)
    }
}