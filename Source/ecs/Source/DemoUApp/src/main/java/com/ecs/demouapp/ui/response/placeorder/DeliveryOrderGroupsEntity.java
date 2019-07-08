package com.ecs.demouapp.ui.response.placeorder;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryOrderGroupsEntity {
    private TotalPriceWithTaxEntity totalPriceWithTax;

    private List<EntriesEntity> entries;

    public TotalPriceWithTaxEntity getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public List<EntriesEntity> getEntries() {
        return entries;
    }
}
