package com.philips.cdp2.ews.navigation;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.troubleshooting.base.BaseDialogActivity;

/**
 * Created by salvatorelafiura on 06/10/2017.
 */

public class ActivityNavigator {
    private final Context context;

    public ActivityNavigator(@NonNull Context context) {
        this.context = context;
    }

    public void showFragment(String fragmentName) {
        BaseDialogActivity.startActivity(context, fragmentName);

    }
}
