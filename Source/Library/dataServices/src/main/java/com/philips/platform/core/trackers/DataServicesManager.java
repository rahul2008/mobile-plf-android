/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.trackers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Measurement;
import com.philips.platform.core.datatypes.MeasurementDetail;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentDetail;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.LoadMomentsRequest;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentSaveRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.injection.ApplicationModule;
import com.philips.platform.core.injection.BackendModule;
import com.philips.platform.core.injection.DaggerAppComponent;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.DeletingMonitor;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.monitors.ExceptionMonitor;
import com.philips.platform.core.monitors.FetchingMonitor;
import com.philips.platform.core.monitors.LoggingMonitor;
import com.philips.platform.core.monitors.SavingMonitor;
import com.philips.platform.core.monitors.UpdatingMonitor;
import com.philips.platform.core.utils.EventingImpl;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.synchronisation.SynchronisationMonitor;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DataServicesManager {

    @NonNull
    private final Eventing mEventing;

    private BaseAppDataCreator mDataCreater;

    @Inject
    DataPullSynchronise mDataPullSynchronise;

    @Inject
    DataPushSynchronise mDataPushSynchronise;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    LoggingMonitor mLoggingMonitor;

    @Inject
    ExceptionMonitor mExceptionMonitor;

    @Inject
    Backend mBackend;

    AppComponent mAppComponent;

    private BackendIdProvider mBackendIdProvider;
    private BaseAppCore mCore;

    private DBMonitors mDbMonitors;
    private List<EventMonitor> mMonitors = new ArrayList<>();

    private static DataServicesManager mDataServicesManager;
    private Context mContext;
    private UserRegistrationFacade mUserRegistrationFacadeImpl;

    @Singleton
    private DataServicesManager() {
        this.mEventing = new EventingImpl(new EventBus(), new Handler());
    }

    public static DataServicesManager getInstance() {
        if (mDataServicesManager == null) {
            return mDataServicesManager = new DataServicesManager();
        }
        return mDataServicesManager;
    }

    public UCoreAccessProvider getUCoreAccessProvider(){
        return (UCoreAccessProvider) mBackendIdProvider;
    }

    public BaseAppDataCreator getDataCreater(){
        return mDataCreater;
    }

    @NonNull
    public Moment save(@NonNull final Moment moment) {
        mEventing.post(new MomentSaveRequest(moment));
        return moment;
    }
    public Moment update(@NonNull final Moment moment) {
        mEventing.post(new MomentUpdateRequest(moment));
        return moment;
    }

    public void fetch(final @NonNull MomentType... type){
        mEventing.post(new LoadMomentsRequest(type[0]));
    }

    public void fetchAllData(){
        mEventing.post(new LoadMomentsRequest());
    }

    @NonNull
    public Moment createMoment(@NonNull final MomentType type) {
        return mDataCreater.createMoment(mBackendIdProvider.getUserId(), mBackendIdProvider.getSubjectId(), type);
    }

    @NonNull
    public MomentDetail createMomentDetail(@NonNull final MomentDetailType type, @NonNull final Moment moment) {
        MomentDetail momentDetail = mDataCreater.createMomentDetail(type, moment);
        moment.addMomentDetail(momentDetail);
        return momentDetail;
    }

    @NonNull
    public Measurement createMeasurement(@NonNull final MeasurementType type, @NonNull final Moment moment) {
        Measurement measurement = mDataCreater.createMeasurement(type, moment);
        moment.addMeasurement(measurement);
        return measurement;
    }

    @NonNull
    public MeasurementDetail createMeasurementDetail(@NonNull final MeasurementDetailType type,
                                                     @NonNull final Measurement measurement) {
        MeasurementDetail measurementDetail = mDataCreater.createMeasurementDetail(type, measurement);
        measurement.addMeasurementDetail(measurementDetail);
        return measurementDetail;
    }

    public void deleteMoment(final Moment moment) {
        mEventing.post(new MomentDeleteRequest(moment));
    }

    public void updateMoment(Moment moment){
        mEventing.post((new MomentUpdateRequest(moment)));
    }

    public void synchchronize(){
        sendPullDataEvent();
    }

    @SuppressWarnings("rawtypes")
    public void initializeSyncMonitors(ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders){
        Log.i("***SPO***", "In DataServicesManager.Synchronize");
        SynchronisationMonitor monitor = new SynchronisationMonitor(mDataPullSynchronise,mDataPushSynchronise);
        monitor.start(mEventing);
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
        Log.i("***SPO***", "In DataServicesManager.sendPullDataEvent");
        if(mCore !=null) {
            mCore.start();
        }
        else{
            mCore = new BaseAppCore(mEventing, mDataCreater,mBackend, mMonitors,mDbMonitors);
        }
        mEventing.post(new ReadDataFromBackendRequest(null));
    }

    public void initializeDBMonitors(DBDeletingInterface deletingInterface, DBFetchingInterface fetchingInterface, DBSavingInterface savingInterface, DBUpdatingInterface updatingInterface) {
        SavingMonitor savingMonitor = new SavingMonitor(savingInterface);
        FetchingMonitor fetchMonitor = new FetchingMonitor(fetchingInterface);
        DeletingMonitor deletingMonitor = new DeletingMonitor(deletingInterface);
        UpdatingMonitor updatingMonitor = new UpdatingMonitor(updatingInterface, deletingInterface, fetchingInterface);

        mDbMonitors = new DBMonitors(Arrays.asList(savingMonitor, fetchMonitor, deletingMonitor, updatingMonitor));
    }

    public void initialize(Context context, BaseAppDataCreator creator, UserRegistrationFacade facade) {

        mContext = context;
        this.mDataCreater = creator;
        this.mUserRegistrationFacadeImpl = facade;
        this.mBackendIdProvider = new UCoreAccessProvider(facade);

        prepareInjectionsGraph(context);
        mAppComponent.injectApplication(this);
        mBackendIdProvider.injectSaredPrefs(mSharedPreferences);

        mMonitors = new ArrayList<>();
        mMonitors.add(mLoggingMonitor);
        mMonitors.add(mExceptionMonitor);

        mCore = new BaseAppCore(mEventing, mDataCreater,mBackend, mMonitors,mDbMonitors);
        mCore.start();
    }

    public void deleteAll(){
        mEventing.post(new DataClearRequest());
    }

    protected void prepareInjectionsGraph(Context context) {
        BackendModule backendModule = new BackendModule(mEventing);
        final ApplicationModule applicationModule = new ApplicationModule(context);

        // initiating all application module events
        mAppComponent = DaggerAppComponent.builder().backendModule(backendModule).applicationModule(applicationModule).build();
    }

    public void stopCore() {
        mCore.stop();
    }

    public UserRegistrationFacade getUserRegistrationImpl() {
        return mUserRegistrationFacadeImpl;
    }
}
