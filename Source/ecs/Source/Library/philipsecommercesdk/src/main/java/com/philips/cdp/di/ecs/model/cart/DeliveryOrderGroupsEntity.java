package com.philips.cdp.di.ecs.model.cart;

import java.io.Serializable;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeliveryOrderGroupsEntity   implements Serializable {

    private TotalPriceWithTaxEntity totalPriceWithTax;

    private List<ECSEntries> entries;

    public TotalPriceWithTaxEntity getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public List<ECSEntries> getEntries() {
        return entries;
    }
}

