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

    @SuppressWarnings("unchecked")
    public static <T extends Parcelable> T extractParcelableFromIntentOrNull(@NonNull Intent intent,
                                                                             @NonNull String key) {
        Bundle extras = intent.getExtras();
        return extras == null ? null : (T) extras.getParcelable(key);
    }
}
