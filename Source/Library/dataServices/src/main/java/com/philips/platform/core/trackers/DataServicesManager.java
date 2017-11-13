/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.core.trackers;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.CreateSubjectProfileRequestEvent;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.DeleteAllMomentsRequest;
import com.philips.platform.core.events.DeleteExpiredMomentRequest;
import com.philips.platform.core.events.DeleteInsightFromDB;
import com.philips.platform.core.events.DeleteSubjectProfileRequestEvent;
import com.philips.platform.core.events.FetchInsightsFromDB;
import com.philips.platform.core.events.GetPairedDeviceRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileListRequestEvent;
import com.philips.platform.core.events.GetSubjectProfileRequestEvent;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadLatestMomentByTypeRequest;
import com.philips.platform.core.events.LoadMomentsByDate;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.LoadUserCharacteristicsRequest;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.MomentsDeleteRequest;
import com.philips.platform.core.events.MomentsSaveRequest;
import com.philips.platform.core.events.MomentsUpdateRequest;
import com.philips.platform.core.events.PairDevicesRequestEvent;
import com.philips.platform.core.events.RegisterDeviceToken;
import com.philips.platform.core.events.UnPairDeviceRequestEvent;
import com.philips.platform.core.events.UnRegisterDeviceToken;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.injection.ApplicationModule;
import com.philips.platform.core.injection.BackendModule;
import com.philips.platform.core.injection.DaggerAppComponent;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.utils.DataServicesConstants;
import com.philips.platform.core.utils.EventingImpl;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.subjectProfile.UCoreCreateSubjectProfileRequest;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.synchronisation.SynchronisationMonitor;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The Public Interface for the Propositions for initializing and using Data-Services Component
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DataServicesManager {

    public static final String TAG = DataServicesManager.class.getName();

    //private Context mContext;

    private ServiceDiscoveryInterface mServiceDiscoveryInterface;
    private AppInfraInterface mAppInfraInterface;
    private AppConfigurationInterface.AppConfigurationError mAppConfigurationError;

    public String mDataServicesBaseUrl;
    public String mDataServicesCoachingServiceUrl;

    private DBChangeListener dbChangeListener;

    protected SynchronisationCompleteListener mSynchronisationCompleteListener;

    public DBChangeListener getDbChangeListener() {
        return dbChangeListener;
    }

    @Inject
    Eventing mEventing;

    @NonNull
    private static AppComponent mAppComponent;

    private DBDeletingInterface mDeletingInterface;
    private DBFetchingInterface mFetchingInterface;
    private DBSavingInterface mSavingInterface;
    private DBUpdatingInterface mUpdatingInterface;

    @Inject
    BaseAppDataCreator mDataCreater;

    @Inject
    UCoreAccessProvider mBackendIdProvider;

    @Inject
    BaseAppCore mCore;

    @Inject
    SynchronisationMonitor mSynchronisationMonitor;

    @Inject
    SynchronisationManager mSynchronisationManager;

    private static DataServicesManager sDataServicesManager;

    @Inject
    UserRegistrationInterface userRegistrationInterface;

    @Inject
    ErrorHandlingInterface errorHandlingInterface;

    private ArrayList<DataFetcher> mCustomFetchers;

    private ArrayList<DataSender> mCustomSenders;

    private Set<String> mSyncDataTypes;

    private AppInfraInterface mAppInfra;

    public AppInfraInterface getAppInfra() {
        return mAppInfra;
    }

    @Singleton
    private DataServicesManager() {
    }

    public static synchronized DataServicesManager getInstance() {
        if (sDataServicesManager == null) {
            return sDataServicesManager = new DataServicesManager();
        }
        return sDataServicesManager;
    }

    /**
     * Initialize the Data-Services module
     *
     * @param context                Application Context
     * @param creator                DatabaseHelper for creating DataBaseFields
     * @param facade                 UserRegistrationInterface implementation for getting User-Registration related things @{@link UserRegistrationInterface}
     * @param errorHandlingInterface ErrorHandlingInterface Implementation for handling sync errors @{@link ErrorHandlingInterface}
     */
    public void initializeDataServices(Context context, BaseAppDataCreator creator,
                                       UserRegistrationInterface facade, ErrorHandlingInterface errorHandlingInterface ,AppInfraInterface mAppInfra) {
        //mContext = context;
        fetchUrlFromServiceDiscovery(context);

        this.mDataCreater = creator;
        this.userRegistrationInterface = facade;
        this.errorHandlingInterface = errorHandlingInterface;
        this.mAppInfra=mAppInfra;
    }

    @Deprecated
    @SuppressWarnings("rawtypes")
    public void initializeSyncMonitors(Context context, ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders, SynchronisationCompleteListener synchronisationCompleteListener) {
        initSyncMonitors(context, fetchers, senders, synchronisationCompleteListener);
    }

    /**
     * Initialize the Sync Monitors
     *
     * @param context  Application Context
     * @param fetchers List of custom fetchers from Propositions if applicable
     * @param senders  List of custom senders from Propositions if applicable
     */
    @SuppressWarnings("rawtypes")
    public void initializeSyncMonitors(Context context, ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders) {
        initSyncMonitors(context, fetchers, senders, null);
    }

    /**
     * Initialize the Sync Monitors
     *
     * @param context                         Application context
     * @param fetchers                        List of custom fetchers from Propositions if applicable
     * @param senders                         List of custom senders from Propositions if applicable
     * @param synchronisationCompleteListener Callback for sync complete notification @{@link SynchronisationCompleteListener}
     */
    private void initSyncMonitors(Context context, ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders, SynchronisationCompleteListener synchronisationCompleteListener) {
        this.mCustomFetchers = fetchers;
        this.mCustomSenders = senders;
        this.mSynchronisationCompleteListener = synchronisationCompleteListener;
        prepareInjectionsGraph(context);
        startMonitors();
    }

    /**
     * Initializing the Data-Base Monitors
     *
     * @param context           Application Context
     * @param deletingInterface DBDeletingInterfaceImplementation from propositions @{@link DBDeletingInterface}
     * @param fetchingInterface DBFetchingInterfaceImplementation from propositions @{@link DBFetchingInterface}
     * @param savingInterface   DBSavingInterfaceImplementation from propositions @{@link DBSavingInterface}
     * @param updatingInterface DBUpdatingInterfaceImplementation from propositions @{@link DBUpdatingInterface}
     */
    public void initializeDatabaseMonitor(Context context, DBDeletingInterface deletingInterface,
                                          DBFetchingInterface fetchingInterface, DBSavingInterface savingInterface, DBUpdatingInterface updatingInterface) {
        this.mDeletingInterface = deletingInterface;
        this.mFetchingInterface = fetchingInterface;
        this.mSavingInterface = savingInterface;
        this.mUpdatingInterface = updatingInterface;
    }


    /**
     * For Pulling and Pushing of data from DataBase to Backend
     */
    public void synchronize() {
        synchronized (this) {
            startMonitors();
            mSynchronisationManager.startSync(null, null, mSynchronisationCompleteListener);
        }
    }

    public void synchronizeWithFetchByDateRange(DateTime startDate, DateTime endDate, SynchronisationCompleteListener synchronisationCompleteListener) {
        synchronized (this) {
            startMonitors();
            mSynchronisationManager.startSync(startDate.toString(), endDate.toString(), synchronisationCompleteListener);
        }
    }

    /**
     * Start All the Event Monitors
     */
    private void startMonitors() {
        if (mCore != null) {
            mCore.start();
        }
        if (mSynchronisationMonitor != null) {
            mSynchronisationMonitor.start(mEventing);
        }
    }

    /**
     * @param fetchers configure the DataType for syncing of Data, Only the dataType mentioned in the list will be synced to and from the server
     */
    public void configureSyncDataType(Set<String> fetchers) {
        mSyncDataTypes = fetchers;
    }

    /**
     * Saves a Moment in the Data-Base
     *
     * @param moment            The Moment to be saved in the DataBase
     * @param dbRequestListener Callback for notifying DB save
     */
    @NonNull
    public void saveMoment(@NonNull final Moment moment, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentSaveRequest(moment, dbRequestListener));
    }

    /**
     * Saves List of Moments in the Data-Base
     *
     * @param moments           List of Moments for saving into the DB
     * @param dbRequestListener Callback for notifying success and failure of DataBase save
     */
    @NonNull
    public void saveMoments(@NonNull final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentsSaveRequest(moments, dbRequestListener));
    }

    /**
     * Fetch the Moment with the given type from Data-Base
     *
     * @param dbFetchRequestListner Callback for notifying the fetch result
     * @param type
     */
    public void fetchMomentWithType(DBFetchRequestListner<Moment> dbFetchRequestListner, final @NonNull String... type) {
        mEventing.post(new LoadMomentsRequest(dbFetchRequestListner, type));
    }

    /**
     * Fetch the Momenet with the given momentID
     *
     * @param momentID              Moment ID
     * @param dbFetchRequestListner Callback for notifying the fetch result
     */
    public void fetchMomentForMomentID(final int momentID, DBFetchRequestListner<Moment> dbFetchRequestListner) {
        mEventing.post(new LoadMomentsRequest(momentID, dbFetchRequestListner));
    }

    public void fetchLatestMomentByType(final @NonNull String type, DBFetchRequestListner<Moment> dbFetchRequestListener) {
        mEventing.post(new LoadLatestMomentByTypeRequest(type, dbFetchRequestListener));
    }

    /**
     * Fetch All the Moments from the Data-Base
     *
     * @param dbFetchRequestListner Callback for notifying the fetch result
     */
    public void fetchAllMoment(DBFetchRequestListner<Moment> dbFetchRequestListner) {
        mEventing.post(new LoadMomentsRequest(dbFetchRequestListner));
    }

    public void fetchMomentsWithTimeLine(Date startDate, Date endDate, DSPagination dsPagination, DBFetchRequestListner<Moment> dbFetchRequestListener) {
        mEventing.post(new LoadMomentsByDate(startDate, endDate, dsPagination, dbFetchRequestListener));
    }

    public void fetchMomentsWithTypeAndTimeLine(String momentType, Date startDate, Date endDate, DSPagination dsPagination, DBFetchRequestListner<Moment> dbFetchRequestListener) {
        mEventing.post(new LoadMomentsByDate(momentType, startDate, endDate, dsPagination, dbFetchRequestListener));
    }

    /**
     * Fetch the Consent Detail from Data-Base
     *
     * @param dbFetchRequestListner Callback for notifying the fetch result
     */
    @NonNull
    public void fetchConsentDetail(DBFetchRequestListner<ConsentDetail> dbFetchRequestListner) {
        mEventing.post(new LoadConsentsRequest(dbFetchRequestListner));
    }

    /**
     * Creates the ConsentDetail DataBase Object
     *
     * @param detailType                 Type of the Consent
     * @param consentDetailStatusType    Status of the consent (Accepted/refused)
     * @param documentVersion            document version (default: draft)
     * @param deviceIdentificationNumber deviceIdentificationNumber (default:manual)
     * @return
     */
    public ConsentDetail createConsentDetail(@NonNull final String detailType, final ConsentDetailStatusType consentDetailStatusType, String documentVersion, final String deviceIdentificationNumber) {
        return mDataCreater.createConsentDetail(detailType, consentDetailStatusType.getDescription(), documentVersion, deviceIdentificationNumber);
    }

    /**
     * Save ConsentDetails to Data-Base
     *
     * @param consentDetails    List of Consent Details
     * @param dbRequestListener Callback for notifying the save result
     */
    public void saveConsentDetails(List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) {
        mEventing.post(new DatabaseConsentSaveRequest(consentDetails, dbRequestListener));
    }

    /**
     * Update the consent details in Data-Base
     *
     * @param consentDetails    List of Consent Details
     * @param dbRequestListener Callback for notifying the update result
     */
    public void updateConsentDetails(List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) {
        mEventing.post(new DatabaseConsentUpdateRequest(consentDetails, dbRequestListener));
    }

    /**
     * Creates the User Setting Data-Base Object
     *
     * @param locale locale of the User
     * @param unit   unitsystem of User choice
     * @return returns a Setting Data-Base Object
     */
    public Settings createUserSettings(String locale, String unit) {
        Settings settings = mDataCreater.createSettings(unit, locale);
        return settings;
    }

    /**
     * Save User Settings Object to the Data-Base
     *
     * @param settings          Saves the Settings Object to the Data-Base
     * @param dbRequestListener Callback for notifying the save result
     */
    public void saveUserSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) {
        mEventing.post(new DatabaseSettingsSaveRequest(settings, dbRequestListener));
    }

    /**
     * Update the User Setting Object
     *
     * @param settings          The Setting Object to be updated
     * @param dbRequestListener Callback for notifying the update result
     */
    public void updateUserSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) {
        mEventing.post(new DatabaseSettingsUpdateRequest(settings, dbRequestListener));
    }

    /**
     * Creates a Moment Data-Base Object
     *
     * @param type Type of the Moment
     * @return returns a Moment Object created
     */
    @NonNull
    public Moment createMoment(@NonNull final String type) {
        return mDataCreater.createMoment(mBackendIdProvider.getUserId(), mBackendIdProvider.getSubjectId(), type, null);
    }

    /**
     * Delete expired moments and associated sub-entities
     */
    public void clearExpiredMoments(DBRequestListener<Integer> dbRequestListener) {
        mEventing.post(new DeleteExpiredMomentRequest(dbRequestListener));
    }

    /**
     * Creates a MeasurementGroup Object
     *
     * @param moment The Moment Object to which the measurementGroup needs to be attached
     * @return returns a MeasurementGroup Object created
     */
    @NonNull
    public MeasurementGroup createMeasurementGroup(@NonNull final Moment moment) {
        return mDataCreater.createMeasurementGroup(moment);
    }

    /**
     * Creates a MomentDetail Data-Base Object
     *
     * @param type   Type of the MomentDetail
     * @param value  Value of the moment detail
     * @param moment The Moment to which the momentDetail needs to get attached
     * @return returns a MomentDetail Object created
     */
    @NonNull
    public MomentDetail createMomentDetail(@NonNull final String type, String value, @NonNull final Moment moment) {
        MomentDetail momentDetail = mDataCreater.createMomentDetail(type, moment);
        moment.addMomentDetail(momentDetail);
        momentDetail.setValue(value);
        return momentDetail;
    }

    /**
     * Craeted a Measurement Data-Base Object
     *
     * @param type             Type of the Measurement
     * @param value            Value of the measurement
     * @param unit             unit of the measurement
     * @param measurementGroup The MeasurementGroup to which the Measurement has to be attached
     * @return returns the Measurement Object created
     */
    @NonNull
    public Measurement createMeasurement(@NonNull final String type, String value, String unit, @NonNull final MeasurementGroup measurementGroup) {
        Measurement measurement = mDataCreater.createMeasurement(type, measurementGroup);
        measurement.setValue(value);
        //  measurement.setDateTime(DateTime.now());
        measurement.setUnit(unit);
        measurementGroup.addMeasurement(measurement);
        return measurement;
    }

    /**
     * Creates the MeasurementGroup Data-Base Object
     *
     * @param measurementGroup the MeasurementGroup Object to which the MeasurementGroup has to be attached.
     * @return The MeasurementGroup Object created
     */
    @NonNull
    public MeasurementGroup createMeasurementGroup(@NonNull final MeasurementGroup measurementGroup) {
        return mDataCreater.createMeasurementGroup(measurementGroup);
    }

    /**
     * Creates the MeasurementDetail Data-Base Object
     *
     * @param type        The type of the measurementDetail
     * @param value       value of the measurementDetail
     * @param measurement The Measurement Object to which the MeasurementDetail has to be attached
     * @return returns the MeasurementDetail Object created
     */
    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                     String value, @NonNull final Measurement measurement) {
        MeasurementDetail measurementDetail = mDataCreater.createMeasurementDetail(type, measurement);
        measurementDetail.setValue(value);
        measurement.addMeasurementDetail(measurementDetail);
        return measurementDetail;
    }

    /**
     * Delete the Moment from Data-Base
     *
     * @param moment            The Moment Object that has to be deleted from the table
     * @param dbRequestListener Callback for notifying the delete result
     */
    public void deleteMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentDeleteRequest(moment, dbRequestListener));
    }

    /**
     * Bach Delete of Moments
     *
     * @param moments           The List of moments to be deleted
     * @param dbRequestListener Callback for notifying the delete result
     */
    public void deleteMoments(final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentsDeleteRequest(moments, dbRequestListener));
    }

    /**
     * Update the Moment in the Data-Base
     *
     * @param moment            The Moment to be updated
     * @param dbRequestListener Callback for notifying the update result
     */
    public void updateMoment(Moment moment, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post((new MomentUpdateRequest(moment, dbRequestListener)));
    }

    /**
     * Batch Update of Moments
     *
     * @param moments           List of Moments to be Updated
     * @param dbRequestListener Callback for notifying the update result
     */
    public void updateMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post((new MomentsUpdateRequest(moments, dbRequestListener)));
    }

    /**
     * Delete All the Entries from all the tables
     *
     * @param dbRequestListener Callback for notifying the DataClearRequest result
     */
    public void deleteAll(DBRequestListener dbRequestListener) {
        mEventing.post(new DataClearRequest(dbRequestListener));
    }

    /**
     * Delete All the moments from the Data-Base
     *
     * @param dbRequestListener CallBack for notifying the delete result
     */
    public void deleteAllMoments(DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new DeleteAllMomentsRequest(dbRequestListener));
    }

    private void prepareInjectionsGraph(Context context) {
        BackendModule backendModule = new BackendModule(new EventingImpl(new EventBus(), new Handler()), mDataCreater, userRegistrationInterface,
                mDeletingInterface, mFetchingInterface, mSavingInterface, mUpdatingInterface,
                mCustomFetchers, mCustomSenders, errorHandlingInterface);
        final ApplicationModule applicationModule = new ApplicationModule(context);

        mAppComponent = DaggerAppComponent.builder().backendModule(backendModule).applicationModule(applicationModule).build();
        mAppComponent.injectApplication(this);
    }

    /**
     * Stop the Event Monitors
     */
    public void stopCore() {
        synchronized (this) {
            if (mCore != null)
                mCore.stop();
            if (mSynchronisationMonitor != null)
                mSynchronisationMonitor.stop();

            mSynchronisationManager.stopSync();
        }
    }

    /**
     * Creates a MeasurementGroupDetail Data-Base Object
     *
     * @param type              Type of the MeasurementGroupDetail
     * @param value             Value of the MeasurementGroupDetail
     * @param mMeasurementGroup The measurementGroup to which the MeasurementGroupDetail has to be attached
     * @return
     */
    public MeasurementGroupDetail createMeasurementGroupDetail(String type, String value, MeasurementGroup mMeasurementGroup) {
        MeasurementGroupDetail measurementGroupDetail = mDataCreater.createMeasurementGroupDetail(type, mMeasurementGroup);
        measurementGroupDetail.setValue(value);
        mMeasurementGroup.addMeasurementGroupDetail(measurementGroupDetail);
        return measurementGroupDetail;
    }

    /**
     * Update the UserCharacteristics Object in Data-Base
     *
     * @param characteristicses List of UserCharacteristics Objects
     * @param dbRequestListener Callback for notifying the update result
     */
    public void updateUserCharacteristics(List<Characteristics> characteristicses, DBRequestListener<Characteristics> dbRequestListener) {
        mEventing.post(new UserCharacteristicsSaveRequest(characteristicses, dbRequestListener));
    }

    /**
     * Save the UserCharacteristics Object in the Data-Base
     *
     * @param characteristicses UserCharacteristics Object to be saved
     * @param dbRequestListener Callback for notifying the save result
     */
    public void saveUserCharacteristics(List<Characteristics> characteristicses, DBRequestListener<Characteristics> dbRequestListener) {
        mEventing.post(new UserCharacteristicsSaveRequest(characteristicses, dbRequestListener));
    }

    /**
     * Fetch the UserCharacteristics from Data-Base
     *
     * @param dbFetchRequestListner Callback for notifying the fetch result from Data-Base
     */
    public void fetchUserCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) {
        mEventing.post(new LoadUserCharacteristicsRequest(dbFetchRequestListner));
    }

    /**
     * Create UserCharacteristics Data-Base Object
     *
     * @param detailType      The Type of UserCharacteristics
     * @param detailValue     Value of the UserCharacteristics
     * @param characteristics The UserCharacteristics object to which the UserCharacteristics Object has to be attached
     * @return
     */
    public Characteristics createUserCharacteristics(@NonNull final String detailType, @NonNull final String detailValue, Characteristics characteristics) {

        Characteristics chDetail;
        if (characteristics != null) {
            chDetail = mDataCreater.createCharacteristics(detailType, detailValue, characteristics);
        } else {
            chDetail = mDataCreater.createCharacteristics(detailType, detailValue);
        }
        return chDetail;
    }

    /**
     * Register the Listener for getting CallBacks on DBChange
     * The DBChange Event is sent to the Propositions only when the Data-Base changes due to change in Data on Server
     *
     * @param dbChangeListener Callback to be registered with Library
     */
    public void registerDBChangeListener(DBChangeListener dbChangeListener) {
        this.dbChangeListener = dbChangeListener;
    }

    /**
     * UnRegister the Listener for receiving the notifications due to Data-Base change
     * The Propositions will not receive any callbacks in case they unregister the DBChangeListener
     */
    public void unRegisterDBChangeListener() {
        this.dbChangeListener = null;
    }

    /**
     * Register the Listener for getting the Synchronisation status from Library (success/Fail)
     *
     * @param synchronisationCompleteListener Callback for receiving the Synchronisation status from Library
     */
    public void registerSynchronisationCompleteListener(SynchronisationCompleteListener synchronisationCompleteListener) {
        this.mSynchronisationCompleteListener = synchronisationCompleteListener;
    }

    /**
     * Unregister the SynchronisationCompleteListener from Library
     * Propositions will not get the SynchronisationComplete status in case they unregister the SynchronisationCompleteListener
     */
    public void unRegisterSynchronisationCosmpleteListener() {
        this.mSynchronisationCompleteListener = null;
    }

    /**
     * Fetch UserSettings Object from Data-Base
     *
     * @param dbFetchRequestListner Callback for notifying the fetch result
     */
    public void fetchUserSettings(DBFetchRequestListner<Settings> dbFetchRequestListner) {
        mEventing.post(new LoadSettingsRequest(dbFetchRequestListner));
    }

    /**
     * This is Used for Dagger Injections within Library - not to be used by Propositions
     *
     * @return retuns an AppComponant Object
     */
    public AppComponent getAppComponant() {
        return mAppComponent;
    }

    /**
     * This is Used for Setting the Mock AppComponant for writing test cases - Not to be used by Propositions
     *
     * @param appComponent returns the AppComponant Object
     */
    public void setAppComponant(AppComponent appComponent) {
        mAppComponent = appComponent;
    }

    /**
     * Get the sync DataTypes configured by the Propositions
     *
     * @return retuns the List of DataTypes Configured by Propositions
     */
    public Set<String> getSyncTypes() {
        return mSyncDataTypes;
    }

    /**
     * Returns the List of CustomFetchers sent by Propositions during initialization
     *
     * @return returns the List of CustomFetchers sent by Propositions during initialization
     */
    public ArrayList<DataFetcher> getCustomFetchers() {
        return mCustomFetchers;
    }

    /**
     * Returns the List of CustomSenders sent by Propositions during initialization
     *
     * @return returns the List of CustomSenders sent by Propositions during initialization
     */
    public ArrayList<DataSender> getCustomSenders() {
        return mCustomSenders;
    }

    //Insight

    /**
     * Fetch the Insights from Data-Base
     *
     * @param dbFetchRequestListner CallBack for notifying the result
     */
    public void fetchInsights(DBFetchRequestListner dbFetchRequestListner) {
        mEventing.post(new FetchInsightsFromDB(dbFetchRequestListner));
    }

    /**
     * Delete Insights from Data-Base
     *
     * @param insights          The Insight Data-Object that has to be deleted
     * @param dbRequestListener Callback for notifying the delete result
     */
    public void deleteInsights(List<? extends Insight> insights, DBRequestListener<Insight> dbRequestListener) {
        mEventing.post(new DeleteInsightFromDB((List<Insight>) insights, dbRequestListener));
    }

    //Push Notification

    /**
     * Register the device token for receiving the push notification
     *
     * @param deviceToken                 deviceToken
     * @param appVariant                  A type of Device (Ex: RAP-ANDROID)
     * @param protocolProvider            The Protocol provider (Ex: Push.Gcma)
     * @param registerDeviceTokenListener Callback for notifying the register result
     */
    public void registerDeviceToken(String deviceToken, String appVariant, String protocolProvider, RegisterDeviceTokenListener registerDeviceTokenListener) {
        mEventing.post(new RegisterDeviceToken(deviceToken, appVariant, protocolProvider, registerDeviceTokenListener));
    }

    /**
     * UnRegister the device token form receiving the push notification
     *
     * @param appToken                    DeviceToken
     * @param appVariant                  A type of Device (Ex: RAP-ANDROID)
     * @param registerDeviceTokenListener Callback for notifying the unregister result
     */
    public void unRegisterDeviceToken(String appToken, String appVariant, RegisterDeviceTokenListener registerDeviceTokenListener) {
        mEventing.post(new UnRegisterDeviceToken(appToken, appVariant, registerDeviceTokenListener));
    }

    /**
     * Pulling the Data from Server, followed by pushing the data to server
     *
     * @param jsonObject Propositions will send the json information for registering the token
     * @throws JSONException throws JSONException in case invalid json is sent
     */
    public void handlePushNotificationPayload(JSONObject jsonObject) throws JSONException {
        synchronize();
    }

    //Create Subject Profile
    public void createSubjectProfile(String firstName, String dateOfBirth, String gender,
                                     double weight, String creationDate, SubjectProfileListener subjectProfileListener) {
        UCoreCreateSubjectProfileRequest uCoreCreateSubjectProfileRequest = new UCoreCreateSubjectProfileRequest();
        uCoreCreateSubjectProfileRequest.setFirstName(firstName);
        uCoreCreateSubjectProfileRequest.setDateOfBirth(dateOfBirth);
        uCoreCreateSubjectProfileRequest.setGender(gender);
        uCoreCreateSubjectProfileRequest.setWeight(weight);
        uCoreCreateSubjectProfileRequest.setCreationDate(creationDate);
        mEventing.post(new CreateSubjectProfileRequestEvent(uCoreCreateSubjectProfileRequest, subjectProfileListener));
    }

    public void getSubjectProfiles(SubjectProfileListener subjectProfileListener) {
        mEventing.post(new GetSubjectProfileListRequestEvent(subjectProfileListener));
    }

    public void getSubjectProfile(String subjectID, SubjectProfileListener subjectProfileListener) {
        mEventing.post(new GetSubjectProfileRequestEvent(subjectID, subjectProfileListener));
    }

    public void deleteSubjectProfile(String subjectID, SubjectProfileListener subjectProfileListener) {
        mEventing.post(new DeleteSubjectProfileRequestEvent(subjectID, subjectProfileListener));
    }

    //Device Pairing
    public void pairDevices(String deviceID, String deviceType, List<String> subjectIds,
                            List<String> standardObservationNames, String relationshipType, DevicePairingListener devicePairingListener) {
        mEventing.post(new PairDevicesRequestEvent(deviceID, deviceType, standardObservationNames, subjectIds, relationshipType, devicePairingListener));
    }

    public void unPairDevice(String deviceID, DevicePairingListener devicePairingListener) {
        mEventing.post(new UnPairDeviceRequestEvent(deviceID, devicePairingListener));
    }

    public void getPairedDevices(DevicePairingListener devicePairingListener) {
        mEventing.post(new GetPairedDeviceRequestEvent(devicePairingListener));
    }

    //Service Discovery
    protected void fetchUrlFromServiceDiscovery(Context context) {
        mAppInfraInterface = new AppInfra.Builder().build(context);
        mServiceDiscoveryInterface = mAppInfraInterface.getServiceDiscovery();
        mAppConfigurationError = new AppConfigurationInterface.AppConfigurationError();

        //fetchBaseUrlFromServiceDiscovery();
        //fetchCoachingServiceUrlFromServiceDiscovery();
    }

    /**
     * Fetches the Data-Service Base Url from Service Discovery
     *
     * @return returns the Base Url
     */
    public String fetchBaseUrlFromServiceDiscovery() {
        if (mDataServicesBaseUrl != null) {
            return mDataServicesBaseUrl;
        }

        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(DataServicesConstants.BASE_URL_KEY, new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        errorHandlingInterface.onServiceDiscoveryError(s);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        if (url.toString().isEmpty()) {
                            errorHandlingInterface.onServiceDiscoveryError("Empty Url from Service discovery");
                        } else {
                            mDataServicesBaseUrl = url.toString();
                        }
                    }
                });
        return mDataServicesBaseUrl;
    }

    /**
     * Fetches the coaching service URL form Service Discovery
     *
     * @return returns a coaching service URL
     */
    public String fetchCoachingServiceUrlFromServiceDiscovery() {
        if (mDataServicesCoachingServiceUrl != null)
            return mDataServicesCoachingServiceUrl;

        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(DataServicesConstants.COACHING_SERVICE_URL_KEY, new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        errorHandlingInterface.onServiceDiscoveryError(s);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        if (url.toString().isEmpty()) {
                            errorHandlingInterface.onServiceDiscoveryError("Empty Url from Service discovery");
                        } else {
                            mDataServicesCoachingServiceUrl = url.toString();
                        }
                    }
                });
        return mDataServicesCoachingServiceUrl;
    }

    /**
     * Used for setting a Mock ServiceDiscoveryInterface for writing test cases
     * Should not be used by Propositions
     *
     * @param serviceDiscoveryInterface
     */
    public void setServiceDiscoveryInterface(final ServiceDiscoveryInterface serviceDiscoveryInterface) {
        this.mServiceDiscoveryInterface = serviceDiscoveryInterface;
    }
}
