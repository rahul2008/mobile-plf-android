package com.philips.cdp.di.ecs.request

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.order.OrdersData
import com.philips.cdp.di.ecs.store.ECSURLBuilder
import com.philips.cdp.di.ecs.util.ECSConfig
import com.philips.cdp.di.ecs.error.ECSNetworkError
import org.json.JSONObject
import java.util.HashMap
import kotlin.Exception

open class GetOrderHistoryRequest (currentPage: Int, ecsCallback: ECSCallback<OrdersData,Exception>) :OAuthAppInfraAbstractRequest() , Response.Listener<JSONObject> {

    private val ecsCallback = ecsCallback
    private val currentPage = currentPage

    override fun getURL(): String {
       return ECSURLBuilder().getOrderHistoryUrl(currentPage.toString())
    }

    override fun getHeader(): Map<String, String>? {
        val authMap = HashMap<String, String>()
        authMap["Authorization"] = "Bearer " + ECSConfig.INSTANCE.accessToken
        return authMap
    }

    override fun getMethod(): Int {
        return Request.Method.GET
    }

    override fun onErrorResponse(error: VolleyError?) {
        val ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error)
        ecsCallback.onFailure(ecsError.exception, ecsError.errorcode)
    }

    override fun onResponse(response: JSONObject?) {

        try{
            val ordersData = Gson().fromJson(response!!.toString(),
                    OrdersData::class.java)
            ecsCallback.onResponse(ordersData)
        }catch (exception : Exception){
            val ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(ECSErrorEnum.somethingWentWrong, exception, response.toString())
            ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode())
        }
    }

    override fun getJSONSuccessResponseListener(): Response.Listener<JSONObject> {
        return this;
    }
}