/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.retailers;

import java.util.List;

public class ECSRetailerList {

    private WrbresultsEntity wrbresults;

    public WrbresultsEntity getWrbresults() {
        return wrbresults;
    }

    List<ECSRetailer> getRetailers() {
        if (getWrbresults() != null && getWrbresults().getOnlineStoresForProduct() != null
                && getWrbresults().getOnlineStoresForProduct().getStores() != null && getWrbresults().getOnlineStoresForProduct().getStores().getRetailerList() != null) {

            return getWrbresults().getOnlineStoresForProduct().getStores().getRetailerList();
        }
        return null;
    }
}
