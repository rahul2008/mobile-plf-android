package com.philips.platform.datasync.PushNotification;

public class UCorePushNotification {
    private String protocolAddress;
    private String appVariant;
    private String protocolProvider;

    public String getProtocolAddress() {
        return protocolAddress;
    }

    public void setProtocolAddress(String protocolAddress) {
        this.protocolAddress = protocolAddress;
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
