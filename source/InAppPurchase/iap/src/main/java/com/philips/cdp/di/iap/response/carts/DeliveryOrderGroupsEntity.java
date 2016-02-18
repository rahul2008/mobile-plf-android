package com.philips.cdp.di.iap.response.carts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryOrderGroupsEntity {

    private Price totalPriceWithTax;

    private List<EntriesEntity> entries;

    public void setTotalPriceWithTax(Price totalPriceWithTax) {
        this.totalPriceWithTax = totalPriceWithTax;
    }

    public void setEntries(List<EntriesEntity> entries) {
        this.entries = entries;
    }

    public Price getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public List<EntriesEntity> getEntries() {
        return entries;
    }
}
