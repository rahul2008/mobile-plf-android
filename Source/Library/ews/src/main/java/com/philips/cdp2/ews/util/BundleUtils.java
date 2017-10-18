package com.philips.cdp2.ews.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BundleUtils {

    public static String extractStringFromBundleOrThrow(@Nullable Bundle bundle, @NonNull String key) {
        if (bundle == null || !bundle.containsKey(key)) {
            throw new IllegalStateException("Key: " + key + " is missing in bundle!");
        }
        return bundle.getString(key);
    }

    public static Parcelable extractParcelableFromIntent(@NonNull Intent intent, String key, @NonNull RuntimeException e) {
        Bundle extras = intent.getExtras();
        checkBundleContainsKey(extras, key, e);
        return extras.getParcelable(key);
    }

    private static void checkBundleContainsKey(@NonNull Bundle extras, String key, @NonNull RuntimeException e) {
        assertNotNull(extras, e);
        if (!extras.containsKey(key)) {
            throw e;
        }
    }

    private static void assertNotNull(@Nullable Object object, @NonNull RuntimeException e) {
        if (object == null) {
            throw e;
        }
    }

}
