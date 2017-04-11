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
public interface DBFetchingInterface {
    List<? extends Moment> fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException;

    void fetchMoments(final @NonNull String type ,DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException;

    void fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListner,final @NonNull Object... types) throws SQLException;

    void fetchLastMoment(final String type,DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException;

    Object fetchMomentByGuid(@NonNull final String guid) throws SQLException;

    List<?> fetchNonSynchronizedMoments() throws SQLException;

    //TODO: Can the fetchNonSynchronized data types be called once for all the datatypes.

    Object fetchMomentById(final int id, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException;

    void fetchConsentDetails(DBFetchRequestListner<ConsentDetail> dbFetchRequestListner) throws SQLException;

    Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException;

    List<?> fetchNonSyncConsentDetails() throws SQLException;

    List<?> fetchConsentDetails() throws SQLException;

    void postError(Exception e, DBFetchRequestListner dbFetchRequestListner);

    void fetchUserCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) throws SQLException;

    void fetchCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) throws SQLException;

    Settings fetchSettings(DBFetchRequestListner<Settings> dbFetchRequestListner) throws SQLException;

    Settings fetchSettings() throws SQLException;

    List<?> fetchNonSyncSettings() throws SQLException;

    boolean isSynced(int tableID) throws SQLException;

    List<? extends Insight> fetchActiveInsights(DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException;

    Insight fetchInsightByGuid(@NonNull final String guid) throws SQLException;

    Insight fetchInsightById(final int id, DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException;

    List<?> fetchNonSynchronizedInsights() throws SQLException;

    List<? extends BlobMetaData> fetchAllBlobMetaData(DBFetchRequestListner<BlobMetaData> dbFetchRequestListner) throws SQLException;

    BlobMetaData fetchBlobMetaDataByBlobID(String blobID) throws SQLException;
}