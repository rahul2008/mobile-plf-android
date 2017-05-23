/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.dbinterfaces;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Base Saving Interfaces for implementing Save Operations on DB
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface DBSavingInterface {
    /**
     * Save the created moment to Data-Base
     * @param moment moment to be saved
     * @param dbRequestListener Callback for notifying the "saveMoment" result
     * @return returns a boolean indicating weather the save operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean saveMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Batch Operation for Saving Moments
     * @param moments List of Moments to be saved
     * @param dbRequestListener Callback for notifying the "saveMoments" result
     * @return returns a boolean indicating weather the save operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean saveMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException;

    /**
     * Save ConsentDetails to Data-Base
     * @param consentDetails The List of ConsentDetails to be saved
     * @param dbRequestListener Callback for notifying the "saveConsentDetails" result
     * @return returns a boolean indicating weather the save operation was successful. success = true, fail = false
     * @throws SQLException SQLException throws exception if DataBase operation fails
     */
    boolean saveConsentDetails(final List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) throws SQLException;

    /**
     * Notify The Propositions in case any Data-Base Operation failed
     * @param e The DataBaseException
     * @param dbRequestListener Callback for notifying the exception to the propositions
     */
    void postError(Exception e, DBRequestListener dbRequestListener);

    /**
     * Save UserCharacteristics Data-Base object to DataBase
     * @param userCharacteristics UserCharacteristics Object to be saved in Data-Base
     * @param dbRequestListener Callback for notifying the "saveUserCharacteristics" result
     * @return returns a boolean indicating weather the save operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean saveUserCharacteristics(final List<Characteristics> userCharacteristics, DBRequestListener<Characteristics> dbRequestListener) throws SQLException;

    /**
     * Save Settings Object to Data-Base
     * @param settings Setting Object to be saved to Data-Base
     * @param dbRequestListener Callback for notifying the "saveUserCharacteristics" result
     * @return returns a boolean indicating weather the save operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean saveSettings(final Settings settings, DBRequestListener<Settings> dbRequestListener) throws SQLException;

    /**
     * Saves the Insight Object to the Data-Base
     * @param insights Insights Object to saved to DataBase
     * @param dbRequestListener Callback for notifying the "saveUserCharacteristics" result
     * @return returns a boolean indicating weather the save operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean saveInsights(final List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException;

    /**
     * Saves the DCSync Object to Database
     * @param type type of the sync data
     * @param isSynced was the data synced before. true = synced, false = not-synced
     * @return returns a boolean indicating weather the save operation was successful. success = true, fail = false
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean saveSyncBit(SyncType type,boolean isSynced) throws SQLException;
}
