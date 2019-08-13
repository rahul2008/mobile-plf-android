package com.philips.cdp.di.ecs.request

import com.android.volley.Response
import com.android.volley.VolleyError
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.order.OrdersData
import org.json.JSONObject
import java.lang.Exception

class GetOrderDetailRequest (currentPage: Int,ecsCallback: ECSCallback<OrdersData, Exception>) :OAuthAppInfraAbstractRequest() , Response.Listener<JSONObject> {

    override fun getURL(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onErrorResponse(error: VolleyError?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMethod(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResponse(response: JSONObject?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}