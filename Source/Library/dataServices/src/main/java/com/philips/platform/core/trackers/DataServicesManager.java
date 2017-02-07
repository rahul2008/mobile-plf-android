/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
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
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementGroup;
import com.philips.platform.core.datatypes.MeasurementGroupDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsSaveRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.DeleteAllMomentsRequest;
import com.philips.platform.core.events.LoadConsentsRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.LoadSettingsRequest;
import com.philips.platform.core.events.LoadUserCharacteristicsRequest;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.injection.ApplicationModule;
import com.philips.platform.core.injection.BackendModule;
import com.philips.platform.core.injection.DaggerAppComponent;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
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

import java.util.ArrayList;

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

    private ArrayList<DataFetcher> fetchers;
    private ArrayList<DataSender> senders;

    private UserCharacteristics getUserCharacteristics() {
        return userCharacteristics;
    }

    private void setUserCharacteristics(UserCharacteristics userCharacteristics) {
        this.userCharacteristics = userCharacteristics;
    }

    private UserCharacteristics userCharacteristics;

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
    public void saveMoment(@NonNull final Moment moment, DBRequestListener dbRequestListener) {
        DSLog.i("***SPO***", "In DataServicesManager.saveMoment for " + moment.toString());
        mEventing.post(new MomentSaveRequest(moment, dbRequestListener));
    }

/*

    public Moment update(@NonNull final Moment moment, DBRequestListener dbRequestListener) {
        mEventing.post(new MomentUpdateRequest(moment, dbRequestListener));
        return moment;
    }
*/

    public void fetchMomentWithType(DBRequestListener dbRequestListener, final @NonNull String... type) {
        mEventing.post(new LoadMomentsRequest(dbRequestListener, type));
    }

    public void fetchMomentById(final int momentID, DBRequestListener dbRequestListener) {
        mEventing.post(new LoadMomentsRequest(momentID, dbRequestListener));
    }

  /*  public void fetchAllData(DBRequestListener dbRequestListener) {
        mEventing.post(new LoadMomentsRequest(dbRequestListener));
    }*/

    @NonNull
    public void fetchConsent(DBRequestListener dbRequestListener) {
        mEventing.post(new LoadConsentsRequest(dbRequestListener));
    }

    @NonNull
    public Consent createConsent() {
        return mDataCreater.createConsent(userRegistrationInterface.getUserProfile().getGUid());
    }

    public void createConsentDetail(@NonNull Consent consent, @NonNull final String detailType, final ConsentDetailStatusType consentDetailStatusType, final String deviceIdentificationNumber) {
        if (consent == null) {
            consent = createConsent();
        }
        ConsentDetail consentDetail = mDataCreater.createConsentDetail(detailType, consentDetailStatusType.getDescription(), Consent.DEFAULT_DOCUMENT_VERSION, deviceIdentificationNumber, true, consent);
        consent.addConsentDetails(consentDetail);
    }

    public void saveConsent(Consent consent, DBRequestListener dbRequestListener) {
        mEventing.post(new DatabaseConsentSaveRequest(consent, false, dbRequestListener));
    }

    public void updateConsent(Consent consent, DBRequestListener dbRequestListener) {
        mEventing.post(new DatabaseConsentSaveRequest(consent, false, dbRequestListener));
    }

    public Settings createSettings(String unit, String locale) {
        Settings settings = mDataCreater.createSettings(unit, locale);
        return settings;
    }

    public void saveSettings(Settings settings, DBRequestListener dbRequestListener) {
        mEventing.post(new DatabaseSettingsSaveRequest(settings, dbRequestListener));
    }


    public void updateSettings(Settings settings, DBRequestListener dbRequestListener) {
        mEventing.post(new DatabaseSettingsUpdateRequest(settings, dbRequestListener));
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
                                                     String value, @NonNull final Measurement measurement) {
        MeasurementDetail measurementDetail = mDataCreater.createMeasurementDetail(type, measurement);
        measurementDetail.setValue(value);
        measurement.addMeasurementDetail(measurementDetail);
        return measurementDetail;
    }

    public void deleteMoment(final Moment moment, DBRequestListener dbRequestListener) {
        mEventing.post(new MomentDeleteRequest(moment, dbRequestListener));
    }

    public void updateMoment(Moment moment, DBRequestListener dbRequestListener) {
        mEventing.post((new MomentUpdateRequest(moment, dbRequestListener)));
    }

    public void Synchronize() {
        sendPullDataEvent();
    }

    @SuppressWarnings("rawtypes")
    public void initializeSyncMonitors(Context context, ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders, SynchronisationCompleteListener synchronisationCompleteListener) {
        DSLog.i("***SPO***", "In DataServicesManager.initializeSyncMonitors");
        this.fetchers = fetchers;
        this.senders = senders;
        this.mSynchronisationCompleteListener = synchronisationCompleteListener;
        prepareInjectionsGraph(context);
        startMonitors();
    }

    private void sendPullDataEvent() {
        synchronized (this) {
            DSLog.i("***SPO***", "In DataServicesManager.sendPullDataEvent");
            startMonitors();
            //mEventing.post(new ReadDataFromBackendRequest(null));
            mSynchronisationManager.startSync(mSynchronisationCompleteListener);
        }
    }

    //TODO: discuss if its required
    private void startMonitors() {
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

    public void initializeDataServices(Context context, BaseAppDataCreator creator, UserRegistrationInterface facade, ErrorHandlingInterface errorHandlingInterface) {
        DSLog.i("SPO", "initializeDataServices called");
        this.mDataCreater = creator;
        this.userRegistrationInterface = facade;
        this.errorHandlingInterface = errorHandlingInterface;
    }

    public void deleteAll(DBRequestListener dbRequestListener) {
        mEventing.post(new DataClearRequest(dbRequestListener));
    }

    public void deleteAllMoments(DBRequestListener dbRequestListener) {
        mEventing.post(new DeleteAllMomentsRequest(dbRequestListener));
    }


    private void prepareInjectionsGraph(Context context) {
        BackendModule backendModule = new BackendModule(new EventingImpl(new EventBus(), new Handler()), mDataCreater, userRegistrationInterface,
                mDeletingInterface, mFetchingInterface, mSavingInterface, mUpdatingInterface,
                fetchers, senders, errorHandlingInterface);
        final ApplicationModule applicationModule = new ApplicationModule(context);

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

            mSynchronisationManager.stopSync();
        }
    }


    public MeasurementGroupDetail createMeasurementGroupDetail(String type, String value, MeasurementGroup mMeasurementGroup) {
        MeasurementGroupDetail measurementGroupDetail = mDataCreater.createMeasurementGroupDetail(type, mMeasurementGroup);
        measurementGroupDetail.setValue(value);
        mMeasurementGroup.addMeasurementGroupDetail(measurementGroupDetail);
        return measurementGroupDetail;
    }

    @NonNull
    private UserCharacteristics createUCSync() {
        return mDataCreater.createCharacteristics(userRegistrationInterface.getUserProfile().getGUid());
    }

    public void updateCharacteristics(DBRequestListener dbRequestListener) {

        mEventing.post(new UserCharacteristicsSaveRequest(getUserCharacteristics(), dbRequestListener));
        setUserCharacteristics(null);
    }

    public void fetchUserCharacteristics(DBRequestListener dbRequestListener) {
        mEventing.post(new LoadUserCharacteristicsRequest(dbRequestListener));
    }

    public Characteristics createUserCharacteristics(@NonNull final String detailType, @NonNull final String detailValue, Characteristics characteristics) {

        userCharacteristics = getUserCharacteristics();
        if (userCharacteristics == null) {
            userCharacteristics = createUCSync();
        }

        Characteristics chDetail;
        if (characteristics != null) {
            chDetail = mDataCreater.createCharacteristicsDetails(detailType, detailValue, userCharacteristics, characteristics);
        } else {
            chDetail = mDataCreater.createCharacteristicsDetails(detailType, detailValue, userCharacteristics);
        }
        userCharacteristics.addCharacteristicsDetail(chDetail);
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

    public void fetchSettings(DBRequestListener dbRequestListener) {
        mEventing.post(new LoadSettingsRequest(dbRequestListener));
    }

    public AppComponent getAppComponant() {
        return mAppComponent;
    }

    public void setAppComponant(AppComponent appComponent) {
        mAppComponent = appComponent;
    }
}
