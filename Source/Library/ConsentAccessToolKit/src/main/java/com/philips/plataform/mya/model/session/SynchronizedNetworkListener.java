/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.plataform.mya.model.session;

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
