/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.session.OAuthHandler;

public class Store {

    private final static String BASEURL_FORMAT = "https://%s/%s";

    private String hostPort;
    private String webRoot;
    private String janRainID;
    private String userName;
    private String baseURl;

    private OAuthHandler oAuthHandler;
    private Context context;

    public Store(Context context, final String hostPort, final String webRoot, final String userID,
                 final String janRainID) {
        this.context = context;
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

    public String getAuthToken() {
        return oAuthHandler.generateToken(context, janRainID, userName);
    }

    public void setAuthHandler(final OAuthHandler oAuthHandler) {
        this.oAuthHandler = oAuthHandler;
    }
}