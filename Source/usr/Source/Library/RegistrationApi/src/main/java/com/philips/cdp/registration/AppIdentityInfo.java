package com.philips.cdp.registration;

/**
 * Created by 310190722 on 8/31/2016.
 */
public class AppIdentityInfo {

    private String appLocalizedNAme;
    private String sector;
    private String micrositeId;
    private String appName;
    private String appState;
    private String appVersion;
    private String serviceDiscoveryEnvironment;


    public String getAppLocalizedNAme() {
        return appLocalizedNAme;
    }

    public void setAppLocalizedNAme(String appLocalizedNAme) {
        this.appLocalizedNAme = appLocalizedNAme;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getMicrositeId() {
        return micrositeId;
    }

    public void setMicrositeId(String micrositeId) {
        this.micrositeId = micrositeId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppState() {
        return appState;
    }

    public void setAppState(String appState) {
        this.appState = appState;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getServiceDiscoveryEnvironment() {
        return serviceDiscoveryEnvironment;
    }

    public void setServiceDiscoveryEnvironment(String serviceDiscoveryEnvironment) {
        this.serviceDiscoveryEnvironment = serviceDiscoveryEnvironment;
    }
}
