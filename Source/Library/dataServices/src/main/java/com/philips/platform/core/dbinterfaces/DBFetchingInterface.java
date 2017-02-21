package com.philips.platform.core.dbinterfaces;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBFetchRequestListner;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings("rawtypes")
public interface DBFetchingInterface {
    void fetchMoments(DBFetchRequestListner dbFetchRequestListner) throws SQLException;

    void fetchMoments(final @NonNull String type ,DBFetchRequestListner dbFetchRequestListner) throws SQLException;

    void fetchMoments(DBFetchRequestListner dbFetchRequestListner,final @NonNull Object... types) throws SQLException;

    void fetchLastMoment(final String type,DBFetchRequestListner dbFetchRequestListner) throws SQLException;

    Object fetchMomentByGuid(@NonNull final String guid) throws SQLException;

    List<?> fetchNonSynchronizedMoments() throws SQLException;

    //TODO: Can the fetchNonSynchronized data types be called once for all the datatypes.

    Object fetchMomentById(final int id, DBFetchRequestListner dbFetchRequestListner) throws SQLException;

    void fetchConsentDetails(DBFetchRequestListner dbFetchRequestListner) throws SQLException;

    Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException;

    List<?> fetchNonSyncConsentDetails() throws SQLException;

    List<?> fetchConsentDetails() throws SQLException;

    void postError(Exception e, DBFetchRequestListner dbFetchRequestListner);

    void fetchUserCharacteristics(DBFetchRequestListner dbFetchRequestListner) throws SQLException;

    void fetchCharacteristics(DBFetchRequestListner dbFetchRequestListner) throws SQLException;

    Settings fetchSettings(DBFetchRequestListner dbFetchRequestListner) throws SQLException;

    Settings fetchSettings() throws SQLException;

    List<?> fetchNonSyncSettings() throws SQLException;

    boolean isSynced(int tableID) throws SQLException;
}