package com.philips.platform.udi.utilities;

import com.philips.platform.udi.BuildConfig;

public class UdiConstants {

    public static final String COMPONENT_TAGS_ID = "udi";
    public static final String UDI_KEY_ACTIVITY_THEME = "UDI_KEY_ACTIVITY_THEME";

    public static String getAppAuthApiVersion() {
        return BuildConfig.VERSION_NAME;
    }
}

