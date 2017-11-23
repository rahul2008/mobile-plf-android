/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.core.trackers;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
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
import com.philips.platform.core.utils.DataServicesLogger;
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

    private static AppComponent mAppComponent;

    private DBDeletingInterface mDeletingInterface;
    private DBFetchingInterface mFetchingInterface;
    private DBSavingInterface mSavingInterface;
    private DBUpdatingInterface mUpdatingInterface;

    ServiceDiscoveryInterface mServiceDiscoveryInterface;
    private AppInfraInterface mAppInfra;

    private ArrayList<DataFetcher> mCustomFetchers;
    private ArrayList<DataSender> mCustomSenders;
    private Set<String> mSyncDataTypes;

    public String mDataServicesBaseUrl;
    public String mDataServicesCoachingServiceUrl;

    private DBChangeListener dbChangeListener;
    SynchronisationCompleteListener mSynchronisationCompleteListener;

    @Inject
    Eventing mEventing;

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

    @Singleton
    private DataServicesManager() {
    }

    public static synchronized DataServicesManager getInstance() {
        if (sDataServicesManager == null) {
            return sDataServicesManager = new DataServicesManager();
        }
        return sDataServicesManager;
    }

    public void initializeDataServices(Context context, BaseAppDataCreator creator,
                                       UserRegistrationInterface userRegistrationInterface,
                                       ErrorHandlingInterface errorHandlingInterface, AppInfraInterface appInfraInterface) {
        this.mDataCreater = creator;
        this.userRegistrationInterface = userRegistrationInterface;
        this.errorHandlingInterface = errorHandlingInterface;
        this.mAppInfra = appInfraInterface;
        this.mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        initLogger();
    }

    private void initLogger() {
        DataServicesLogger.init();
        DataServicesLogger.enableLogging();
    }

    @Deprecated
    @SuppressWarnings("rawtypes")
    public void initializeSyncMonitors(Context context, ArrayList<DataFetcher> customFetchers,
                                       ArrayList<DataSender> customSenders, SynchronisationCompleteListener synchronisationCompleteListener) {
        this.mCustomFetchers = customFetchers;
        this.mCustomSenders = customSenders;
        this.mSynchronisationCompleteListener = synchronisationCompleteListener;
        buildDaggerComponent(context);
        startMonitors();
    }

    @SuppressWarnings("rawtypes")
    public void initializeSyncMonitors(Context context, ArrayList<DataFetcher> customFetchers, ArrayList<DataSender> customSenders) {
        this.mCustomFetchers = customFetchers;
        this.mCustomSenders = customSenders;
        buildDaggerComponent(context);
        startMonitors();
    }

    private void buildDaggerComponent(Context context) {
        BackendModule backendModule = new BackendModule(new EventingImpl(new EventBus(), new Handler()), mDataCreater, userRegistrationInterface,
                mDeletingInterface, mFetchingInterface, mSavingInterface, mUpdatingInterface,
                mCustomFetchers, mCustomSenders, errorHandlingInterface);
        final ApplicationModule applicationModule = new ApplicationModule(context);

        mAppComponent = DaggerAppComponent.builder().backendModule(backendModule).applicationModule(applicationModule).build();
        mAppComponent.injectApplication(this);
    }

    public void initializeDatabaseMonitor(Context context, DBDeletingInterface deletingInterface,
                                          DBFetchingInterface fetchingInterface, DBSavingInterface savingInterface, DBUpdatingInterface updatingInterface) {
        this.mDeletingInterface = deletingInterface;
        this.mFetchingInterface = fetchingInterface;
        this.mSavingInterface = savingInterface;
        this.mUpdatingInterface = updatingInterface;
    }

    public void synchronize() {
        clearExpiredMoments(null);
        synchronized (this) {
            startMonitors();
            mSynchronisationManager.startSync(mSynchronisationCompleteListener);
        }
    }

    public void synchronizeMomentsByDateRange(DateTime startDate, DateTime endDate, SynchronisationCompleteListener synchronisationCompleteListener) {
        synchronized (this) {
            startMonitors();
            mSynchronisationManager.startSync(startDate, endDate, synchronisationCompleteListener);
        }
    }

    private void startMonitors() {
        if (mCore != null) {
            mCore.start();
        }
        if (mSynchronisationMonitor != null) {
            mSynchronisationMonitor.start(mEventing);
        }
    }

    public void stopCore() {
        synchronized (this) {
            if (mCore != null)
                mCore.stop();
            if (mSynchronisationMonitor != null)
                mSynchronisationMonitor.stop();

            mSynchronisationManager.stopSync();
        }
    }

    @NonNull
    public Moment createMoment(@NonNull final String type) {
        return mDataCreater.createMoment(mBackendIdProvider.getUserId(), mBackendIdProvider.getSubjectId(), type, null);
    }

    @NonNull
    public MomentDetail createMomentDetail(@NonNull final String type, String value, @NonNull final Moment moment) {
        MomentDetail momentDetail = mDataCreater.createMomentDetail(type, moment);
        moment.addMomentDetail(momentDetail);
        momentDetail.setValue(value);
        return momentDetail;
    }

    @NonNull
    public Measurement createMeasurement(@NonNull final String type, String value, String unit, @NonNull final MeasurementGroup measurementGroup) {
        Measurement measurement = mDataCreater.createMeasurement(type, measurementGroup);
        measurement.setValue(value);
        measurement.setUnit(unit);
        measurementGroup.addMeasurement(measurement);
        return measurement;
    }

    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                     String value, @NonNull final Measurement measurement) {
        MeasurementDetail measurementDetail = mDataCreater.createMeasurementDetail(type, measurement);
        measurementDetail.setValue(value);
        measurement.addMeasurementDetail(measurementDetail);
        return measurementDetail;
    }

    @NonNull
    public MeasurementGroup createMeasurementGroup(@NonNull final Moment moment) {
        return mDataCreater.createMeasurementGroup(moment);
    }

    @NonNull
    public MeasurementGroup createMeasurementGroup(@NonNull final MeasurementGroup measurementGroup) {
        return mDataCreater.createMeasurementGroup(measurementGroup);
    }

    public MeasurementGroupDetail createMeasurementGroupDetail(String type, String value, MeasurementGroup mMeasurementGroup) {
        MeasurementGroupDetail measurementGroupDetail = mDataCreater.createMeasurementGroupDetail(type, mMeasurementGroup);
        measurementGroupDetail.setValue(value);
        mMeasurementGroup.addMeasurementGroupDetail(measurementGroupDetail);
        return measurementGroupDetail;
    }

    public void deleteMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentDeleteRequest(moment, dbRequestListener));
    }

    public void deleteMoments(final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentsDeleteRequest(moments, dbRequestListener));
    }

    public void deleteAllMoments(DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new DeleteAllMomentsRequest(dbRequestListener));
    }

    public void clearExpiredMoments(DBRequestListener<Integer> dbRequestListener) {
        mEventing.post(new DeleteExpiredMomentRequest(dbRequestListener));
    }

    public void updateMoment(Moment moment, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post((new MomentUpdateRequest(moment, dbRequestListener)));
    }

    public void updateMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post((new MomentsUpdateRequest(moments, dbRequestListener)));
    }

    @NonNull
    public void saveMoment(@NonNull final Moment moment, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentSaveRequest(moment, dbRequestListener));
    }

    @NonNull
    public void saveMoments(@NonNull final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentsSaveRequest(moments, dbRequestListener));
    }

    public void fetchMomentWithType(DBFetchRequestListner<Moment> dbFetchRequestListner, final @NonNull String... type) {
        mEventing.post(new LoadMomentsRequest(dbFetchRequestListner, type));
    }

    public void fetchMomentForMomentID(final int momentID, DBFetchRequestListner<Moment> dbFetchRequestListner) {
        mEventing.post(new LoadMomentsRequest(momentID, dbFetchRequestListner));
    }

    public void fetchLatestMomentByType(final @NonNull String type, DBFetchRequestListner<Moment> dbFetchRequestListener) {
        mEventing.post(new LoadLatestMomentByTypeRequest(type, dbFetchRequestListener));
    }

    public void fetchAllMoment(DBFetchRequestListner<Moment> dbFetchRequestListner) {
        mEventing.post(new LoadMomentsRequest(dbFetchRequestListner));
    }

    public void fetchMomentsWithTimeLine(Date startDate, Date endDate, DSPagination dsPagination, DBFetchRequestListner<Moment> dbFetchRequestListener) {
        mEventing.post(new LoadMomentsByDate(startDate, endDate, dsPagination, dbFetchRequestListener));
    }

    public void fetchMomentsWithTypeAndTimeLine(String momentType, Date startDate, Date endDate, DSPagination dsPagination, DBFetchRequestListner<Moment> dbFetchRequestListener) {
        mEventing.post(new LoadMomentsByDate(momentType, startDate, endDate, dsPagination, dbFetchRequestListener));
    }

    public ConsentDetail createConsentDetail(@NonNull final String detailType, final ConsentDetailStatusType consentDetailStatusType, String documentVersion, final String deviceIdentificationNumber) {
        return mDataCreater.createConsentDetail(detailType, consentDetailStatusType.getDescription(), documentVersion, deviceIdentificationNumber);
    }

    public void saveConsentDetails(List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) {
        mEventing.post(new DatabaseConsentSaveRequest(consentDetails, dbRequestListener));
    }

    public void updateConsentDetails(List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) {
        mEventing.post(new DatabaseConsentUpdateRequest(consentDetails, dbRequestListener));
    }

    public void fetchConsentDetail(DBFetchRequestListner<ConsentDetail> dbFetchRequestListner) {
        mEventing.post(new LoadConsentsRequest(dbFetchRequestListner));
    }

    public Settings createUserSettings(String locale, String unit) {
        return mDataCreater.createSettings(unit, locale);
    }

    public void saveUserSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) {
        mEventing.post(new DatabaseSettingsSaveRequest(settings, dbRequestListener));
    }

    public void updateUserSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) {
        mEventing.post(new DatabaseSettingsUpdateRequest(settings, dbRequestListener));
    }

    public void fetchUserSettings(DBFetchRequestListner<Settings> dbFetchRequestListner) {
        mEventing.post(new LoadSettingsRequest(dbFetchRequestListner));
    }

    public Characteristics createUserCharacteristics(@NonNull final String detailType, @NonNull final String detailValue, Characteristics characteristics) {
        Characteristics chDetail;
        if (characteristics != null) {
            chDetail = mDataCreater.createCharacteristics(detailType, detailValue, characteristics);
        } else {
            chDetail = mDataCreater.createCharacteristics(detailType, detailValue);
        }
        return chDetail;
    }

    public void saveUserCharacteristics(List<Characteristics> characteristicses, DBRequestListener<Characteristics> dbRequestListener) {
        mEventing.post(new UserCharacteristicsSaveRequest(characteristicses, dbRequestListener));
    }

    public void updateUserCharacteristics(List<Characteristics> characteristicses, DBRequestListener<Characteristics> dbRequestListener) {
        mEventing.post(new UserCharacteristicsSaveRequest(characteristicses, dbRequestListener));
    }

    public void fetchUserCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) {
        mEventing.post(new LoadUserCharacteristicsRequest(dbFetchRequestListner));
    }

    public void fetchInsights(DBFetchRequestListner dbFetchRequestListner) {
        mEventing.post(new FetchInsightsFromDB(dbFetchRequestListner));
    }

    public void deleteInsights(List<? extends Insight> insights, DBRequestListener<Insight> dbRequestListener) {
        mEventing.post(new DeleteInsightFromDB((List<Insight>) insights, dbRequestListener));
    }

    public void deleteAll(DBRequestListener dbRequestListener) {
        mEventing.post(new DataClearRequest(dbRequestListener));
    }

    public void registerDeviceToken(String deviceToken, String appVariant, String protocolProvider, RegisterDeviceTokenListener registerDeviceTokenListener) {
        mEventing.post(new RegisterDeviceToken(deviceToken, appVariant, protocolProvider, registerDeviceTokenListener));
    }

    public void unRegisterDeviceToken(String appToken, String appVariant, RegisterDeviceTokenListener registerDeviceTokenListener) {
        mEventing.post(new UnRegisterDeviceToken(appToken, appVariant, registerDeviceTokenListener));
    }

    public void handlePushNotificationPayload(JSONObject jsonObject) throws JSONException {
        synchronize();
    }

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

    public String fetchBaseUrlFromServiceDiscovery() {
        if (mServiceDiscoveryInterface == null) {
            throw new RuntimeException("Please initialize appinfra");
        }

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

    public String fetchCoachingServiceUrlFromServiceDiscovery() {
        if (mServiceDiscoveryInterface == null) {
            throw new RuntimeException("Please initialize appinfra");
        }

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

    public AppInfraInterface getAppInfra() {
        return mAppInfra;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public void setAppComponent(AppComponent appComponent) {
        mAppComponent = appComponent;
    }

    public ArrayList<DataFetcher> getCustomFetchers() {
        return mCustomFetchers;
    }

    public ArrayList<DataSender> getCustomSenders() {
        return mCustomSenders;
    }

    public void configureSyncDataType(Set<String> fetchers) {
        mSyncDataTypes = fetchers;
    }

    public Set<String> getSyncTypes() {
        return mSyncDataTypes;
    }

    public void registerDBChangeListener(DBChangeListener dbChangeListener) {
        this.dbChangeListener = dbChangeListener;
    }

    public void unRegisterDBChangeListener() {
        this.dbChangeListener = null;
    }

    public void registerSynchronisationCompleteListener(SynchronisationCompleteListener synchronisationCompleteListener) {
        this.mSynchronisationCompleteListener = synchronisationCompleteListener;
    }

    public void unRegisterSynchronisationCosmpleteListener() {
        this.mSynchronisationCompleteListener = null;
    }

    public DBChangeListener getDbChangeListener() {
        return dbChangeListener;
    }
}
