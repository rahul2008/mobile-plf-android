/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.DeletingMonitor;
import com.philips.platform.core.monitors.FetchingMonitor;
import com.philips.platform.core.monitors.SavingMonitor;
import com.philips.platform.core.monitors.UpdatingMonitor;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.OkClientFactory;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.consent.ConsentDataSender;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;
import com.philips.platform.datasync.consent.ConsentsMonitor;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataSender;
import com.philips.platform.datasync.moments.MomentsMonitor;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;
import com.philips.platform.datasync.userprofile.ErrorHandler;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Module
public class BackendModule {

    @NonNull
    private final Eventing eventing;

    @NonNull
    private final ErrorHandler errorHandler;

    @NonNull
    private final BaseAppDataCreator creator;

    @NonNull
    private final DBDeletingInterface deletingInterface;

    @NonNull
    private final DBFetchingInterface fetchingInterface;

    @NonNull
    private final DBSavingInterface savingInterface;

    @NonNull
    private final DBUpdatingInterface updatingInterface;

    public BackendModule(@NonNull final Eventing eventing, @NonNull final BaseAppDataCreator creator, @NonNull final ErrorHandler errorHandler,DBDeletingInterface deletingInterface, DBFetchingInterface fetchingInterface, DBSavingInterface savingInterface, DBUpdatingInterface updatingInterface) {
        this.eventing = eventing;
        this.creator = creator;
        this.errorHandler = errorHandler;
        this.deletingInterface = deletingInterface;
        this.fetchingInterface = fetchingInterface;
        this.savingInterface = savingInterface;
        this.updatingInterface = updatingInterface;
    }

    @Provides
    OkHttpClient provideOkHttpClient(@NonNull final List<Interceptor> interceptors) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        for (Interceptor i : interceptors) {
            okHttpClient.networkInterceptors().add(i);
        }
        return okHttpClient;
    }

    @Provides
    RestAdapter.Builder provideRestAdapterBuilder() {
        return new RestAdapter.Builder();
    }

    @Provides
    @Singleton
    Backend providesBackend(
            @NonNull final MomentsMonitor momentsMonitor,
            @NonNull final ConsentsMonitor consentsMonitor) {
        return new Backend(momentsMonitor, consentsMonitor);
    }

    @Provides
    @Singleton
    DataPullSynchronise providesDataSynchronise(
            @NonNull final MomentsDataFetcher momentsDataFetcher,
            @NonNull final ConsentsDataFetcher consentsDataFetcher,@NonNull final ExecutorService executor) {

        return new DataPullSynchronise(Arrays.asList(momentsDataFetcher,consentsDataFetcher), executor);
    }

    //TODO: Spoorti: Can this move out so that we can support senders and fetchers from Application
    @Provides
    @Singleton
    DataPushSynchronise providesDataPushSynchronise(
            @NonNull final MomentsDataSender momentsDataSender,
            @NonNull final ConsentDataSender consentDataSender) {
        return new DataPushSynchronise(Arrays.asList(momentsDataSender,consentDataSender),
                null);
    }

    @Provides
    GsonConverter providesGsonConverter() {
        return new GsonConverter(new Gson());
    }

    @Provides
    MomentGsonConverter providesMomentsGsonConverter() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return new MomentGsonConverter(gson);
    }

    @Provides
    UCoreAdapter providesUCoreAdapter(OkClientFactory okClientFactory, RestAdapter.Builder restAdapterBuilder, Context context) {
        return new UCoreAdapter(okClientFactory, restAdapterBuilder, context);
    }

    @Provides
    @Singleton
    public Eventing provideEventing() {
        return eventing;
    }

    @Provides
    @Singleton
    public BaseAppDataCreator provideCreater() {
        return creator;
    }

    @Provides
    @Singleton
    public DBMonitors providesDMMonitors(){
        SavingMonitor savingMonitor = new SavingMonitor(savingInterface);
        FetchingMonitor fetchMonitor = new FetchingMonitor(fetchingInterface);
        DeletingMonitor deletingMonitor = new DeletingMonitor(deletingInterface);
        UpdatingMonitor updatingMonitor = new UpdatingMonitor(updatingInterface, deletingInterface, fetchingInterface);

        return new DBMonitors(Arrays.asList(savingMonitor, fetchMonitor, deletingMonitor, updatingMonitor));
    }

    @Provides
    @Singleton
    public BaseAppCore providesCore(){
        return  new BaseAppCore();
    }

    @Provides
    public ErrorHandler providesErrorHandler(){
        return  errorHandler;
    }

    @Provides
    public UCoreAccessProvider providesAccessProvider(){
        return new UCoreAccessProvider(errorHandler);
    }
}