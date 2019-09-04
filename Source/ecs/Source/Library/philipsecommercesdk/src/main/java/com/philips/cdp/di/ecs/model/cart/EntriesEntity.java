package com.philips.cdp.di.ecs.model.cart;

import android.support.annotation.VisibleForTesting;

import com.philips.cdp.di.ecs.model.products.Product;

import java.io.Serializable;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EntriesEntity implements Serializable{


    private static final long serialVersionUID = 9115373408948680734L;
    private BasePriceEntity basePrice;
    private int entryNumber;

    private Product product;
    private int quantity;

    private TotalPriceEntity totalPrice;
    private boolean updateable;

    public BasePriceEntity getBasePrice() {
        return basePrice;
    }

    public int getEntryNumber() {
        return entryNumber;
    }

    @VisibleForTesting
    public void setEntryNumber(int entryNumber) {
        this.entryNumber = entryNumber;
    }

    public Product getProduct() {
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

    public void setProduct(Product product) {
        this.product = product;
    }
}