/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.philips.pins.shinelib.utility.SHNLogger;

public final class UiUtils {

    private static final String TAG = "UiUtils";

    private UiUtils() {
        // Utility class
    }

    private static void logAndShowMessage(View view, String message, boolean isIndefinite) {
        SHNLogger.i(TAG, message);

        if (view == null) {
            return;
        }
        Snackbar.make(view, message, isIndefinite ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG).show();
    }

    public static void showVolatileMessage(View view, String message) {
        logAndShowMessage(view, message, false);
    }

    public static void showPersistentMessage(View view, String message) {
        logAndShowMessage(view, message, true);
    }

    public static void showConfirmationMessage(View view, String message, View.OnClickListener confirmationListener) {
        Snackbar snackBar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction("Ok", confirmationListener);
        snackBar.show();
    }
}
