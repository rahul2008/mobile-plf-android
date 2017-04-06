/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.trackers;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
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
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.DeleteAllMomentsRequest;
import com.philips.platform.core.events.DeleteInsightFromDB;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.FetchInsightsFromDB;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.LoadUserCharacteristicsRequest;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.MomentsDeleteRequest;
import com.philips.platform.core.events.MomentsSaveRequest;
import com.philips.platform.core.events.MomentsUpdateRequest;
import com.philips.platform.core.events.RegisterDeviceToken;
import com.philips.platform.core.events.UnRegisterDeviceToken;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.injection.ApplicationModule;
import com.philips.platform.core.injection.BackendModule;
import com.philips.platform.core.injection.DaggerAppComponent;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.core.utils.EventingImpl;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.synchronisation.SynchronisationMonitor;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;


@SuppressWarnings({"rawtypes", "unchecked"})
public class DataServicesManager {

    public static final String TAG = DataServicesManager.class.getName();

    private DBChangeListener dbChangeListener;

    private SynchronisationCompleteListener mSynchronisationCompleteListener;

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

    @Singleton
    private DataServicesManager() {
    }

    public static synchronized DataServicesManager getInstance() {
        if (sDataServicesManager == null) {
            return sDataServicesManager = new DataServicesManager();
        }
        return sDataServicesManager;
    }

    @NonNull
    public void saveMoment(@NonNull final Moment moment, DBRequestListener<Moment> dbRequestListener) {
        DSLog.i(DSLog.LOG, "In DataServicesManager.saveMoment for " + moment.toString());
        mEventing.post(new MomentSaveRequest(moment, dbRequestListener));
    }

    @NonNull
    public void saveMoments(@NonNull final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentsSaveRequest(moments, dbRequestListener));
    }

    public void fetchMomentWithType(DBFetchRequestListner<Moment> dbFetchRequestListner, final @NonNull String... type) {
        DSLog.i(DSLog.LOG, "pabitra DataServiceManger fetchMomentWithType");
        mEventing.post(new LoadMomentsRequest(dbFetchRequestListner, type));
    }

    public void fetchMomentForMomentID(final int momentID, DBFetchRequestListner<Moment> dbFetchRequestListner) {
        mEventing.post(new LoadMomentsRequest(momentID, dbFetchRequestListner));
    }

    public void fetchAllMoment(DBFetchRequestListner<Moment> dbFetchRequestListner) {
        mEventing.post(new LoadMomentsRequest(dbFetchRequestListner));
    }

    @NonNull
    public void fetchConsentDetail(DBFetchRequestListner<ConsentDetail> dbFetchRequestListner) {
        mEventing.post(new LoadConsentsRequest(dbFetchRequestListner));
    }


    public ConsentDetail createConsentDetail(@NonNull final String detailType, final ConsentDetailStatusType consentDetailStatusType,String documentVersion, final String deviceIdentificationNumber) {
        return mDataCreater.createConsentDetail(detailType, consentDetailStatusType.getDescription(), documentVersion, deviceIdentificationNumber);
    }

    public void saveConsentDetails(List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) {
        mEventing.post(new DatabaseConsentSaveRequest(consentDetails,dbRequestListener));
    }

    public void updateConsentDetails(List<ConsentDetail> consentDetails, DBRequestListener<ConsentDetail> dbRequestListener) {
        mEventing.post(new DatabaseConsentUpdateRequest(consentDetails,dbRequestListener));
    }

    public Settings createUserSettings(String locale ,String unit) {
        Settings settings = mDataCreater.createSettings(unit,locale);
        return settings;
    }

    public void saveUserSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) {
        mEventing.post(new DatabaseSettingsSaveRequest(settings,dbRequestListener));
    }


    public void updateUserSettings(Settings settings, DBRequestListener<Settings> dbRequestListener) {
        mEventing.post(new DatabaseSettingsUpdateRequest(settings,dbRequestListener));
    }


    @NonNull
    public Moment createMoment(@NonNull final String type) {
        return mDataCreater.createMoment(mBackendIdProvider.getUserId(), mBackendIdProvider.getSubjectId(), type);
    }

    @NonNull
    public MeasurementGroup createMeasurementGroup(@NonNull final Moment moment) {
        return mDataCreater.createMeasurementGroup(moment);
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
      //  measurement.setDateTime(DateTime.now());
        measurement.setUnit(unit);
        measurementGroup.addMeasurement(measurement);
        return measurement;
    }

    @NonNull
    public MeasurementGroup createMeasurementGroup(@NonNull final MeasurementGroup measurementGroup) {
        return mDataCreater.createMeasurementGroup(measurementGroup);
    }

    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                     String value, @NonNull final Measurement measurement) {
        MeasurementDetail measurementDetail = mDataCreater.createMeasurementDetail(type, measurement);
        measurementDetail.setValue(value);
        measurement.addMeasurementDetail(measurementDetail);
        return measurementDetail;
    }

    public void deleteMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentDeleteRequest(moment, dbRequestListener));
    }

    public void deleteMoments(final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post(new MomentsDeleteRequest(moments, dbRequestListener));
    }

    public void updateMoment(Moment moment, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post((new MomentUpdateRequest(moment, dbRequestListener)));
    }

    public void updateMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        mEventing.post((new MomentsUpdateRequest(moments, dbRequestListener)));
    }

    public void synchronize() {
        sendPullDataEvent();
    }

    @Deprecated
    @SuppressWarnings("rawtypes")
    public void initializeSyncMonitors(Context context, ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders, SynchronisationCompleteListener synchronisationCompleteListener) {
        initSyncMonitors(context, fetchers, senders, synchronisationCompleteListener);
    }

    @SuppressWarnings("rawtypes")
    public void initializeSyncMonitors(Context context, ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders) {
        initSyncMonitors(context, fetchers, senders, null);
    }

    private void initSyncMonitors(Context context, ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders, SynchronisationCompleteListener synchronisationCompleteListener) {
        DSLog.i(DSLog.LOG, "In DataServicesManager.initializeSyncMonitors");
        this.mCustomFetchers = fetchers;
        this.mCustomSenders = senders;
        this.mSynchronisationCompleteListener = synchronisationCompleteListener;
        prepareInjectionsGraph(context);
        startMonitors();
    }

    private void sendPullDataEvent() {
        synchronized (this) {
            DSLog.i(DSLog.LOG, "In DataServicesManager.sendPullDataEvent");
            startMonitors();
            //mEventing.post(new ReadDataFromBackendRequest(null));
            mSynchronisationManager.startSync(mSynchronisationCompleteListener);
        }
    }

    private void startMonitors() {
        if (mCore != null) {
            DSLog.i(DSLog.LOG, "mCore not null, hence starting");
            mCore.start();
        }
        if (mSynchronisationMonitor != null) {
            DSLog.i(DSLog.LOG, "In DataServicesManager.mSynchronisationMonitor.start");
            mSynchronisationMonitor.start(mEventing);
        }
    }

    public void initializeDatabaseMonitor(Context context, DBDeletingInterface deletingInterface, DBFetchingInterface fetchingInterface, DBSavingInterface savingInterface, DBUpdatingInterface updatingInterface) {
        this.mDeletingInterface = deletingInterface;
        this.mFetchingInterface = fetchingInterface;
        this.mSavingInterface = savingInterface;
        this.mUpdatingInterface = updatingInterface;
    }

    public void configureSyncDataType(Set<String> fetchers){
        mSyncDataTypes = fetchers;
    }

    public void initializeDataServices(Context context, BaseAppDataCreator creator, UserRegistrationInterface facade, ErrorHandlingInterface errorHandlingInterface) {
        DSLog.i("SPO", "initializeDataServices called");
        this.mDataCreater = creator;
        this.userRegistrationInterface = facade;
        this.errorHandlingInterface = errorHandlingInterface;
    }

    public void deleteAll(DBRequestListener dbRequestListener) {
        mEventing.post(new DataClearRequest(dbRequestListener));
    }

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

    public void stopCore() {
        synchronized (this) {
            DSLog.i(DSLog.LOG, "In DataServicesManager.stopCore");
            if (mCore != null)
                mCore.stop();
            if (mSynchronisationMonitor != null)
                mSynchronisationMonitor.stop();

            mSynchronisationManager.stopSync();
        }
    }


    public MeasurementGroupDetail createMeasurementGroupDetail(String type, String value, MeasurementGroup mMeasurementGroup) {
        MeasurementGroupDetail measurementGroupDetail = mDataCreater.createMeasurementGroupDetail(type, mMeasurementGroup);
        measurementGroupDetail.setValue(value);
        mMeasurementGroup.addMeasurementGroupDetail(measurementGroupDetail);
        return measurementGroupDetail;
    }

    public void updateUserCharacteristics(List<Characteristics> characteristicses, DBRequestListener<Characteristics> dbRequestListener) {
        mEventing.post(new UserCharacteristicsSaveRequest(characteristicses, dbRequestListener));
    }

    public void saveUserCharacteristics(List<Characteristics> characteristicses, DBRequestListener<Characteristics> dbRequestListener) {
        mEventing.post(new UserCharacteristicsSaveRequest(characteristicses, dbRequestListener));
    }


    public void fetchUserCharacteristics(DBFetchRequestListner<Characteristics> dbFetchRequestListner) {
        mEventing.post(new LoadUserCharacteristicsRequest(dbFetchRequestListner));
    }

    public Characteristics createUserCharacteristics(@NonNull final String detailType, @NonNull final String detailValue, Characteristics characteristics) {

        Characteristics chDetail;
        if (characteristics != null) {
            chDetail = mDataCreater.createCharacteristics(detailType, detailValue,characteristics);
        } else {
            chDetail = mDataCreater.createCharacteristics(detailType, detailValue);
        }
        return chDetail;
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

    public void fetchUserSettings(DBFetchRequestListner<Settings> dbFetchRequestListner) {
      mEventing.post(new LoadSettingsRequest(dbFetchRequestListner));
    }

    public AppComponent getAppComponant(){
        return mAppComponent;
    }

    public void setAppComponant(AppComponent appComponent){
        mAppComponent = appComponent;
    }

    public Set<String> getSyncTypes() {
        return mSyncDataTypes;
    }

    public ArrayList<DataFetcher> getCustomFetchers() {
        return mCustomFetchers;
    }

    public ArrayList<DataSender> getCustomSenders() {
        return mCustomSenders;
    }

    //Insight
    public void fetchInsights(DBFetchRequestListner dbFetchRequestListner){
        mEventing.post(new FetchInsightsFromDB(dbFetchRequestListner));
    }

    public void deleteInsights(List<? extends Insight> insights, DBRequestListener<Insight> dbRequestListener){
        mEventing.post(new DeleteInsightFromDB((List<Insight>) insights,dbRequestListener));
    }

    //Push Notification
    public void registerDeviceToken(String deviceToken, String appVariant, String protocolProvider, RegisterDeviceTokenListener registerDeviceTokenListener) {
        mEventing.post(new RegisterDeviceToken(deviceToken, appVariant, protocolProvider, registerDeviceTokenListener));
    }

    public void unRegisterDeviceToken(String appToken, String appVariant, RegisterDeviceTokenListener registerDeviceTokenListener) {
        mEventing.post(new UnRegisterDeviceToken(appToken, appVariant, registerDeviceTokenListener));
    }

    public void handlePushNotificationPayload(JSONObject jsonObject) throws JSONException {
        synchronize();
    }
}
