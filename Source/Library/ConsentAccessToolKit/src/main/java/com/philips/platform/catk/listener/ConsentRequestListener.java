/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.listener;

import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.philips.platform.catk.request.ConsentRequest;

public interface ConsentRequestListener {
    void onResponse(ConsentRequest request, JsonArray response);

    void onErrorResponse(ConsentRequest request, VolleyError error);
}
