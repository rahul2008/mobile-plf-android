package com.philips.cdp.di.ecs.request

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.user.UserProfile
import com.philips.cdp.di.ecs.store.ECSURLBuilder
import com.philips.cdp.di.ecs.util.ECSConfig
import com.philips.cdp.di.ecs.error.ECSErrors
import org.json.JSONObject
import java.util.HashMap

open class GetUserProfileRequest(ecsCallback: ECSCallback<UserProfile,Exception>) :OAuthAppInfraAbstractRequest() , Response.Listener<JSONObject> {

    val ecsCallback = ecsCallback;

    override fun getURL(): String {
       return ECSURLBuilder().getUserUrl()
    }

    override fun onErrorResponse(error: VolleyError?) {
        val errorMessage = ECSErrors.getDetailErrorMessage(error)
        ecsCallback.onFailure(error, errorMessage, 9000)
    }

    override fun getMethod(): Int {
        return Request.Method.GET
    }

    override fun onResponse(response: JSONObject?) {

        try{
            System.out.println(response.toString())
            val userProfile = Gson().fromJson(response.toString(),
                    UserProfile::class.java)
            ecsCallback.onResponse(userProfile)
        }catch (e :Exception){
            ecsCallback.onFailure(e,e.message,9000);
        }
    }

    override fun getJSONSuccessResponseListener(): Response.Listener<JSONObject> {
        return this
    }
}