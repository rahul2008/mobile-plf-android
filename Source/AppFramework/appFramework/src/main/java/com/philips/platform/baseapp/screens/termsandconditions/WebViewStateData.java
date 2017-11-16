package com.philips.platform.baseapp.screens.termsandconditions;

import com.philips.platform.appframework.flowmanager.base.UIStateData;

/**
 * Created by philips on 31/07/17.
 */

public class WebViewStateData extends UIStateData {

    private WebViewEnum webViewEnum;

    public WebViewEnum getWebViewEnum() {
        return webViewEnum;
    }

    public void setWebViewEnum(WebViewEnum termsAndPrivacyEnum) {
        this.webViewEnum = termsAndPrivacyEnum;
    }

}
