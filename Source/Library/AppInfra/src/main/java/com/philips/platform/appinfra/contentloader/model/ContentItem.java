package com.philips.platform.appinfra.contentloader.model;

/**
 * Created by 310238114 on 11/8/2016.
 */
// each ContentItem represent a row in ContentTable
public class ContentItem {


    String Id;
    String serviceId;

    String tags;
    String rawData;
    long versionNumber;

   public ContentItem(){

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
}
