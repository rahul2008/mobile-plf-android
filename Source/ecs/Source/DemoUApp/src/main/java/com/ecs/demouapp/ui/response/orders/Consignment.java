package com.ecs.demouapp.ui.response.orders;


import java.util.List;

public class Consignment {
    private String code;

    private Address shippingAddress;
    private String status;
    private String statusDate;
    private List<ConsignmentEntries> entries;

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

    public List<ConsignmentEntries> getEntries() {
        return entries;
    }


}
