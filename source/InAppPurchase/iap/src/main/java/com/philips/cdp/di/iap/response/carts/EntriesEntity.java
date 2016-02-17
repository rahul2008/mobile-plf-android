package com.philips.cdp.di.iap.response.carts;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EntriesEntity {

    private BasePriceEntity basePrice;
    private int entryNumber;

    private ProductEntity product;
    private int quantity;

    private TotalPriceEntity totalPrice;
    private boolean updateable;

    public void setBasePrice(BasePriceEntity basePrice) {
        this.basePrice = basePrice;
    }

    public void setEntryNumber(int entryNumber) {
        this.entryNumber = entryNumber;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(TotalPriceEntity totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setUpdateable(boolean updateable) {
        this.updateable = updateable;
    }

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


}
