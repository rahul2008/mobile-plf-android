package com.philips.platform.core.dbinterfaces;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings("rawtypes")
public interface DBFetchingInterface {
    void fetchMoments(DBRequestListener dbRequestListener) throws SQLException;

    void fetchMoments(final @NonNull String type ,DBRequestListener dbRequestListener) throws SQLException;

    void fetchMoments(DBRequestListener dbRequestListener,final @NonNull Object... types) throws SQLException;

    void fetchLastMoment(final String type,DBRequestListener dbRequestListener) throws SQLException;

    Object fetchMomentByGuid(@NonNull final String guid) throws SQLException;

    List<?> fetchNonSynchronizedMoments() throws SQLException;

    Object fetchMomentById(final int id, DBRequestListener dbRequestListener) throws SQLException;

    void fetchConsents(DBRequestListener dbRequestListener) throws SQLException;

    Consent fetchConsent(DBRequestListener dbRequestListener) throws SQLException;

    void postError(Exception e, DBRequestListener dbRequestListener);

    List<?> fetchNonSyncConsentDetails() throws SQLException;

}
