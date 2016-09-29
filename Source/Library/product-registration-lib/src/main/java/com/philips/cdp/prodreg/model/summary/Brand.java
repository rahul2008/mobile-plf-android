/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.model.summary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Brand implements Serializable {

    private static final long serialVersionUID = -9120576767607019891L;
    private String brandLogo;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The brandLogo
     */
    public String getBrandLogo() {
        return brandLogo;
    }

    /**
     * @param brandLogo The brandLogo
     */
    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
