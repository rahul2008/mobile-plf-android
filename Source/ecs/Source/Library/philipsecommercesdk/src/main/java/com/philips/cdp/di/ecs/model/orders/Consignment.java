package com.philips.cdp.di.ecs.model.orders;



import com.philips.cdp.di.ecs.model.address.ECSAddress;

import java.util.List;

public class Consignment {
    private String code;

    private ECSAddress shippingAddress;
    private String status;
    private String statusDate;
    private List<ConsignmentEntries> entries;

    public String getCode() {
        return code;
    }


    public ECSAddress getShippingAddress() {
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