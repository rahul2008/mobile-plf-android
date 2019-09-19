package com.philips.cdp.di.ecs.model.orders;

import java.util.List;

public class ConsignmentEntries {
    private int entryNumber;

    private int quantity;

    private OrderEntry orderEntry;

    private Cost totalPrice;
    private List<String> trackAndTraceIDs;
    private List<String> trackAndTraceUrls;


    public int getEntryNumber() {
        return entryNumber;
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

    public List<String> getTrackAndTraceUrls() {
        return trackAndTraceUrls;
    }

    public OrderEntry getOrderEntry() {
        return orderEntry;
    }

    public void setOrderEntry(OrderEntry orderEntry) {
        this.orderEntry = orderEntry;
    }

    public void setTrackAndTraceIDs(List<String> trackAndTraceIDs) {
        this.trackAndTraceIDs = trackAndTraceIDs;
    }

    public void setTrackAndTraceUrls(List<String> trackAndTraceUrls) {
        this.trackAndTraceUrls = trackAndTraceUrls;
    }
}
