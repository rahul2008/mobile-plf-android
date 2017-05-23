/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Base Deleting Interface for implementing Delete Operations on DB
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface DBDeletingInterface {
    /**
     * Delete All the Entries from All the table
     * @param dbRequestListener Callback for notifying the deleteAll result to the calling function
     * @throws SQLException throws exception if DataBase operation fails
     */
    void deleteAll(DBRequestListener dbRequestListener) throws SQLException;

    /**
     * Mark the Moment as InActive
     * This is called when Propositions delete the moment from UI. The Moment is first marked as inactive. One's the success response is received from server,
     * the Moment Entry is deleted from Data-Base
     * @param moment The Moment to be deleted
     * @param dbRequestListener Callback for notifying the delete result to the calling function
     * @throws SQLException throws exception if DataBase operation fails
     */
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
