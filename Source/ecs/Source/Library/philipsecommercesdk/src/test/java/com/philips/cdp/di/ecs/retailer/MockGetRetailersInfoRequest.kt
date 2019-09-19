package com.philips.cdp.di.ecs.retailer

import com.android.volley.VolleyError
import com.philips.cdp.di.ecs.TestUtil
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.retailers.ECSRetailerList
import com.philips.cdp.di.ecs.request.GetRetailersInfoRequest
import org.json.JSONException
import org.json.JSONObject

class MockGetRetailersInfoRequest(jsonFileName: String, ecsCallback: ECSCallback<ECSRetailerList, Exception>, ctn :String) : GetRetailersInfoRequest(ecsCallback,ctn) {


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