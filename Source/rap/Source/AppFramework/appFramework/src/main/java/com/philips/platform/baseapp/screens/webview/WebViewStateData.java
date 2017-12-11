package com.philips.platform.baseapp.screens.webview;

import com.philips.platform.appframework.flowmanager.base.UIStateData;

public class WebViewStateData extends UIStateData {

    private String url;

    private String serviceId;

    public void setUrl(String url){
        this.url=url;
    }

    public String getUrl(){
        return url;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}