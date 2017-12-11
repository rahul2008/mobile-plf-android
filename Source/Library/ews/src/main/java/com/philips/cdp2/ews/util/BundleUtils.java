package com.philips.cdp2.ews.util;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class BundleUtils {

    private BundleUtils(){}

    public static String extractStringFromBundleOrThrow(@Nullable Bundle bundle, @NonNull String key) {
        if (bundle == null || !bundle.containsKey(key)) {
            throw new IllegalStateException("Key: " + key + " is missing in bundle!");
        }
        return bundle.getString(key);
    }

    public static Boolean extractBooleanFromBundleOrThrow(@Nullable Bundle bundle, @NonNull String key) {
        if (bundle == null || !bundle.containsKey(key)) {
            throw new IllegalStateException("Key: " + key + " is missing in bundle!");
        }
        return bundle.getBoolean(key);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Parcelable> T extractParcelableFromIntentOrNull(@Nullable Bundle extras,
                                                                             @NonNull String key) {
        return extras == null ? null : (T) extras.getParcelable(key);
    }
}
