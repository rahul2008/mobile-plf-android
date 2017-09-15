package com.philips.platform.appinfra.contentloader.model;

/**
 * The generic class model for all find of content.
 */
// each ContentItem represent a row in ContentTable
//this is a generic class model for all find of content ie contentArticle etc
public class ContentItem {

    private String Id;
    private String serviceId;

    private String tags;
    private String rawData;
    private long versionNumber;
    private long lastUpdatedTime;

    public ContentItem() {

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
