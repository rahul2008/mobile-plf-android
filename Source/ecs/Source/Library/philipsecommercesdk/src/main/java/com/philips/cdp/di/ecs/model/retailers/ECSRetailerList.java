/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.retailers;

import java.io.Serializable;
import java.util.List;

/**
 * The type Ecs retailer list which contains the list of retailers selling the product
 * This object is returned when fetchRetailers
 */
public class ECSRetailerList implements Serializable {

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
