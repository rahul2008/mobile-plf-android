/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.philips.cdp2.ews.R;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class PermissionHandler {

    @Inject
    public PermissionHandler() {

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

    public void requestPermission(@NonNull final Fragment fragment,
                                  @StringRes final int title,
                                  @NonNull final String permission, final int requestCode) {
        final Resources resources = fragment.getResources();
        //noinspection ConstantConditions
        final Snackbar snack = Snackbar.make(fragment.getView(), resources.getString(title,
                resources.getString(R.string.ews_app_name_default)), Snackbar.LENGTH_INDEFINITE);

        snack.setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (fragment.isAdded()) {
                    fragment.requestPermissions(new String[]{permission}, requestCode);
                }
                snack.dismiss();
            }
        });
        snack.show();
    }
}