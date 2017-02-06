/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.trackers;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.OrmTableType;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadUserCharacteristicsRequest;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.injection.ApplicationModule;
import com.philips.platform.core.injection.BackendModule;
import com.philips.platform.core.injection.DaggerAppComponent;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.core.utils.EventingImpl;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.synchronisation.SynchronisationMonitor;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DataServicesManager {

    public static final String TAG = DataServicesManager.class.getName();

    private volatile boolean isPullComplete = true;

    private volatile boolean isPushComplete = true;

    private DBRequestListener dbRequestListener;

    private DBChangeListener dbChangeListener;

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

    private static DataServicesManager sDataServicesManager;

    @Inject
    UserRegistrationInterface userRegistrationInterface;

    @Inject
    ErrorHandlingInterface errorHandlingInterface;

    private ArrayList<DataFetcher> fetchers;
    private ArrayList<DataSender> senders;



    @Singleton
    private DataServicesManager() {
    }

    public static synchronized DataServicesManager getInstance() {
        if (sDataServicesManager == null) {
            return sDataServicesManager = new DataServicesManager();
        }
        return sDataServicesManager;
    }

 /*   public UCoreAccessProvider getUCoreAccessProvider() {
        return (UCoreAccessProvider) mBackendIdProvider;
    }*/

  /*  public BaseAppDataCreator getDataCreater() {
        return mDataCreater;
    }*/

    @NonNull
    public Moment save(@NonNull final Moment moment, DBRequestListener dbRequestListener) {
        DSLog.i("***SPO***", "In DataServicesManager.save for " + moment.toString());
        mEventing.post(new MomentSaveRequest(moment, dbRequestListener));
        return moment;
    }

   /* @NonNull
    public List<Moment> save(@NonNull final List<Moment> momentList, DBRequestListener dbRequestListener) {
        mEventing.post(new MomentSaveRequest(moment, dbRequestListener));
        return momentList;
    }*/

    public Moment update(@NonNull final Moment moment, DBRequestListener dbRequestListener) {
        mEventing.post(new MomentUpdateRequest(moment, dbRequestListener));
        return moment;
    }

    public void fetch(DBRequestListener dbRequestListener, final @NonNull Integer... type) {
        mEventing.post(new LoadMomentsRequest(dbRequestListener, type));
    }

    public void fetchMomentById(final int momentID, DBRequestListener dbRequestListener) {
        mEventing.post(new LoadMomentsRequest(momentID, dbRequestListener));
    }

    public void fetchAllData(DBRequestListener dbRequestListener) {
        mEventing.post(new LoadMomentsRequest(dbRequestListener));
//        Log.d(this.getClass().getName(),"Inside DataService");
    }

    @NonNull
    public void fetchConsentDetail(DBRequestListener dbRequestListener) {
        mEventing.post(new LoadConsentsRequest(dbRequestListener));
    }


    public ConsentDetail createConsentDetail(@NonNull final String detailType, final ConsentDetailStatusType consentDetailStatusType,String documentVersion, final String deviceIdentificationNumber) {
        return mDataCreater.createConsent(detailType, consentDetailStatusType.getDescription(), documentVersion, deviceIdentificationNumber);
    }

    public void saveConsentDetail(List<ConsentDetail> consentDetails, DBRequestListener dbRequestListener) {
        mEventing.post(new DatabaseConsentSaveRequest(consentDetails,dbRequestListener));
    }

    public void updateConsentDetail(List<ConsentDetail> consentDetails, DBRequestListener dbRequestListener) {
        mEventing.post(new SyncBitUpdateRequest(OrmTableType.CONSENT,false));
        mEventing.post(new DatabaseConsentUpdateRequest(consentDetails,dbRequestListener));
    }

    public Settings createSettings(String unit,String locale) {
        Settings settings = mDataCreater.createSettings(unit,locale);
        return settings;
    }

    public void saveSettings(Settings settings, DBRequestListener dbRequestListener) {
        mEventing.post(new DatabaseSettingsSaveRequest(settings,dbRequestListener));
    }


    public void updateSettings(Settings settings, DBRequestListener dbRequestListener) {
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
    public MomentDetail createMomentDetail(@NonNull final String type, @NonNull final Moment moment) {
        MomentDetail momentDetail = mDataCreater.createMomentDetail(type, moment);
        moment.addMomentDetail(momentDetail);
        return momentDetail;
    }

  /*  @NonNull
    public Measurement createMeasurement(@NonNull final MeasurementType type, @NonNull final Moment moment) {
        Measurement measurement = mDataCreater.createMeasurement(type, moment);
        moment.addMeasurement(measurement);
        return measurement;
    }*/

    @NonNull
    public Measurement createMeasurement(@NonNull final String type, @NonNull final MeasurementGroup measurementGroup) {
        Measurement measurement = mDataCreater.createMeasurement(type, measurementGroup);
        measurementGroup.addMeasurement(measurement);
        return measurement;
    }

    @NonNull
    public MeasurementGroup createMeasurementGroup(@NonNull final MeasurementGroup measurementGroup) {
        return mDataCreater.createMeasurementGroup(measurementGroup);
    }

    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final String type,
                                                     @NonNull final Measurement measurement) {
        MeasurementDetail measurementDetail = mDataCreater.createMeasurementDetail(type, measurement);
        measurement.addMeasurementDetail(measurementDetail);
        return measurementDetail;
    }

    public void deleteMoment(final Moment moment, DBRequestListener dbRequestListener) {
        mEventing.post(new MomentDeleteRequest(moment, dbRequestListener));
    }

    public void updateMoment(Moment moment, DBRequestListener dbRequestListener) {
        mEventing.post((new MomentUpdateRequest(moment, dbRequestListener)));
    }

    public void synchchronize() {
        sendPullDataEvent();
    }

    @SuppressWarnings("rawtypes")
    public void initializeSyncMonitors(Context context, ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders) {
        DSLog.i("***SPO***", "In DataServicesManager.initializeSyncMonitors");
        this.fetchers = fetchers;
        this.senders = senders;
        prepareInjectionsGraph(context);
        startMonitors();
    }

 /*   private void sendPushEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("***SPO***", "In DataServicesManager.sendPushEvent");
                mEventing.post(new WriteDataToBackendRequest());
            }
        }, 20 * DateTimeConstants.MILLIS_PER_SECOND);

    }*/

    private void sendPullDataEvent() {
        synchronized (this) {
            DSLog.i("***SPO***", "In DataServicesManager.sendPullDataEvent");
            startMonitors();
            mEventing.post(new ReadDataFromBackendRequest(null));
        }
    }

    public void startMonitors() {
        if (mCore != null) {
            DSLog.i("***SPO***", "mCore not null, hence starting");
            mCore.start();
        }
        if (mSynchronisationMonitor != null) {
            DSLog.i("***SPO***", "In DataServicesManager.mSynchronisationMonitor.start");
            mSynchronisationMonitor.start(mEventing);
        }
    }

    public void initializeDBMonitors(Context context, DBDeletingInterface deletingInterface, DBFetchingInterface fetchingInterface, DBSavingInterface savingInterface, DBUpdatingInterface updatingInterface) {
        this.mDeletingInterface = deletingInterface;
        this.mFetchingInterface = fetchingInterface;
        this.mSavingInterface = savingInterface;
        this.mUpdatingInterface = updatingInterface;
    }

    public void initialize(Context context, BaseAppDataCreator creator, UserRegistrationInterface facade, ErrorHandlingInterface errorHandlingInterface) {
        DSLog.i("SPO", "initialize called");
        this.mDataCreater = creator;
        this.userRegistrationInterface = facade;
        this.errorHandlingInterface = errorHandlingInterface;
    }

    //Currently this is same as deleteAllMoment as only moments are there - later will be changed to delete all the tables
    public void deleteAll(DBRequestListener dbRequestListener) {
        mEventing.post(new DataClearRequest(dbRequestListener));
    }

    public void deleteAllMoment(DBRequestListener dbRequestListener) {
        mEventing.post(new DataClearRequest(dbRequestListener));
    }


    private void prepareInjectionsGraph(Context context) {
        BackendModule backendModule = new BackendModule(new EventingImpl(new EventBus(), new Handler()), mDataCreater, userRegistrationInterface,
                mDeletingInterface, mFetchingInterface, mSavingInterface, mUpdatingInterface,
                fetchers, senders, errorHandlingInterface);
        final ApplicationModule applicationModule = new ApplicationModule(context);

        // initiating all application module events
        mAppComponent = DaggerAppComponent.builder().backendModule(backendModule).applicationModule(applicationModule).build();
        mAppComponent.injectApplication(this);
    }

    public void stopCore() {
        synchronized (this) {
            DSLog.i("***SPO***", "In DataServicesManager.stopCore");
            if (mCore != null)
                mCore.stop();
            if (mSynchronisationMonitor != null)
                mSynchronisationMonitor.stop();

            isPullComplete = true;
            isPushComplete = true;
        }
    }

    /*public void releaseDataServicesInstances() {
        userRegistrationInterface = null;
        mBackendIdProvider = null;
        mDataCreater = null;
        mAppComponent = null;
        mDeletingInterface = null;
        mFetchingInterface = null;
        mSavingInterface = null;
        mUpdatingInterface = null;
        mEventing = null;
        mCore = null;
        mSynchronisationMonitor = null;
        fetchers = null;
        senders = null;
        userRegistrationInterface = null;
        errorHandlingInterface =null;
        // mCore.stop();
    }*/


  /*  public UserRegistrationInterface getUserRegistrationImpl() {
        return userRegistrationInterface;
    }*/


    public MeasurementGroupDetail createMeasurementGroupDetail(String tempOfDay, MeasurementGroup mMeasurementGroup) {
        return mDataCreater.createMeasurementGroupDetail(tempOfDay, mMeasurementGroup);
    }



    public void updateUserCharacteristics(List<Characteristics> characteristicses, DBRequestListener dbRequestListener) {
        mEventing.post(new UserCharacteristicsSaveRequest(characteristicses, dbRequestListener));
    }

    public void saveUserCharacteristics(List<Characteristics> characteristicses, DBRequestListener dbRequestListener) {
        mEventing.post(new UserCharacteristicsSaveRequest(characteristicses, dbRequestListener));
    }


    public void fetchUserCharacteristics(DBRequestListener dbRequestListener) {
        mEventing.post(new LoadUserCharacteristicsRequest(dbRequestListener));
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

    public boolean isPullComplete() {
        return isPullComplete;
    }

    public void setPullComplete(boolean pullComplete) {
        isPullComplete = pullComplete;
    }

    public boolean isPushComplete() {
        return isPushComplete;
    }

    public void setPushComplete(boolean pushComplete) {
        isPushComplete = pushComplete;
    }


    public void registerDBChangeListener(DBChangeListener dbChangeListener) {
        this.dbChangeListener = dbChangeListener;
    }

    public void unRegisterDBChangeListener() {
        this.dbChangeListener = null;
    }


    public void fetchSettings(DBRequestListener dbRequestListener) {
      mEventing.post(new LoadSettingsRequest(dbRequestListener));
    }

    public AppComponent getAppComponant(){
        return mAppComponent;
    }

    public void setAppComponant(AppComponent appComponent){
        mAppComponent = appComponent;
    }
}
