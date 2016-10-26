/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

/**
 * Handles the scenario where the CTNs are provided from the vertical app.
 * All other urls, we are forced to override though it makes no sense.
 */
public class LocalStore extends AbstractStore {
    private Context mContext;

    public LocalStore(final Context context) {
        mContext = context;
    }
}
