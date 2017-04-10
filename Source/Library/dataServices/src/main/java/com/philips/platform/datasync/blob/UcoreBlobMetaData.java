package com.philips.platform.datasync.blob;

/**
 * Created by sangamesh on 10/04/17.
 */

public class UcoreBlobMetaData {

    String userId ="userId";
    String hsdpObjectId = "hsdpObjectID";
    String contentType = "contentType";
    String contentLength= "contentLength";
    String contentChecksum = "contentCheckSum";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHsdpObjectId() {
        return hsdpObjectId;
    }

    public void setHsdpObjectId(String hsdpObjectId) {
        this.hsdpObjectId = hsdpObjectId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentChecksum() {
        return contentChecksum;
    }

    public void setContentChecksum(String contentChecksum) {
        this.contentChecksum = contentChecksum;
    }

    public String getBlobVersion() {
        return blobVersion;
    }

    public void setBlobVersion(String blobVersion) {
        this.blobVersion = blobVersion;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(String lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    String blobVersion = "blobVersion";
    String creationTimestamp = "creationTimeStamp";
    String lastModifiedTimestamp = "lastModifiedTimestamp";
}
