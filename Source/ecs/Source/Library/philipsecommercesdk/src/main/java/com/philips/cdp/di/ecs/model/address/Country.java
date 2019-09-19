/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.ecs.model.address;

import java.io.Serializable;

public class Country implements Serializable{
    private static final long serialVersionUID = 3239898965381617362L;
    
    private String isocode;
    private String name;

    public void setIsocode(String isocode) {
        this.isocode = isocode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;

    }

    public String getIsocode() {
        return isocode;
    }
}
