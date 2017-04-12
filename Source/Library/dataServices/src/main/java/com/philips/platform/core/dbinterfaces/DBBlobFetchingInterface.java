package com.philips.platform.core.dbinterfaces;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.datasync.blob.BlobMetaData;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings("rawtypes")
public interface DBBlobFetchingInterface {

    List<? extends BlobMetaData> fetchAllBlobMetaData(DBFetchRequestListner<BlobMetaData> dbFetchRequestListner, String userID) throws SQLException;

    BlobMetaData fetchBlobMetaDataByBlobID(String blobID) throws SQLException;
}