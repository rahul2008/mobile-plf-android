package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.store.StoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class GetAppliedVoucherRequest extends AbstractModel {


    public GetAppliedVoucherRequest(StoreListener store, Map<String, String> query,
                                    DataLoadListener listener) {
        super(store, query, listener);
    }
    @Override
    public Object parseResponse(Object response) {
        String voucherCode =null;
        try {
            JSONObject jsonObject= new JSONObject(response.toString());
            JSONArray jsonArray = jsonObject.optJSONArray("vouchers");
            voucherCode = jsonArray.getJSONObject(0).getString("voucherCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return voucherCode;
    }
    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        return store.getAppliedVoucherUrl();
    }
}
