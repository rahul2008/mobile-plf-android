/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.core.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.platform.mya.catk.ConsentsClient;
import com.philips.platform.mya.catk.ConsentInteractor;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.DeletingMonitor;
import com.philips.platform.core.monitors.ErrorMonitor;
import com.philips.platform.core.monitors.FetchingMonitor;
import com.philips.platform.core.monitors.SavingMonitor;
import com.philips.platform.core.monitors.UpdatingMonitor;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.OkClientFactory;
import com.philips.platform.datasync.PushNotification.PushNotificationMonitor;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.UserAccessProvider;
import com.philips.platform.datasync.UserAccessProviderInteractor;
import com.philips.platform.datasync.characteristics.UserCharacteristicsFetcher;
import com.philips.platform.datasync.characteristics.UserCharacteristicsMonitor;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSegregator;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSender;
import com.philips.platform.datasync.consent.ConsentDataSender;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;
import com.philips.platform.datasync.consent.ConsentsMonitor;
import com.philips.platform.datasync.consent.ConsentsSegregator;
import com.philips.platform.datasync.devicePairing.DevicePairingMonitor;
import com.philips.platform.datasync.insights.InsightDataFetcher;
import com.philips.platform.datasync.insights.InsightDataSender;
import com.philips.platform.datasync.insights.InsightMonitor;
import com.philips.platform.datasync.insights.InsightSegregator;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataSender;
import com.philips.platform.datasync.moments.MomentsSegregator;
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
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@SuppressWarnings({"rawtypes", "unchecked"})
@Module
public class BackendModule {

    @NonNull
    private final Eventing eventing;

    @NonNull
    private final UserRegistrationInterface userRegistrationInterface;

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


    ArrayList<DataFetcher> fetchers;
    ArrayList<DataSender> senders;

    private ErrorHandlingInterface errorHandlingInterface;

    public BackendModule(@NonNull final Eventing eventing, @NonNull final BaseAppDataCreator creator,
                         @NonNull final UserRegistrationInterface userRegistrationInterface, DBDeletingInterface deletingInterface,
                         DBFetchingInterface fetchingInterface, DBSavingInterface savingInterface,
                         DBUpdatingInterface updatingInterface,
                         ArrayList<DataFetcher> fetchers, ArrayList<DataSender> senders,
                         ErrorHandlingInterface errorHandlingInterface) {
        this.fetchers = fetchers;
        this.senders = senders;
        this.eventing = eventing;
        this.creator = creator;
        this.userRegistrationInterface = userRegistrationInterface;
        this.deletingInterface = deletingInterface;
        this.fetchingInterface = fetchingInterface;
        this.savingInterface = savingInterface;
        this.updatingInterface = updatingInterface;
        this.errorHandlingInterface = errorHandlingInterface;
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
            @NonNull final ConsentsMonitor consentsMonitor,
            @NonNull final UserCharacteristicsMonitor userCharacteristicsMonitor,
            @NonNull final SettingsMonitor settingsMonitor, @NonNull final InsightMonitor insightMonitor, @NonNull final PushNotificationMonitor pushNotificationMonitor, @NonNull final DevicePairingMonitor devicePairingMonitor, @NonNull final SubjectProfileMonitor subjectProfileMonitor) {
        return new Backend(consentsMonitor, userCharacteristicsMonitor, settingsMonitor, insightMonitor, pushNotificationMonitor, devicePairingMonitor, subjectProfileMonitor);
    }

    @Provides
    @Singleton
    DataPullSynchronise providesDataSynchronise(
            @NonNull final MomentsDataFetcher momentsDataFetcher,
            @NonNull final ConsentsDataFetcher consentsDataFetcher,
            @NonNull final UserCharacteristicsFetcher userCharacteristicsFetcher,
            @NonNull final SettingsDataFetcher settingsDataFetcher,
            @NonNull final InsightDataFetcher insightDataFetcher) {
        List<DataFetcher> dataFetchers = Arrays.asList(momentsDataFetcher, consentsDataFetcher, userCharacteristicsFetcher, settingsDataFetcher, insightDataFetcher);
        if (fetchers != null && fetchers.size() != 0) {
            for (DataFetcher fetcher : fetchers) {
                dataFetchers.add(fetcher);
            }
        }
        return new DataPullSynchronise(dataFetchers);
    }

    @Provides
    @Singleton
    DataPushSynchronise providesDataPushSynchronise(
            @NonNull final MomentsDataSender momentsDataSender,
            @NonNull final ConsentDataSender consentDataSender,
            @NonNull final UserCharacteristicsSender userCharacteristicsSender,
            @NonNull final SettingsDataSender settingsDataSender,
            @NonNull final InsightDataSender insightDataSender) {

        List dataSenders = Arrays.asList(momentsDataSender, consentDataSender, userCharacteristicsSender, settingsDataSender, insightDataSender);
        if (senders != null && senders.size() != 0) {
            for (DataSender sender : senders) {
                dataSenders.add(sender);
            }
        }
        return new DataPushSynchronise(dataSenders
        );
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
    UCoreAdapter providesUCoreAdapter(OkClientFactory okClientFactory, RestAdapter.Builder
            restAdapterBuilder, Context context) {
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
    public DBMonitors providesDMMonitors() {
        SavingMonitor savingMonitor = new SavingMonitor(savingInterface, deletingInterface, updatingInterface);
        FetchingMonitor fetchMonitor = new FetchingMonitor(fetchingInterface);
        DeletingMonitor deletingMonitor = new DeletingMonitor(deletingInterface);
        UpdatingMonitor updatingMonitor = new UpdatingMonitor(updatingInterface, deletingInterface, fetchingInterface, savingInterface);

        return new DBMonitors(Arrays.asList(savingMonitor, fetchMonitor, deletingMonitor, updatingMonitor));
    }

    @Provides
    @Singleton
    public ErrorMonitor providesErrorMonitor() {
        return new ErrorMonitor(errorHandlingInterface);
    }

    @Provides
    @Singleton
    public BaseAppCore providesCore() {
        return new BaseAppCore();
    }

    @Provides
    public UserRegistrationInterface providesUserRegistrationInterface() {
        return userRegistrationInterface;
    }

    @Provides
    public UCoreAccessProvider providesAccessProvider() {
        return new UCoreAccessProvider(userRegistrationInterface);
    }

    @Provides
    public ErrorHandlingInterface providesErrorHandlingInterface() {
        return errorHandlingInterface;
    }

    @Provides
    public SynchronisationMonitor providesSynchronizationMonitor() {
        return new SynchronisationMonitor();
    }

    @Provides
    public MomentsSegregator providesMomentsSegregater() {
        return new MomentsSegregator();
    }

    @Provides
    public ConsentsSegregator providesConsentsSegregater() {
        return new ConsentsSegregator();
    }

    @Provides
    public InsightSegregator providesInsightSegregater() {
        return new InsightSegregator();
    }

    @Provides
    public SettingsSegregator providesSettingsSegregater() {
        return new SettingsSegregator();
    }

    @Provides
    public UserCharacteristicsSegregator providesUserCharacteristicsSegregator() {
        return new UserCharacteristicsSegregator();
    }

    @Provides
    public DBFetchingInterface providesFetchigImplementation() {
        return fetchingInterface;
    }

    @Provides
    public DBUpdatingInterface providesUpdatingImplementation() {
        return updatingInterface;
    }

    @Provides
    public DBDeletingInterface providesDeletingImplementation() {
        return deletingInterface;
    }

    @Provides
    public DBSavingInterface providesSavingImplementation() {
        return savingInterface;
    }

    @Provides
    @Singleton
    public SynchronisationManager providesSynchronisationManager() {
        return new SynchronisationManager();
    }

    @Provides
    public UserAccessProvider providesUserAccessProvider(@NonNull final UCoreAccessProvider uCoreAccessProvider) {
        return new UserAccessProviderInteractor(uCoreAccessProvider);
    }

    @Provides
    @Singleton
    public static ConsentsClient providesConsentsClient() {
        return ConsentsClient.getInstance();
    }

    @Provides
    @Singleton
    public static ConsentInteractor providesGetConsentInteractor(@NonNull final ConsentsClient consentsClient) {
        return new ConsentInteractor(consentsClient);
    }
}