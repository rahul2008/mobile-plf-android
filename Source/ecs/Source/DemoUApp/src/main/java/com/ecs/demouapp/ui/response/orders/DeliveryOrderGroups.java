package com.ecs.demouapp.ui.response.orders;

import com.philips.cdp.di.ecs.model.orders.Entries;

import java.util.List;

public class DeliveryOrderGroups {

    private Cost totalPriceWithTax;

    private List<Entries> entries;

    public Cost getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public List<Entries> getEntries() {
        return entries;
    }

}
