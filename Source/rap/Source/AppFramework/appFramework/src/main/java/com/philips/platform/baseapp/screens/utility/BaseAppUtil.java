package com.philips.platform.baseapp.screens.utility;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

public class BaseAppUtil {
    public final String TAG = BaseAppUtil.class.getSimpleName();

    /**
     * Check whether network is available or not
     *
     * @return true if network available
     */
    public static boolean isNetworkAvailable(Context context) {
        return ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra().getRestClient().isInternetReachable();
    }

    public static void restartApp(Context context){
        Intent intent = context.getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage( context.getApplicationContext().getPackageName() );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

}
