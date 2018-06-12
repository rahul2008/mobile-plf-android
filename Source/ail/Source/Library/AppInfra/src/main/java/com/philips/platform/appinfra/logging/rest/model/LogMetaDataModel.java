package com.philips.platform.appinfra.logging.rest.model;

/**
 * Created by abhishek on 5/17/18.
 */


import com.google.gson.annotations.SerializedName;

public class LogMetaDataModel {

    @SerializedName("devicetype")
    private String devicetype;
    @SerializedName("appsid")
    private String appsid;
    @SerializedName("appstate")
    private String appstate;
    @SerializedName("locale")
    private String locale;
    @SerializedName("homecountry")
    private String homecountry;
    @SerializedName("localtime")
    private String localtime;
    @SerializedName("networktype")
    private String networktype;
    @SerializedName("description")
    private String description;
    @SerializedName("details")
    private String details;

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getAppsid() {
        return appsid;
    }

    public void setAppsid(String appsid) {
        this.appsid = appsid;
    }

    public String getAppstate() {
        return appstate;
    }

    public void setAppstate(String appstate) {
        this.appstate = appstate;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getHomecountry() {
        return homecountry;
    }

    public void setHomecountry(String homecountry) {
        this.homecountry = homecountry;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public String getNetworktype() {
        return networktype;
    }

    public void setNetworktype(String networktype) {
        this.networktype = networktype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
