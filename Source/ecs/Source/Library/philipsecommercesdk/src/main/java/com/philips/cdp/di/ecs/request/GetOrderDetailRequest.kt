package com.philips.cdp.di.ecs.request

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.orders.OrderDetail
import com.philips.cdp.di.ecs.store.ECSURLBuilder
import com.philips.cdp.di.ecs.util.ECSConfig
import com.philips.cdp.di.ecs.error.ECSErrors
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

open class GetOrderDetailRequest (orderID: String, ecsCallback: ECSCallback<OrderDetail, Exception>) :OAuthAppInfraAbstractRequest() , Response.Listener<JSONObject> {

    val orderID = orderID
    val ecsCallback = ecsCallback

    override fun onErrorResponse(error: VolleyError?) {
        ecsCallback.onFailure(ECSErrors.getVolleyException(error), 9000)
    }

    override fun getURL(): String {
        return ECSURLBuilder().getOrderDetailUrl(orderID)
    }

    override fun getHeader(): Map<String, String>? {
        val authMap = HashMap<String, String>()
        authMap["Authorization"] = "Bearer " + ECSConfig.INSTANCE.accessToken
        return authMap
    }

    override fun getMethod(): Int {
        return Request.Method.GET
    }


    override fun onResponse(response: JSONObject?) {
        val orderDetail = Gson().fromJson<OrderDetail>(response.toString(), OrderDetail::class.java)
        ecsCallback.onResponse(orderDetail)
    }

    override fun getJSONSuccessResponseListener(): Response.Listener<JSONObject> {
        return this;
    }
}