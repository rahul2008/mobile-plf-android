package com.philips.cdp2.ews.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BundleUtils {

    public static String extractStringFromBundleOrThrow(@Nullable Bundle bundle, @NonNull String key) {
        if (bundle == null || !bundle.containsKey(key)) {
            throw new IllegalStateException("Key: " + key + " is missing in bundle!");
        }
        return bundle.getString(key);
    }

}
