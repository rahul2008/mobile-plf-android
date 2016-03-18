/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public interface SynchronizedNetworkCallBack<T> {
    void onSyncRequestSuccess(Response<T> response);
    void onSyncRequestError(VolleyError volleyError);
}
