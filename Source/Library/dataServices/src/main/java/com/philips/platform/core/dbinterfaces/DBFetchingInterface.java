/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.dbinterfaces;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.DCSync;
import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBFetchRequestListner;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Data Base Fetching Interfaces for implementing Fetch Operations on DB
 */
@SuppressWarnings("rawtypes")
public interface DBFetchingInterface {
    /**
     * Fetches All the moments from the Data-Base
     *
     * @param dbFetchRequestListner Callback for notifying the "fetchMoments" result
     * @return returns the list of moments
     * @throws SQLException throws exception if DataBase operation fails
     */
    List<? extends Moment> fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException;

    /**
     * Fetch the Moments of a particular type
     *
     * @param type                  The Moment type
     * @param dbFetchRequestListner Callback for notifying the "fetchMoments" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void fetchMoments(final @NonNull String type, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException;

    /**
     * Fetch the Moments of a various types
     *
     * @param dbFetchRequestListner Callback for notifying the "fetchMoments" result
     * @param types                 The Moment type
     * @throws SQLException throws exception if DataBase operation fails
     */
    void fetchMoments(DBFetchRequestListner<Moment> dbFetchRequestListner, final @NonNull Object... types) throws SQLException;

    /**
     * Fetches the last created moment of a particular type
     *
     * @param type                  The moment type
     * @param dbFetchRequestListner Callback for notifying the "fetchLastMoment" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void fetchLastMoment(final String type, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException;

    /**
     * Fetches the last created moment of a particular type
     *
     * @param type                   The moment type
     * @param dbFetchRequestListener Callback to notify the fetch result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void fetchLatestMomentByType(final String type, DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException;


    void fetchMomentsWithTimeLine(final Date startDate, Date endDate, DSPagination paginationModel, DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException;

    void fetchMomentsWithTypeAndTimeLine(String momentType, Date startDate, Date endDate, DSPagination paginationModel, DBFetchRequestListner<Moment> dbFetchRequestListener) throws SQLException;

    /**
     * Fetches the Moment with the given guid
     *
     * @param guid The guid of Moment (The MomentID returned from server)
     * @return returns the moment object
     * @throws SQLException throws exception if DataBase operation fails
     */
    Object fetchMomentByGuid(@NonNull final String guid) throws SQLException;

    /**
     * Fetches the Moments that are not yet synchronized on the server
     *
     * @return returns list of moments that are not synchronized by server
     * @throws SQLException throws exception if DataBase operation fails
     */
    List<?> fetchNonSynchronizedMoments() throws SQLException;

    //TODO: Can the fetchNonSynchronized data types be called once for all the datatypes.

    /**
     * Fetches moment by momentID
     *
     * @param id                    The Moment ID
     * @param dbFetchRequestListner Callback for notifying the "fetchMomentByID" result
     * @return returns a moment object
     * @throws SQLException throws exception if DataBase operation fails
     */
    Object fetchMomentById(final int id, DBFetchRequestListner<Moment> dbFetchRequestListner) throws SQLException;

    /**
     * Fetch ConsentDetails from Data-Base
     *
     * @param dbFetchRequestListner Callback for notifying the "fetchConsentDetails" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void fetchConsentDetails(DBFetchRequestListner<ConsentDetail> dbFetchRequestListner) throws SQLException;

    /**
     * Fetch the Non Synchronized Consent Details
     *
     * @return returns list of ConsentDetails
     * @throws SQLException throws exception if DataBase operation fails
     */
    List<?> fetchNonSyncConsentDetails() throws SQLException;

    /**
     * Fetch ConsentDetails from DataBase
     *
     * @return returns list of consentDetails
     * @throws SQLException throws exception if DataBase operation fails
     */
    List<?> fetchConsentDetails() throws SQLException;

    /**
     * Fetch UserCharacteristics from Data-Base
     *
     * @param dbFetchRequestListner Callback for notifying the "fetchUserCharacteristics" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void fetchUserCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) throws SQLException;

    /**
     * Fetches the Non Synchronized UserCharacteristics Objects from Data-Base and stores it into a Map (dataToSync)
     * The Propositions should only modify the Map and do not create a new map
     *
     * @param dataToSync The Map who's value has to be modified
     * @return returns the map containing the key-value pair, where-in Key = Characteristics.class and value = List of None- Synchronized UserCharacteristics
     * @throws SQLException
     */
    Map<Class, List<?>> putUserCharacteristicsForSync(Map<Class, List<?>> dataToSync) throws SQLException;

    /**
     * Fetch UserCharacteristics from Data-Base
     *
     * @param dbFetchRequestListner dbFetchRequestListner Callback for notifying the "fetchCharacteristics" result
     * @throws SQLException throws exception if DataBase operation fails
     */
    void fetchCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) throws SQLException;

    /**
     * Fetch User Settings from DataBase
     *
     * @param dbFetchRequestListner Callback for notifying the "fetchSettings" result
     * @return returns the settings object
     * @throws SQLException throws exception if DataBase operation fails
     */
    Settings fetchSettings(DBFetchRequestListner<Settings> dbFetchRequestListner) throws SQLException;

    /**
     * Fetch User Settings from DataBase
     *
     * @return returns the settings object
     * @throws SQLException throws exception if DataBase operation fails
     */
    Settings fetchSettings() throws SQLException;

    /**
     * Fetch Non-Synchronized Settings
     *
     * @return List of User Setting Objects
     * @throws SQLException throws exception if DataBase operation fails
     */
    List<?> fetchNonSyncSettings() throws SQLException;

    /**
     * Checks weather the Data is synced or not with the given tableId
     *
     * @param tableID The Table ID
     * @return boolean indicating weather the data is synced or not. true = synced, false = not-synced
     * @throws SQLException throws exception if DataBase operation fails
     */
    boolean isSynced(int tableID) throws SQLException;

    /**
     * Fetch the DCSyncData from Data-Base
     *
     * @param syncType The Sync DataType
     * @return returns a DCSync type Object
     * @throws SQLException throws exception if DataBase operation fails
     */
    DCSync fetchDCSyncData(SyncType syncType) throws SQLException;

    /**
     * Fetch All the active Insights
     *
     * @param dbFetchRequestListner Callback for notifying the "fetchActiveInsights" result
     * @return returns list of active insights
     * @throws SQLException throws exception if DataBase operation fails
     */
    List<? extends Insight> fetchActiveInsights(DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException;

    /**
     * Fetch Insight by guid
     *
     * @param guid The insight guid (The Insight ID from server)
     * @return returns the Insight object
     * @throws SQLException throws exception if DataBase operation fails
     */
    Insight fetchInsightByGuid(@NonNull final String guid) throws SQLException;

    /**
     * Fetch Insight by Insight Id
     *
     * @param id                    The Insight row ID
     * @param dbFetchRequestListner Callback for notifying the "fetchInsightById" result
     * @return returns the Insight object
     * @throws SQLException throws exception if DataBase operation fails
     */
    Insight fetchInsightById(final int id, DBFetchRequestListner<Insight> dbFetchRequestListner) throws SQLException;

    /**
     * Fetch Non-Synchronized Insights from Data-Base
     *
     * @return returns the list of Non-Synchronized Insights
     * @throws SQLException throws exception if DataBase operation fails
     */
    List<?> fetchNonSynchronizedInsights() throws SQLException;

    /**
     * Notify The Propositions in case any Data-Base Operation failed
     *
     * @param e                     The DataBaseException
     * @param dbFetchRequestListner Callback for notifying the exception to the propositions
     */
    void postError(Exception e, DBFetchRequestListner dbFetchRequestListner);

}