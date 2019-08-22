package com.philips.cdp.di.ecs.request

import android.util.Pair
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.philips.cdp.di.ecs.error.ECSError
import com.philips.cdp.di.ecs.error.ECSErrorConstant
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.order.OrdersData
import com.philips.cdp.di.ecs.store.ECSURLBuilder
import com.philips.cdp.di.ecs.util.ECSConfig
import com.philips.cdp.di.ecs.error.ECSErrors
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

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
        val errorMessage = ECSErrors.getDetailErrorMessage(error)
        ecsCallback.onFailure(error, errorMessage, 9000)
    }

    override fun onResponse(response: JSONObject?) {
        val orderHistoryECSErrorPair = getOrderHistoryECSErrorPair(response)

        if (orderHistoryECSErrorPair.first != null) {
            ecsCallback.onResponse(orderHistoryECSErrorPair.first)
        } else {
            ecsCallback.onFailure(orderHistoryECSErrorPair.second.getException(), orderHistoryECSErrorPair.second.getErrorMessage(), orderHistoryECSErrorPair.second.getErrorcode())
        }
    }

    override fun getJSONSuccessResponseListener(): Response.Listener<JSONObject> {
        return this;
    }

    fun getOrderHistoryECSErrorPair(response: JSONObject?): Pair<OrdersData, ECSError> {

        var ordersData: OrdersData? = null
        var detailError = ""
        var ecsError: ECSError? = null

        try {
            if (null != response && null != response.toString()) {
                detailError = response.toString()
            }
            ordersData = Gson().fromJson(response!!.toString(),
                    OrdersData::class.java)

        } catch (e: Exception) {
            ecsError = ECSError(e, detailError, ECSErrorConstant.GetDeliveryModeError.UNKNOWN_ERROR.errorCode)
        } finally {
            return Pair<OrdersData, ECSError>(ordersData, ecsError)
        }

    }
}