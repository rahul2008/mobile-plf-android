/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.ssdp;

import java.nio.charset.Charset;

public class SSDPUtils {
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    public static final String PROTOCOL_HTTPS = "https";

    /*
    static {
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    */
}
