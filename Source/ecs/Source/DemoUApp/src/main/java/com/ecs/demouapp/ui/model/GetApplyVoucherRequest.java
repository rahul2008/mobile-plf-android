package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;


import java.util.HashMap;
import java.util.Map;

public class GetApplyVoucherRequest extends AbstractModel {


    public GetApplyVoucherRequest(StoreListener store, Map<String, String> query,
                                  DataLoadListener listener) {
        super(store, query, listener);
    }


    @Override
    public Object parseResponse(Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> params = new HashMap<>();
        String voucherId = this.params.get(ModelConstants.VOUCHER_ID);
        if (voucherId != null)
            params.put(ModelConstants.VOUCHER_ID, voucherId);

        return params;
    }

    @Override
    public String getUrl() {
        return store.getApplyVoucherUrl();
    }
}
