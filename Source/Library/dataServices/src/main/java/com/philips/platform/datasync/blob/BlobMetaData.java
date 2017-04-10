package com.philips.platform.datasync.blob;

import com.philips.platform.core.datatypes.BaseAppData;

import java.io.Serializable;

/**
 * Created by philips on 4/5/17.
 */

public interface BlobMetaData extends BaseAppData, Serializable {

    String userId ="userId";
    String hsdpObjectId = "hsdpObjectID";
    String contentType = "contentType";
    String contentLength= "contentLength";
    String contentChecksum = "contentCheckSum";
    String blobVersion = "blobVersion";
    String creationTimestamp = "creationTimeStamp";
    String lastModifiedTimestamp = "lastModifiedTimestamp";

    String blobId="blobID";

    public String getBlobID();
    public void setBlobId(String blobId);
    public String getUserId();

    public void setUserId(String userId);

    public String getHsdpObjectId();

    public void setHsdpObjectId(String hsdpObjectId);

    public String getContentType();

    public void setContentType(String contentType);

    public String getContentLength();

    public void setContentLength(String contentLength);

    public String getContentChecksum();

    public void setContentChecksum(String contentChecksum);

    public String getBlobVersion();

    public void setBlobVersion(String blobVersion);

    public String getCreationTimestamp();

    public void setCreationTimestamp(String creationTimestamp);

    public String getLastModifiedTimestamp();

    public void setLastModifiedTimestamp(String lastModifiedTimestamp) ;

    void setId(int id);


}
