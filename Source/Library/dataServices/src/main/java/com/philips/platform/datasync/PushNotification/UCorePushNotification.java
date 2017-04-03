package com.philips.platform.datasync.PushNotification;

public class UCorePushNotification {
    private String token;
    private String appVariant;
    private String protocolProvider;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppVariant() {
        return appVariant;
    }

    public void setAppVariant(String appVariant) {
        this.appVariant = appVariant;
    }

    public String getProtocolProvider() {
        return protocolProvider;
    }

    public void setProtocolProvider(String protocolProvider) {
        this.protocolProvider = protocolProvider;
    }
}
