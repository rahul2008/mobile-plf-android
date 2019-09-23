package com.philips.cdp.di.ecs.model.orders;

import java.io.Serializable;

public class Promotion implements Serializable {
    private String code;
    private String description;
    private String endDate;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getEndDate() {
        return endDate;
    }
}
