/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.response.addresses;

import java.io.Serializable;

public class Country implements Serializable{
    private static final long serialVersionUID = 3239898965381617362L;
    
    private String isocode;
    private String name;

    public String getName() {
        return name;
    }

    public String getIsocode() {
        return isocode;
    }
}
