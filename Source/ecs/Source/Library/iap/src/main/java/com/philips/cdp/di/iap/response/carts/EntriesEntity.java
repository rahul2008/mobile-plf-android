package com.philips.cdp.di.iap.response.carts;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EntriesEntity implements Serializable{


    private static final long serialVersionUID = 9115373408948680734L;
    private BasePriceEntity basePrice;
    private int entryNumber;

    private ProductEntity product;
    private int quantity;

    private TotalPriceEntity totalPrice;
    private boolean updateable;

    public BasePriceEntity getBasePrice() {
        return basePrice;
    }

    public int getEntryNumber() {
        return entryNumber;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public TotalPriceEntity getTotalPrice() {
        return totalPrice;
    }

    public boolean isUpdateable() {
        return updateable;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}