package com.philips.platform.appframework.connectivity;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConnectivityUtils {

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity!=null && activity.getCurrentFocus()!=null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
