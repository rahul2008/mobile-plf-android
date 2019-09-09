package com.philips.cdp.di.ecs.request

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.philips.cdp.di.ecs.error.ECSErrorEnum
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.orders.OrderDetail
import com.philips.cdp.di.ecs.store.ECSURLBuilder
import com.philips.cdp.di.ecs.util.ECSConfiguration
import com.philips.cdp.di.ecs.error.ECSNetworkError
import com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

open class GetOrderDetailRequest(orderID: String, ecsCallback: ECSCallback<OrderDetail, Exception>) : OAuthAppInfraAbstractRequest(), Response.Listener<JSONObject> {

    val orderID = orderID
    val ecsCallback = ecsCallback

    override fun onErrorResponse(error: VolleyError?) {
        val ecsErrorWrapper = ECSNetworkError.getErrorLocalizedErrorMessage(error,this)
        ecsCallback.onFailure(ecsErrorWrapper.exception, ecsErrorWrapper.ecsError)
    }

    override fun getURL(): String {
        return ECSURLBuilder().getOrderDetailUrl(orderID)
    }

    override fun getHeader(): Map<String, String>? {
        val authMap = HashMap<String, String>()
        authMap["Authorization"] = "Bearer " + ECSConfiguration.INSTANCE.accessToken
        return authMap
    }

    override fun getMethod(): Int {
        return Request.Method.GET
    }


    override fun onResponse(response: JSONObject?) {

        try {
            val orderDetail = Gson().fromJson(response.toString(), OrderDetail::class.java)
            ecsCallback.onResponse(orderDetail)
        } catch (e: Exception) {
            val ecsErrorWrapper = getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong, e, response.toString())
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.ecsError)
        }
    }

    override fun getJSONSuccessResponseListener(): Response.Listener<JSONObject> {
        return this;
    }
}