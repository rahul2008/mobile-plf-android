package com.ecs.demouapp.ui.response.orders;

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
