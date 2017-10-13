/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.navigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.troubleshooting.base.BaseDialogActivity;

public class ActivityNavigator {

    @NonNull
    private final Context context;

    public ActivityNavigator(@NonNull Context context) {
        this.context = context;
    }

    public void showFragment(@NonNull String fragmentName) {
        BaseDialogActivity.startActivity(context, fragmentName);
    }

    public void showFragmentWithResult(@NonNull Fragment currentFragment, @NonNull String fragmentName,
                                       int requestCode) {
        BaseDialogActivity.startActivityForResult(currentFragment, fragmentName, requestCode);
    }

}
