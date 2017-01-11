package com.philips.platform.core.dbinterfaces;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings("rawtypes")
public interface DBFetchingInterface {
    void fetchMoments() throws SQLException;

    void fetchMoments(final @NonNull String type) throws SQLException;

    void fetchMoments(final @NonNull Object... types) throws SQLException;

    void fetchLastMoment(final String type) throws SQLException;

    Object fetchMomentByGuid(@NonNull final String guid) throws SQLException;

    List<?> fetchNonSynchronizedMoments() throws SQLException;

    Object fetchMomentById(final int id) throws SQLException;

    Map<Class, List<?>> putMomentsForSync(final Map<Class, List<?>> dataToSync) throws SQLException;

    Map<Class, List<?>> putConsentForSync(final Map<Class, List<?>> dataToSync) throws SQLException;

    Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException;

    void fetchConsents() throws SQLException;

    Consent fetchConsent() throws SQLException;

    void fetchCharacteristics() throws SQLException;

    void postError(Exception e);
}
