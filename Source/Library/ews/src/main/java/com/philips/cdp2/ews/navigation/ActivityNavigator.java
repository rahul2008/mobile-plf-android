package com.philips.cdp2.ews.navigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.troubleshooting.base.BaseDialogActivity;

/**
 * Created by salvatorelafiura on 06/10/2017.
 */

public class ActivityNavigator {

    @NonNull private final Context context;

    public ActivityNavigator(@NonNull Context context) {
        this.context = context;
    }

    public void showFragment(String fragmentName) {
        BaseDialogActivity.startActivity(context, fragmentName);
    }

    public void showFragmentWithResult(@NonNull Fragment currentFragment, @NonNull String fragmentName,
                                       int requestCode) {
        BaseDialogActivity.startActivityForResult(currentFragment, fragmentName, requestCode);
    }

}
