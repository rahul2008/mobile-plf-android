/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.os.Message;

/**
 * Contains the result for the request.
 * It bundles the result in {@link Message} and the request code Message.what field.
 */
public interface RequestListener {
    public void onSuccess(Message msg);

    public void onError(Message msg);


}
