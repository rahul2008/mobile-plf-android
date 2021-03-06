/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.response.retailers;

import java.io.Serializable;

public class StoreEntity implements Serializable{

    private static final long serialVersionUID = -4549397314749988036L;
    private String name;
    private String availability;
    private String isPhilipsStore;
    private String philipsOnlinePrice;
    private int logoHeight;
    private int logoWidth;
    private String buyURL;
    private String logoURL;

    public String getName() {
        return name;
    }

    public String getAvailability() {
        return availability;
    }

    public String getIsPhilipsStore() {
        return isPhilipsStore;
    }

    public String getPhilipsOnlinePrice() {
        return philipsOnlinePrice;
    }

    public int getLogoHeight() {
        return logoHeight;
    }

    public int getLogoWidth() {
        return logoWidth;
    }

    public String getBuyURL() {
        return buyURL;
    }

    public String getLogoURL() {
        return logoURL;
    }
}
