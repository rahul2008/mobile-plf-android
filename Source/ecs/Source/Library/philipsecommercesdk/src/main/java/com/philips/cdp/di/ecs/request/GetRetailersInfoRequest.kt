package com.philips.cdp.di.ecs.request


import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson

import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.ecs.util.ECSConfiguration
import com.philips.cdp.di.ecs.error.ECSNetworkError
import org.json.JSONObject
import java.util.HashMap

open class GetRetailersInfoRequest (ecsCallback: ECSCallback<ECSRetailerList,Exception>, ctn :String) :OAuthAppInfraAbstractRequest() , Response.Listener<JSONObject> {

    val PREFIX_RETAILERS = "www.philips.com/api/wtb/v1"
    val RETAILERS_ALTER = "online-retailers?product=%s&lang=en"
    val PRX_SECTOR_CODE = "B2C"

    val callBack = ecsCallback
    val ctn = ctn
    override fun getURL(): String {
        return createURL()
    }

    fun createURL():String{
            val builder = StringBuilder("https://")
            builder.append(PREFIX_RETAILERS).append("/")
            builder.append(PRX_SECTOR_CODE).append("/")
            builder.append(ECSConfiguration.INSTANCE.locale).append("/")
            builder.append(RETAILERS_ALTER)
            return String.format(builder.toString(), ctn)
    }

    override fun onErrorResponse(error: VolleyError?) {
        val ecsErrorWrapper = ECSNetworkError.getErrorLocalizedErrorMessage(error,this)
        callBack.onFailure(ecsErrorWrapper.exception, ecsErrorWrapper.ecsError)
    }

    override fun getMethod(): Int {
        return Request.Method.GET
    }

    override fun getHeader(): Map<String, String>? {
        val authMap = HashMap<String, String>()
        authMap["Authorization"] = "Bearer " + ECSConfiguration.INSTANCE.accessToken
        return authMap
    }

    override fun onResponse(response: JSONObject?) {

        try{
           val webResults = Gson().fromJson(response.toString(),
                    ECSRetailerList::class.java)
            callBack.onResponse(webResults)
        }catch (exception:Exception){
            val ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong, exception, response.toString())
            callBack.onFailure(ecsError.getException(), ecsError.ecsError)
        }

    }

    override fun getJSONSuccessResponseListener(): Response.Listener<JSONObject> {
        return this
    }


}