package com.philips.cdp.di.ecs.model.region;

import java.io.Serializable;

/**
 * The type Ecs region contains region name and code.
 * This object is returned for fetchRegions.
 */
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
