/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.store;

public class Store {

    private final String BASEURL_FORMAT = "https://%s/%s";

    private String hostPort;
    private String webRoot;
    private String janRainID;
    private String userName;
    private String baseURl;

    public Store(final String hostPort, final String webRoot, final String userID, final String janRainID) {
        this.hostPort = hostPort;
        this.webRoot = webRoot;
        this.janRainID = janRainID;
        this.userName = userID;
    }

    public String getBaseURl() {
        if (baseURl == null) {
            baseURl = String.format(BASEURL_FORMAT, hostPort, webRoot);
        }
        return baseURl;
    }

    public String getUser() {
        return userName;
    }

    public String getJanRainToken() {
        return janRainID;
    }
}