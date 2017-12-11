/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.util.StringProvider;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class PermissionHandler {

    @NonNull BaseContentConfiguration baseContentConfiguration;
    @NonNull StringProvider stringProvider;

    @Inject
    public PermissionHandler(@NonNull BaseContentConfiguration baseContentConfiguration, @NonNull StringProvider stringProvider) {
        this.baseContentConfiguration = baseContentConfiguration;
        this.stringProvider = stringProvider;
    }

    public boolean areAllPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}