package com.philips.platform.dscdemo.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {

    /* This method has been deprecated in favor of com.philips.platform.appinfra.rest.RestInterface::isInternetReachable() */
    @Deprecated
    public boolean isOnline(Context context) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
        } catch (Exception ignored) {
            return false;
        }
    }
}
