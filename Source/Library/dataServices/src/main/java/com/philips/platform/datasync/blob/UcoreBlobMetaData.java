package com.philips.platform.datasync.blob;

/**
 * Created by philips on 4/5/17.
 */

public class UcoreBlobMetaData {

    String userId;
    String hsdpObjectId;
    String contentType;
    String contentLength;
    String contentChecksum;
    String blobVersion;

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

    String creationTimestamp;
    String lastModifiedTimestamp;

}
