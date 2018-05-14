package com.philips.platform.appinfra.logging.model;

/**
 * Created by abhishek on 4/30/18.
 */

public class AILCloudLogMetaData {

    private String homeCountry;

    private String locale;


    private String userUUID;

    private String appState;

    private String appVersion;

    private String appsId;

    private String appName;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHomeCountry() {
        return homeCountry;
    }

    public void setHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
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

    public String getAppsId() {
        return appsId;
    }

    public void setAppsId(String appsId) {
        this.appsId = appsId;
    }

}
