/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.config;

import com.philips.cdp.di.ecs.util.ECSConfig;

public class HybrisConfigResponse {

    public String getLocale() {
        return ECSConfig.INSTANCE.getLocale();
    }

    private String locale;
    private String catalogId;
    private String faqUrl;
    private String helpDeskEmail;
    private String helpDeskPhone;
    private String helpUrl;
    private String rootCategory;
    private String siteId;

    public String getCatalogId() {
        return catalogId;
    }

    public String getFaqUrl() {
        return faqUrl;
    }

    public String getHelpDeskEmail() {
        return helpDeskEmail;
    }

    public String getHelpDeskPhone() {
        return helpDeskPhone;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public String getRootCategory() {
        return rootCategory;
    }

    public String getSiteId() {
        return siteId;
    }
}
