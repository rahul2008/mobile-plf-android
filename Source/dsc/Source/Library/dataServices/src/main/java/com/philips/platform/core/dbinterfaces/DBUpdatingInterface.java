/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Base Updating Interfaces for implementing Update Operations on DB
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface DBUpdatingInterface {
    /**
     * Update the moment in Data-Base
     * @param ormMoment The Moment to be updated
     * @param dbRequestListener Callback for notifying the "updateMoment" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void updateMoment(final Moment ormMoment, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Batch Update operation on  list of moments
     * @param ormMoments The List of moments to be updated
     * @param dbRequestListener dbRequestListener Callback for notifying the "updateMoments" result
     * @return returns a boolean indicating weather the update operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean updateMoments(final List<Moment> ormMoments, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Updates the Consent Details in Data-Base
     * @param consentDetails List of consentDetails
     * @param dbRequestListener Callback for notifying the "updateConsent" result
     * @return returns a boolean indicating weather the update operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean updateConsent(final List<? extends ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) throws SQLException;

    /**
     * Notify The Propositions in case any Data-Base Operation failed
     * @param e The DataBaseException
     * @param dbRequestListener Callback for notifying the exception to the propositions
     */
    void updateFailed(Exception e, DBRequestListener dbRequestListener);

    /**
     * Updates the userCharacteristics in Data-Base
     * @param userCharacteristics The userCharacteristics Object to be updated
     * @param dbRequestListener dbRequestListener Callback for notifying the "updateCharacteristics" result
     * @return returns a boolean indicating weather the update operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean updateCharacteristics(final List<Characteristics> userCharacteristics, DBRequestListener<Characteristics> dbRequestListener) throws SQLException;

    /**
     * Updates the Settings Object
     * @param settings The Settings Object to be updated
     * @param dbRequestListener dbRequestListener dbRequestListener Callback for notifying the "updateSettings" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void updateSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) throws SQLException;

    /**
     * Updates the SyncBit in Data-Base
     * @param tableID The tableId for which the syncBit has to be updated
     * @param isSynced the data is synced or not. synced = true, not-synced = false
     * @return returns a boolean indicating weather the update operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean updateSyncBit(int tableID,boolean isSynced) throws SQLException;

    /**
     * Updates the insights in the Data-Base
     * @param insights The Insgiht Object to be updated
     * @param dbRequestListener dbRequestListener Callback for notifying the "updateInsights" result
     * @return returns a boolean indicating weather the update operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean updateInsights(final List<? extends Insight> insights ,DBRequestListener<Insight> dbRequestListener) throws SQLException;

}
