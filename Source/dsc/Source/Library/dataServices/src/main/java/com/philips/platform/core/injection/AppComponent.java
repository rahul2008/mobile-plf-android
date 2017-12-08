/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.injection;

import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.monitors.ErrorMonitor;
import com.philips.platform.core.monitors.FetchingMonitor;
import com.philips.platform.core.monitors.UpdatingMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.PushNotification.PushNotificationMonitor;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.characteristics.UserCharacteristicsConverter;
import com.philips.platform.datasync.characteristics.UserCharacteristicsFetcher;
import com.philips.platform.datasync.characteristics.UserCharacteristicsMonitor;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSegregator;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSender;
import com.philips.platform.datasync.consent.ConsentDataSender;
import com.philips.platform.datasync.consent.ConsentsConverter;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;
import com.philips.platform.datasync.consent.ConsentsMonitor;
import com.philips.platform.datasync.consent.ConsentsSegregator;
import com.philips.platform.datasync.devicePairing.DevicePairingMonitor;
import com.philips.platform.datasync.insights.InsightConverter;
import com.philips.platform.datasync.insights.InsightDataFetcher;
import com.philips.platform.datasync.insights.InsightDataSender;
import com.philips.platform.datasync.insights.InsightMonitor;
import com.philips.platform.datasync.insights.InsightSegregator;
import com.philips.platform.datasync.moments.MomentsConverter;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataSender;
import com.philips.platform.datasync.moments.MomentsSegregator;
import com.philips.platform.datasync.settings.SettingsConverter;
import com.philips.platform.datasync.settings.SettingsDataFetcher;
import com.philips.platform.datasync.settings.SettingsDataSender;
import com.philips.platform.datasync.settings.SettingsMonitor;
import com.philips.platform.datasync.settings.SettingsSegregator;
import com.philips.platform.datasync.subjectProfile.SubjectProfileMonitor;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;
import com.philips.platform.datasync.synchronisation.SynchronisationMonitor;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, BackendModule.class})
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

    void injectSynchronizationMonitor(SynchronisationMonitor synchronisationMonitor);

    void injectErrorMonitor(ErrorMonitor errorMonitor);

    void injectUpdatingMonitor(UpdatingMonitor updatingMonitor);

    void injectMomentsSegregator(MomentsSegregator momentsSegregator);

    void injectFetchingMonitor(FetchingMonitor fetchingMonitor);

    void injectConsentsSegregator(ConsentsSegregator consentsSegregator);

    void injectUserCharacteristicsMonitor(UserCharacteristicsMonitor userCharacteristicsMonitor);

    void injectUserCharacteristicsSender(UserCharacteristicsSender userCharacteristicsSender);

    void injectUserCharacteristicsFetcher(UserCharacteristicsFetcher userCharacteristicsFetcher);

    void injectUserCharacteristicsConverter(UserCharacteristicsConverter userCharacteristicsConverter);

    void injectUserCharacteristicsSegregator(UserCharacteristicsSegregator userCharacteristicsSegregator);

    void injectUserSettingsMonitor(SettingsMonitor settingsMonitor);

    void injectSettingsDataSender(SettingsDataSender settingsDataSender);

    void injectSettingsDataFetcher(SettingsDataFetcher settingsDataFetcher);

    void injectSettingsConverter(SettingsConverter settingsConverter);

    void injectSettingsSegregator(SettingsSegregator segregator);

    void injectSynchronisationManager(SynchronisationManager synchronisationManager);

    void injectDataFetcher(DataFetcher dataFetcher);

    void injectDataSender(DataSender tDataSender);

    void injectInsightSegregator(InsightSegregator insightSegregator);

    void injectInsightDataSender(InsightDataSender insightDataSender);

    void injectInsightConverter(InsightConverter insightConverter);

    void injectInsightsDataFetcher(InsightDataFetcher insightDataFetcher);

    void injectInsightMonitor(InsightMonitor insightMonitor);

    void injectPushNotificationMonitor(PushNotificationMonitor pushNotificationMonitor);

    void injectDevicePairingMonitor(DevicePairingMonitor devicePairingMonitor);

    void injectSubjectProfileMonitor(SubjectProfileMonitor subjectProfileMonitor);
}