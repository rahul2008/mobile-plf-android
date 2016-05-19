package com.philips.cdp.di.iap.response.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppConfigResponse {

    @SerializedName("hostport")
    @Expose
    private String hostport;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("propositionid")
    @Expose
    private String propositionid;
    @SerializedName("theme")
    @Expose
    private Integer theme;

    /**
     * @return The hostport
     */
    public String getHostport() {
        return hostport;
    }

    /**
     * @return The site
     */
    public String getSite() {
        return site;
    }

    /**
     * @return The propositionid
     */
    public String getPropositionid() {
        return propositionid;
    }

    /**
     * @return The theme
     */
    public Integer getTheme() {
        return theme;
    }
}