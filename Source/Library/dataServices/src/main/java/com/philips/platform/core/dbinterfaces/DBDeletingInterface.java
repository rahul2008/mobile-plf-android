/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;
@SuppressWarnings({"rawtypes", "unchecked"})
public interface DBDeletingInterface {
    void deleteAll(DBRequestListener dbRequestListener) throws SQLException;
    void markAsInActive(Moment moment,DBRequestListener<Moment> dbRequestListener) throws SQLException;
    void deleteMoment(Moment moment,DBRequestListener<Moment> dbRequestListener) throws  SQLException;
    boolean deleteMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException;
    void deleteMomentDetail(Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException;
    void deleteMeasurementGroup(Moment moment,DBRequestListener<Moment> dbRequestListener) throws SQLException;
    void deleteFailed(Exception e,DBRequestListener dbRequestListener);
    void deleteAllMoments(DBRequestListener<Moment> dbRequestListener) throws SQLException;
    void markMomentsAsInActive(final List<Moment> moment, DBRequestListener<Moment> dbRequestListener) throws SQLException;
    boolean markInsightsAsInActive(final List<Insight> insights ,DBRequestListener<Insight> dbRequestListener) throws SQLException;
    boolean deleteInsights(final List<Insight> insights ,DBRequestListener<Insight> dbRequestListener) throws SQLException;
    void deleteInsight(Insight insight, DBRequestListener<Insight> dbRequestListener) throws SQLException;

    int deleteSyncBit(SyncType syncType) throws SQLException;
    void deleteUserCharacteristics() throws SQLException;
}
