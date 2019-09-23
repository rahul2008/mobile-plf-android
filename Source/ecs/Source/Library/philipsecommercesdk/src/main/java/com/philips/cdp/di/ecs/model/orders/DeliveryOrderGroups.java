package com.philips.cdp.di.ecs.model.orders;

import java.io.Serializable;
import java.util.List;

public class DeliveryOrderGroups implements Serializable {

    private Cost totalPriceWithTax;

    private List<Entries> entries;

    public Cost getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public List<Entries> getEntries() {
        return entries;
    }

}
