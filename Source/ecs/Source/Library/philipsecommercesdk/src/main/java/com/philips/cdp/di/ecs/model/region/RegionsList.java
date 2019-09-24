package com.philips.cdp.di.ecs.model.region;

import java.io.Serializable;
import java.util.List;

public class RegionsList implements Serializable {

    private List<ECSRegion> regions;

    public List<ECSRegion> getRegions() {
        return regions;
    }
}
