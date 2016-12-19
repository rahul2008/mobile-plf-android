/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.injection;

import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.consent.ConsentDataSender;
import com.philips.platform.datasync.consent.ConsentsConverter;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;
import com.philips.platform.datasync.consent.ConsentsMonitor;
import com.philips.platform.datasync.moments.MomentsConverter;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataSender;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;

import javax.inject.Singleton;

import dagger.Component;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Singleton
@Component(modules = {ApplicationModule.class,BackendModule.class})
public interface AppComponent {

    void injectApplication(DataServicesManager dataServicesManager);

    void injectBaseAppCore(BaseAppCore baseAppCore);

    void injectDataPullSynchronize(DataPullSynchronise dataPullSynchronise);

    void injectAccessProvider(UCoreAccessProvider accessProvider);

    void injectMomentsDataFetcher(MomentsDataFetcher momentsDataFetcher);

    void injectMomentsDataSender(MomentsDataSender momentsDataSender);

    void injectDataPushSynchronize(DataPushSynchronise dataPushSynchronise);

    void injectUCoreAdapter(UCoreAdapter uCoreAdapter);

    void injectMomentsConverter(MomentsConverter momentsConverter);

    void injectConsentsMonitor(ConsentsMonitor consentsMonitor);

    void injectConsentsConverter(ConsentsConverter consentsConverter);

    void injectConsentsDataFetcher(ConsentsDataFetcher consentsDataFetcher);

    void injectConsentsSender(ConsentDataSender consentDataSender);
}