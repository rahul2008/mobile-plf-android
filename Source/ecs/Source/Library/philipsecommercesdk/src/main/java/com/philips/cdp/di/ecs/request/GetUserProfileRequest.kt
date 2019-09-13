package com.philips.cdp.di.ecs.request

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.user.ECSUserProfile
import com.philips.cdp.di.ecs.store.ECSURLBuilder
import com.philips.cdp.di.ecs.error.ECSNetworkError
import org.json.JSONObject

open class GetUserProfileRequest(ecsCallback: ECSCallback<ECSUserProfile,Exception>) :OAuthAppInfraAbstractRequest() , Response.Listener<JSONObject> {

    val ecsCallback = ecsCallback;

    override fun getURL(): String {
       return ECSURLBuilder().getUserUrl()
    }

    override fun onErrorResponse(error: VolleyError?) {
        val ecsErrorWrapper = ECSNetworkError.getErrorLocalizedErrorMessage(error,this)
        ecsCallback.onFailure(ecsErrorWrapper.exception, ecsErrorWrapper.ecsError)
    }

    override fun getMethod(): Int {
        return Request.Method.GET
    }

    override fun onResponse(response: JSONObject?) {

        try{
           val userProfile = Gson().fromJson(response.toString(),
                    ECSUserProfile::class.java)
            ecsCallback.onResponse(userProfile)
        }catch (e :Exception){
            val ecsErrorWrapper = ECSNetworkError.getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong, e, response.toString())
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.ecsError)
        }
    }

    override fun getJSONSuccessResponseListener(): Response.Listener<JSONObject> {
        return this
    }
}