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
import com.philips.platform.core.Eventing;
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
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
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

    public BackendModule(@NonNull final Eventing eventing) {
        this.eventing = eventing;
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
            @NonNull final ConsentsDataFetcher consentsDataFetcher,
            @NonNull final Eventing eventing, @NonNull final ExecutorService executor) {

        return new DataPullSynchronise(Arrays.asList(momentsDataFetcher,consentsDataFetcher), executor, eventing);
    }

    @Provides
    @Singleton
    DataPushSynchronise providesDataPushSynchronise(
            @NonNull final MomentsDataSender momentsDataSender,
            @NonNull final ConsentDataSender consentDataSender,
            @NonNull final Eventing eventing) {
        return new DataPushSynchronise(Arrays.asList(momentsDataSender,consentDataSender),
                null, eventing);
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
}