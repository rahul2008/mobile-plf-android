package com.philips.platform.pim.utilities;

import com.philips.platform.pim.BuildConfig;

public class PIMConstants {

    public static final String PIM_BASEURL = "userreg.janrainoidc.issuer";
    public static final String PIM_MIGRATION = "userreg.janrainoidc.migration";
    public static final String COMPONENT_TAGS_ID = "pim";
    public static final String PIM_KEY_ACTIVITY_THEME = "PIM_KEY_ACTIVITY_THEME";

    public static String getAppAuthApiVersion() {
        return BuildConfig.VERSION_NAME;
    }

}

