package cdp.philips.com.mydemoapp.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.philips.platform.datasync.blob.BlobMetaData;

import java.io.Serializable;

import cdp.philips.com.mydemoapp.database.annotations.DatabaseConstructor;

/**
 * Created by philips on 4/8/17.
 */

@DatabaseTable
public class OrmBlobMetaData implements BlobMetaData,Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true)
    private String userId;

    @DatabaseField(canBeNull = true)
    private String hsdpObjectId;

    @DatabaseField(canBeNull = true)
    private String contentChecksum;

    @DatabaseField(canBeNull = true)
    private String blobVersion;

    @DatabaseField(canBeNull = true)
    private String creationTimestamp;

    @DatabaseField(canBeNull = true)
    private String lastModifiedTimestamp;

    @DatabaseField(canBeNull = true)
    private String blobID;

    @DatabaseField(canBeNull = true)
    private String contentType;

    @DatabaseField(canBeNull = true)
    private String contentLength;

    @DatabaseConstructor
    public OrmBlobMetaData() {
    }

    public OrmBlobMetaData(int id, String userId, String hsdpObjectId,String contentType,String contentLength, String contentChecksum, String blobVersion, String creationTimestamp, String lastModifiedTimestamp, String blobID) {
        this.id = id;
        this.userId = userId;
        this.hsdpObjectId = hsdpObjectId;
        this.contentType=contentType;
        this.contentLength=contentLength;
        this.contentChecksum = contentChecksum;
        this.blobVersion = blobVersion;
        this.creationTimestamp = creationTimestamp;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        this.blobID = blobID;
    }


    @Override
    public String getBlobID() {
        return blobID;
    }

    @Override
    public void setBlobId(String blobId) {
     this.blobID=blobId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getHsdpObjectId() {
        return hsdpObjectId;
    }

    @Override
    public void setHsdpObjectId(String hsdpObjectId) {
        this.hsdpObjectId = hsdpObjectId;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType=contentType;
    }

    @Override
    public String getContentLength() {
        return contentLength;
    }

    @Override
    public void setContentLength(String contentLength) {
    this.contentLength=contentLength;
    }

    @Override
    public String getContentChecksum() {
        return contentChecksum;
    }

    @Override
    public void setContentChecksum(String contentChecksum) {
        this.contentChecksum = contentChecksum;
    }

    @Override
    public String getBlobVersion() {
        return blobVersion;
    }

    @Override
    public void setBlobVersion(String blobVersion) {
        this.blobVersion = blobVersion;
    }

    @Override
    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    @Override
    public void setLastModifiedTimestamp(String lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    @Override
    public void setId(int id) {
        this.id=id;
    }

    @Override
    public int getId() {
        return id;
    }
}

