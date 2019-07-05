/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.cart;

import android.os.Message;

public interface IAPCartListener {
    void onSuccess(int count);

    void onFailure(final Message msg);
}
