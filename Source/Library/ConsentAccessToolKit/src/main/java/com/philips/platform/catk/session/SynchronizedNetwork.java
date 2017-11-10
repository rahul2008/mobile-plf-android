/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catk.session;

import com.android.volley.*;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.JsonArray;
import com.philips.platform.catk.request.ConsentRequest;

public class SynchronizedNetwork {

    private BasicNetwork mBasicNetwork;

    public SynchronizedNetwork(HurlStack stack) {
        HurlStack hurlStack = stack;
        if (stack == null) {
            hurlStack = new HurlStack();
        }
        mBasicNetwork = new BasicNetwork(hurlStack);
    }

    public void performRequest(ConsentRequest request, SynchronizedNetworkListener callBack) {
        try {
            NetworkResponse response = mBasicNetwork.performRequest(request);
            successResponse(request, callBack, response);

        } catch (VolleyError volleyError) {
            callBack.onSyncRequestError(volleyError);
        }
    }

    protected void successResponse(ConsentRequest request, SynchronizedNetworkListener callBack, NetworkResponse response) {
        Response<JsonArray> jsonObjectResponse = request.parseNetworkResponse(response);
        callBack.onSyncRequestSuccess(jsonObjectResponse);
    }
}
