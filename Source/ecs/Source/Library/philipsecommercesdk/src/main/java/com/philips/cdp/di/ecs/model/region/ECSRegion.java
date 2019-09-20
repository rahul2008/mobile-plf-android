package com.philips.cdp.di.ecs.model.region;

import java.io.Serializable;

public class ECSRegion implements Serializable {
    private String isocode;
    private String name;

    public String getIsocode() {
        return isocode;
    }

    public String getName() {
        return name;
    }
}
