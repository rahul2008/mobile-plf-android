package com.philips.cdp.di.iap.response.oauth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.philips.cdp.di.iap.utils.IAPLog;

public class OAuthResponse {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("expires_in")
    @Expose
    private int expiresIn;
    @SerializedName("scope")
    @Expose
    private String scope;

    public String getAccessToken() {
        IAPLog.d(IAPLog.LOG, "OAauth Access Token = "+accessToken);
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        IAPLog.d(IAPLog.LOG, "OAauth Refresh Access Token = "+refreshToken);
        return refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }
}