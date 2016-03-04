package com.philips.cdp.backend;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegRequestInfo {

    private String ctn, serialNumber;
    private Sector sector;
    private Catalog catalog;

    public ProdRegRequestInfo(String ctn, String serialNumber, Sector sector, Catalog catalog) {
        this.ctn = ctn;
        this.serialNumber = serialNumber;
        this.sector = sector;
        this.catalog = catalog;
    }

    public Sector getSector() {
        return sector;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public String getCtn() {
        return ctn;
    }

    public String getSerialNumber() {
        return serialNumber;
    }
}
