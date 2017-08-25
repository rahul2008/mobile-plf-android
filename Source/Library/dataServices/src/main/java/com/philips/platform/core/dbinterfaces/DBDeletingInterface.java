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
 * Data Base Deleting Interfaces for implementing Delete Operations on DB
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface DBDeletingInterface {
    /**
     * Delete All the Entries from All the tables
     * @param dbRequestListener Callback for notifying the deleteAll result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void deleteAll(DBRequestListener dbRequestListener) throws SQLException;

    /**
     * Mark the Moment as InActive.
     * While the propositions request for delete moment,
     * The Moment is first marked as inactive. One's the success response is received from server,
     * the Moment Entry is deleted from Data-Base
     * @param moment The Moment to be deleted
     * @param dbRequestListener Callback for notifying the delete result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void markAsInActive(Moment moment,DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Delete the Moment from Data-Base
     * @param moment The Moment to be deleted from Data-Base
     * @param dbRequestListener Callback for notifying the delete result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void deleteMoment(Moment moment,DBRequestListener<Moment> dbRequestListener) throws  SQLException;

    /**
     * Delete the list of moments from the DataBase
     * @param moments List of moments to be deleted
     * @param dbRequestListener Callback for notifying the deleteAllMoments
     * @return returns weather the DB operation was success (true) or failure (false)
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean deleteMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Delete all the momentDetails related to a particular moment
     * @param moment Moment Object from which momentDetails have to be deleted
     * @param dbRequestListener Callback for notifying the "deleteMomentDetail" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void deleteMomentDetail(Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Delete All the measurementGroups related to a particular moment
     * @param moment Moment Object from which measurementGroup have to be deleted
     * @param dbRequestListener Callback for notifying the "deleteMeasurementGroup" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void deleteMeasurementGroup(Moment moment,DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Notify The Propositions in case any Data-Base Operation failed
     * @param e The DataBaseException
     * @param dbRequestListener Callback for notifying the exception to the propositions
     */
    void deleteFailed(Exception e,DBRequestListener dbRequestListener);

    /**
     * Delete All the Moments from the Data-Base
     * @param dbRequestListener Callback for notifying the "deleteAll" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void deleteAllMoments(DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Delete all expired moments and associated sub-entities
     * @param dbRequestListener Callback for notifying the deleteAllExpiredMoments
     * @return returns number of affected rows
     * @throws SQLException throws exception if DataBase operation fails
     */
    void deleteAllExpiredMoments(DBRequestListener<Integer> dbRequestListener) throws SQLException;

    /**
     * Batch Operation for deleting more than one moment
     * When Propositions requests for delete, first the moments are marked as inActive
     * One's the Data gets synced with the cloud, that particular moment is deleted from Data-Base
     * @param moment List of moments to be deleted
     * @param dbRequestListener Callback for notifying the "deleteBatch" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void markMomentsAsInActive(final List<Moment> moment, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Batch Operation for deleting the Insights
     * When Propositions requests for delete, first the insights are marked as inActive
     * One's the Data gets synced with the cloud, that particular insight is deleted from Data-Base
     * @param insights The List of Insights to be deleted
     * @param dbRequestListener Callback for notifying the "deleteBatch" result
     * @return returns a boolean indicating the database operation fail/success
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean markInsightsAsInActive(final List<Insight> insights ,DBRequestListener<Insight> dbRequestListener) throws SQLException;

    /**
     * Delete the List of Insights from Data-Base
     * @param insights List of Insights to be deleted from Data-Base
     * @param dbRequestListener Callback for notifying the "deleteInsights" result
     * @return returns a boolean indicating the database operation fail/success
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean deleteInsights(final List<Insight> insights ,DBRequestListener<Insight> dbRequestListener) throws SQLException;

    /**
     * Delete the Insight from Data-Base
     * @param insight The Insight Object that has to be deleted from the Data-Base
     * @param dbRequestListener Callback for notifying the "deleteInsight" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void deleteInsight(Insight insight, DBRequestListener<Insight> dbRequestListener) throws SQLException;

    /**
     * Delete SyncBit Object from DataBase
     * @param syncType The SyncType Object to be deleted
     * @return returns a boolean indicating the database operation fail/success
     * @throws SQLException throws exception if DataBase operation fails
     */
    int deleteSyncBit(SyncType syncType) throws SQLException;

    /**
     * Delete UserCharacteristics from Data-Base
     * @throws SQLException throws exception if DataBase operation fails
     */
    void deleteUserCharacteristics() throws SQLException;
}
