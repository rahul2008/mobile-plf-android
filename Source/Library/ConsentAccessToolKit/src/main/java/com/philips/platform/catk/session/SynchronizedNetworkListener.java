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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;

/**
 * Created by Maqsood on 10/12/17.
 */

public interface SynchronizedNetworkListener {
    void onSyncRequestSuccess(Response<JsonArray> jsonObjectResponse);
    void onSyncRequestError(VolleyError volleyError);
}
