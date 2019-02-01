
package com.philips.platform.appinfra.logging.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CloudLogs {

    @SerializedName("entry")
    @Expose
    private List<Entry> entry = null;
    @SerializedName("productKey")
    @Expose
    private String productKey;
    @SerializedName("resourceType")
    @Expose
    private String resourceType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
