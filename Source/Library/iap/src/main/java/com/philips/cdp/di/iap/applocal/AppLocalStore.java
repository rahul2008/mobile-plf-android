/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.philips.cdp.di.iap.core.AbstractStoreSpec;

/**
 * Handles the scenario where the CTNs are provided from the vertical app.
 * All other urls, we are forced to override though it makes no sense.
 */
public class AppLocalStore extends AbstractStoreSpec {
    private Context mContext;

    public AppLocalStore(final Context context) {
        mContext = context;
    }
}
