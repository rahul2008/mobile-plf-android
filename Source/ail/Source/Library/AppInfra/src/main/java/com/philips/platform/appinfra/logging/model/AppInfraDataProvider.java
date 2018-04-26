package com.philips.platform.appinfra.logging.model;


import com.philips.platform.appinfra.AppInfra;

import java.util.Date;

public class AppInfraDataProvider {
    private AppInfra appInfra;

    public String getHomeCountry() {
        homeCountry = appInfra.getServiceDiscovery().getHomeCountry();
        return homeCountry;
    }

    public String getLocale() {

        return locale;
    }

    public Date getLogTime() {
        logTime = appInfra.getTime().getUTCTime();
        return logTime;
    }

    public String homeCountry;
    public String locale;
    public Date logTime;

    AppInfraDataProvider(AppInfra appInfra){
        this.appInfra = appInfra;
    }



}
