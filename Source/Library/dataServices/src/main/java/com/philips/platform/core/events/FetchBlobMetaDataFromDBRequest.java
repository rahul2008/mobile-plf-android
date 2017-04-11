/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.datasync.blob.BlobMetaData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class FetchBlobMetaDataFromDBRequest extends Event {

    private final DBFetchRequestListner<BlobMetaData> dbFetchRequestListner;
    private final String userID;

    public String getUserID() {
        return userID;
    }

    public FetchBlobMetaDataFromDBRequest(DBFetchRequestListner<BlobMetaData> dbFetchRequestListner, String userID) {
        this.dbFetchRequestListner = dbFetchRequestListner;
        this.userID = userID;
    }

    public DBFetchRequestListner<BlobMetaData> getDbFetchRequestListner() {
        return dbFetchRequestListner;
    }

}
