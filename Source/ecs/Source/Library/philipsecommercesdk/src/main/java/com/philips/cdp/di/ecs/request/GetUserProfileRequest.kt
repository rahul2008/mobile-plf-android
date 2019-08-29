package com.philips.cdp.di.ecs.request

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.user.UserProfile
import com.philips.cdp.di.ecs.store.ECSURLBuilder
import com.philips.cdp.di.ecs.error.ECSNetworkError
import org.json.JSONObject

open class GetUserProfileRequest(ecsCallback: ECSCallback<UserProfile,Exception>) :OAuthAppInfraAbstractRequest() , Response.Listener<JSONObject> {

    val ecsCallback = ecsCallback;

    override fun getURL(): String {
       return ECSURLBuilder().getUserUrl()
    }

    override fun onErrorResponse(error: VolleyError?) {
        val ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error)
        ecsCallback.onFailure(ecsError.exception, ecsError.errorcode)
    }

    override fun getMethod(): Int {
        return Request.Method.GET
    }

    override fun onResponse(response: JSONObject?) {

        try{
           val userProfile = Gson().fromJson(response.toString(),
                    UserProfile::class.java)
            ecsCallback.onResponse(userProfile)
        }catch (e :Exception){
            val ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(ECSErrorEnum.something_went_wrong, e, response.toString())
            ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode())
        }
    }

    override fun getJSONSuccessResponseListener(): Response.Listener<JSONObject> {
        return this
    }
}