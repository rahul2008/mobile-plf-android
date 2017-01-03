
package com.philips.cdp.registration.ui.utils;

import android.util.Log;

import com.janrain.android.engage.JREngage;
import com.philips.dhpclient.util.HsdpLog;

public class RLog {

    public static final String FRAGMENT_LIFECYCLE = "FragmentLifecycle";

    public static final String ACTIVITY_LIFECYCLE = "ActivityLifecycle";

    public static final String EVENT_LISTENERS = "EventListeners";

    public static final String APPLICATION = "RegistrationApplication";

    public static final String NETWORK_STATE = "NetworkState";

    public static final String JANRAIN_INITIALIZE = "JanrainInitialize";

    public static final String VERSION = "Version";

    public static final String EXCEPTION = "Exception";

    public static final String ONCLICK = "onClick";

    public static final String CALLBACK = "CallBack";

    public static final String ANALYTICS = "Analytics";

    public static final String HSDP = "Hsdp";

    private static boolean isLoggingEnabled;

    public static void enableLogging() {
        HsdpLog.enableLogging();
        isLoggingEnabled = true;
        JREngage.sLoggingEnabled = Boolean.TRUE;
    }

    public static void disableLogging()
    {
        HsdpLog.disableLogging();
        isLoggingEnabled = false;
        JREngage.sLoggingEnabled = Boolean.FALSE;
    }

    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            Log.e(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            Log.i(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            Log.v(tag, message);
        }
    }

}
