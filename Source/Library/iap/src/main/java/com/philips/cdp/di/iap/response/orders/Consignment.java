package com.philips.cdp.di.iap.response.orders;


import java.util.List;

public class Consignment {
    private String code;

    private Address shippingAddress;
    private String status;
    private String statusDate;
//    private String trackingID;

    private List<Entries> entries;

    public String getCode() {
        return code;
    }


    public Address getShippingAddress() {
        return shippingAddress;
    }


    public String getStatus() {
        return status;
    }


    public String getStatusDate() {
        return statusDate;
    }


//    public String getTrackingID() {
//        return trackingID;
//    }


    public List<Entries> getEntries() {
        return entries;
    }


}
