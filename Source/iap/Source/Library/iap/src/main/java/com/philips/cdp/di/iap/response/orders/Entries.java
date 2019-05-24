package com.philips.cdp.di.iap.response.orders;

import java.util.List;

public class Entries {
    private int entryNumber;

    private Product product;
    private int quantity;

    private Cost totalPrice;
    private List<String> trackAndTraceIDs = null;


    public int getEntryNumber() {
        return entryNumber;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Cost getTotalPrice() {
        return totalPrice;
    }

    public List<String> getTrackAndTraceIDs() {
        return trackAndTraceIDs;
    }

    public void setTrackAndTraceIDs(List<String> trackAndTraceIDs) {
        this.trackAndTraceIDs = trackAndTraceIDs;
    }


}
