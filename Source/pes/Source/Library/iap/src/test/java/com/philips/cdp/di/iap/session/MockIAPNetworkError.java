/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import com.android.volley.VolleyError;

public class MockIAPNetworkError extends IAPNetworkError {

    public MockIAPNetworkError(VolleyError error, int requestCode,
                               RequestListener requestListener) {
        super(error, requestCode, requestListener);
    }

    @Override
    void initMessage(int requestCode, RequestListener requestListener){
    }
}
