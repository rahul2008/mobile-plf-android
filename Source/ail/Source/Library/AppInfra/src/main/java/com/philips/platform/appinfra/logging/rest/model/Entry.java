
package com.philips.platform.appinfra.logging.rest.model;

import com.google.gson.annotations.SerializedName;

public class Entry {

    @SerializedName("resource")
    private Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
