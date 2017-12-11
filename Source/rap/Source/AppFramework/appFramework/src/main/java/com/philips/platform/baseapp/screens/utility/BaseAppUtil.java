package com.philips.platform.baseapp.screens.utility;

import android.content.Context;
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

    /**
     * Check whether Data services polling is enabled or not
     * @param context
     * @return
     */
    public static boolean isDSPollingEnabled(Context context){

        String isPollingEnabled= (String) ((AppFrameworkApplication)context.getApplicationContext()).getAppInfra().getConfigInterface().getPropertyForKey("PushNotification.polling","ReferenceApp",new AppConfigurationInterface.AppConfigurationError());
        if(!TextUtils.isEmpty(isPollingEnabled) && Boolean.parseBoolean(isPollingEnabled)) {
            RALog.d("is DSPolling Enabled ", "  True ");

            return true;

        }
        RALog.d("is DSPolling Enabled ", "  false ");
        return false;
    }

    public static boolean isAutoLogoutEnabled(Context context){
        String isAutoLogoutEnabled= (String) ((AppFrameworkApplication)context.getApplicationContext()).getAppInfra().getConfigInterface().getPropertyForKey("PushNotification.autoLogout","ReferenceApp",new AppConfigurationInterface.AppConfigurationError());
        if(!TextUtils.isEmpty(isAutoLogoutEnabled) && Boolean.parseBoolean(isAutoLogoutEnabled)) {
            RALog.d("is AutoLogout Enabled ", "  True ");

            return true;
        }
        RALog.d("is AutoLogout Enabled ", "  false ");

        return false;
    }

}
