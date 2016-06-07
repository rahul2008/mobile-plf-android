package com.philips.platform.appinfra.servicediscovery.model;

import java.util.ArrayList;

/**
 * Created by 310238114 on 6/7/2016.
 */
public class MatchByCountry {
    boolean available;
    String locale;
    ArrayList<Config> configs;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }



    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }


    public ArrayList<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(ArrayList<Config> configs) {
        this.configs = configs;
    }

}
