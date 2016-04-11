package com.philips.cdp.di.iap.response.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebStoreConfigResponse {

    @SerializedName("catalogId")
    @Expose
    private String catalogId;
    @SerializedName("faqUrl")
    @Expose
    private String faqUrl;
    @SerializedName("helpDeskEmail")
    @Expose
    private String helpDeskEmail;
    @SerializedName("helpDeskPhone")
    @Expose
    private String helpDeskPhone;
    @SerializedName("helpUrl")
    @Expose
    private String helpUrl;
    @SerializedName("rootCategory")
    @Expose
    private String rootCategory;
    @SerializedName("siteId")
    @Expose
    private String siteId;

    /**
     * @return The catalogId
     */
    public String getCatalogId() {
        return catalogId;
    }

    /**
     * @return The faqUrl
     */
    public String getFaqUrl() {
        return faqUrl;
    }

    /**
     * @return The helpDeskEmail
     */
    public String getHelpDeskEmail() {
        return helpDeskEmail;
    }

    /**
     * @return The helpDeskPhone
     */
    public String getHelpDeskPhone() {
        return helpDeskPhone;
    }

    /**
     * @return The helpUrl
     */
    public String getHelpUrl() {
        return helpUrl;
    }

    /**
     * @return The rootCategory
     */
    public String getRootCategory() {
        return rootCategory;
    }

    /**
     * @return The siteId
     */
    public String getSiteId() {
        return siteId;
    }
}