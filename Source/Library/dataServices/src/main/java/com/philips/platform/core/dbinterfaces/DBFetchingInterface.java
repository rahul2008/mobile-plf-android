package com.philips.platform.core.dbinterfaces;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.MomentType;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface DBFetchingInterface {
    void fetchMoments() throws SQLException;
    void fetchMoments(final @NonNull MomentType type) throws SQLException;
    void fetchMoments(final @NonNull MomentType... types) throws SQLException;
    void fetchLastMoment(final MomentType type) throws SQLException;
    Object fetchMomentByGuid(@NonNull final String guid) throws SQLException;
    List<?> fetchNonSynchronizedMoments() throws SQLException;
    Object fetchMomentById(final int id) throws SQLException;
    Map<Class, List<?>> putMomentsForSync(final Map<Class, List<?>> dataToSync) throws SQLException;

    void fetchConsents() throws SQLException;
    List<?> fetchConsentsToSync() throws SQLException;
    List<?> fetchConsentDetails() throws SQLException;
    List<?> fetchConsentDetailTypes() throws SQLException;
}
