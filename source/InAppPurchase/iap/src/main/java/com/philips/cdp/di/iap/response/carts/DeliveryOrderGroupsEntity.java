package com.philips.cdp.di.iap.response.carts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
class DeliveryOrderGroupsEntity {
    /**
     * currencyIso : USD
     * formattedValue : $1,702.97
     * priceType : BUY
     * value : 1702.97
     */

    private TotalPriceWithTaxEntity totalPriceWithTax;
    /**
     * basePrice : {"currencyIso":"USD","formattedValue":"$400.00","priceType":"BUY","value":400}
     * entryNumber : 0
     * product : {"availableForPickup":false,"categories":[{"code":"Tuscany_Campaign","url":"/Tuscany-Category/c/Tuscany_Campaign"}],"code":"HX9042/64","discountPrice":{"currencyIso":"USD","value":400},"purchasable":true,"stock":{"stockLevel":937,"stockLevelStatus":"inStock"},"url":"/Tuscany-Category//p/HX9042_64"}
     * quantity : 2
     * totalPrice : {"currencyIso":"USD","formattedValue":"$800.00","priceType":"BUY","value":800}
     * updateable : true
     */

    private List<EntriesEntity> entries;

    public TotalPriceWithTaxEntity getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public List<EntriesEntity> getEntries() {
        return entries;
    }




}

