/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.util;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;

public final class GpsUtil {

    private GpsUtil() {}

    public static boolean isGPSRequiredForWifiScan() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isGPSEnabled(@NonNull final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm != null ? lm.isProviderEnabled(LocationManager.GPS_PROVIDER) : false;
    }
}
