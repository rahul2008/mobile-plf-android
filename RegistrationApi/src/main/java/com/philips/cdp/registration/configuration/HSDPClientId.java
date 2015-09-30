package com.philips.cdp.registration.configuration;

import java.util.HashMap;

/**
 * Created by 310190722 on 9/30/2015.
 */
public class HSDPClientId {

    public static final String SHARED = "Shared";

    public static final String SECRET = "Secret";

    public static final String BASE_URL = "BaseURL";

    private HashMap<String, String> ids;

    public String getSharedId() {
        if (ids == null) {
            return null;
        }
        return ids.get(SHARED);
    }


    public String getSecretId() {
        if (ids == null) {
            return null;
        }
        return ids.get(SECRET);
    }


    public String getBaseUrl() {
        if (ids == null) {
            return null;
        }
        return ids.get(BASE_URL);
    }

    public void setIds(HashMap<String, String> ids) {
        this.ids = ids;
    }
}



