/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.util;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.philips.cdp2.commlib.demouapp.R;

public class UiUtils {

    private UiUtils() {
        // Utility class
    }

    public static void showMessage(final @NonNull View view, final @NonNull String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showIndefiniteMessage(final @NonNull View view, final @NonNull String message) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.cml_dismiss_indefinite_message, v -> snackbar.dismiss());
        snackbar.show();
    }
}
