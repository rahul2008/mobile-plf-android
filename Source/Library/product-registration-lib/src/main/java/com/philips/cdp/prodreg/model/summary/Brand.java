
package com.philips.cdp.prodreg.model.summary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Brand implements Serializable {

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
