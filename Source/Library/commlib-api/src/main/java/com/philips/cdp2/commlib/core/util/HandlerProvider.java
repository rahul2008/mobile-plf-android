/*
 * © Koninklijke Philips N.V., 2017.
 *   All rights reserved.
 */
package com.philips.cdp2.commlib.core.util;

import android.os.Handler;
import android.os.Looper;

public class HandlerProvider {

    private static Handler mockedHandler;

    public static void enableMockedHandler(Handler handler) {
        mockedHandler = handler;
    }

    public static Handler createHandler() {
        if (mockedHandler == null) {
            return createHandler(Looper.getMainLooper());
        } else {
            return mockedHandler;
        }
    }

    public static Handler createHandler(Looper looper) {
        if (mockedHandler == null) {
            return new Handler(looper);
        } else {
            return mockedHandler;
        }
    }

    public static Handler createHandler(Handler.Callback callback) {
        if (mockedHandler == null) {
            return new Handler(callback);
        } else {
            return mockedHandler;
        }
    }
}
