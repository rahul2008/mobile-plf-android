package cdp.philips.com.mydemoapp.injection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import cdp.philips.com.mydemoapp.DataSyncApplicationClass;
import cdp.philips.com.mydemoapp.DataSyncApplicationClass_MembersInjector;
import cdp.philips.com.mydemoapp.database.Database;
import cdp.philips.com.mydemoapp.datasync.temperature.TemperatureTimeLineFragment;
import cdp.philips.com.mydemoapp.datasync.temperature.TemperatureTimeLineFragment_MembersInjector;
import cdp.philips.com.mydemoapp.datasync.trackers.BaseTracker_MembersInjector;
import cdp.philips.com.mydemoapp.datasync.trackers.DaggerAppComponent_PackageProxy;
import cdp.philips.com.mydemoapp.datasync.trackers.TemperatureTracker;
import cdp.philips.com.mydemoapp.registration.RegistrationLaunchHelperWrapper_Factory;
import cdp.philips.com.mydemoapp.registration.UserRegistrationFacadeImpl;
import cdp.philips.com.mydemoapp.registration.UserRegistrationFacadeImpl_Factory;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.HSDPInfo;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.monitors.ExceptionMonitor;
import com.philips.platform.core.monitors.ExceptionMonitor_Factory;
import com.philips.platform.core.monitors.LoggingMonitor;
import com.philips.platform.core.monitors.LoggingMonitor_Factory;
import com.philips.platform.core.trackers.Tracker;
import com.philips.platform.core.trackers.Tracker_Factory;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.OkClientFactory_Factory;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAccessProvider_Factory;
import com.philips.platform.datasync.UCoreAccessProvider_MembersInjector;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.conversion.MeasurementDetailValueMap_Factory;
import com.philips.platform.datasync.conversion.MomentTypeMap_Factory;
import com.philips.platform.datasync.moments.MomentsConverter;
import com.philips.platform.datasync.moments.MomentsConverter_Factory;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataFetcher_Factory;
import com.philips.platform.datasync.moments.MomentsDataSender;
import com.philips.platform.datasync.moments.MomentsDataSender_Factory;
import com.philips.platform.datasync.moments.MomentsMonitor;
import com.philips.platform.datasync.moments.MomentsMonitor_Factory;
import com.philips.platform.datasync.synchronisation.DataPullSynchronise;
import com.philips.platform.datasync.synchronisation.DataPushSynchronise;
import com.philips.platform.datasync.userprofile.UserRegistrationFacade;
import dagger.MembersInjector;
import dagger.internal.MembersInjectors;
import dagger.internal.ScopedProvider;
import java.util.concurrent.ExecutorService;
import javax.annotation.Generated;
import javax.inject.Provider;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class DaggerAppComponent implements AppComponent {
  private Provider<SharedPreferences> provideSharedPreferencesProvider;
  private MembersInjector<UCoreAccessProvider> uCoreAccessProviderMembersInjector;
  private Provider<Context> providesContextProvider;
  private Provider<User> getUserProvider;
  private Provider<RegistrationHelper> providesRegistrationHelperProvider;
  private Provider<Eventing> provideEventingProvider;
  private Provider<RegistrationConfiguration> providesRegistrationConfigurationProvider;
  private Provider<HSDPInfo> providedHSDPInfoProvider;
  private Provider<UserRegistrationFacadeImpl> userRegistrationFacadeImplProvider;
  private Provider<UserRegistrationFacade> providesUserRegistrationFacadeProvider;
  private Provider<UCoreAccessProvider> uCoreAccessProvider;
  private Provider<RestAdapter.Builder> provideRestAdapterBuilderProvider;
  private Provider<UCoreAdapter> providesUCoreAdapterProvider;
  private Provider<BaseAppDataCreator> providesDataCreatorProvider;
  private Provider<MomentsConverter> momentsConverterProvider;
  private Provider<MomentGsonConverter> providesMomentsGsonConverterProvider;
  private Provider<MomentsDataSender> momentsDataSenderProvider;
  private Provider<MomentsMonitor> momentsMonitorProvider;
  private Provider<Backend> providesBackendProvider;
  private Provider<Database> providesDatabaseProvider;
  private Provider<LoggingMonitor> loggingMonitorProvider;
  private Provider<Handler> providesHandlerProvider;
  private Provider<ExceptionMonitor> exceptionMonitorProvider;
  private Provider<BaseAppCore> provideCoreProvider;
  private MembersInjector<DataSyncApplicationClass> dataSyncApplicationClassMembersInjector;
  private Provider<BackendIdProvider> provideBackendIdProvider;
  private Provider<Tracker> trackerProvider;
  private final DaggerAppComponent_PackageProxy cdp_philips_com_mydemoapp_datasync_trackers_Proxy = new DaggerAppComponent_PackageProxy();
  private MembersInjector<TemperatureTracker> temperatureTrackerMembersInjector;
  private Provider<GsonConverter> providesGsonConverterProvider;
  private Provider<MomentsDataFetcher> momentsDataFetcherProvider;
  private Provider<ExecutorService> provideBackgroundExecutorProvider;
  private Provider<DataPullSynchronise> providesDataSynchroniseProvider;
  private Provider<DataPushSynchronise> providesDataPushSynchroniseProvider;
  private MembersInjector<TemperatureTimeLineFragment> temperatureTimeLineFragmentMembersInjector;

  private DaggerAppComponent(Builder builder) {  
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {  
    return new Builder();
  }

  private void initialize(final Builder builder) {  
    this.provideSharedPreferencesProvider = ScopedProvider.create(ApplicationModule_ProvideSharedPreferencesFactory.create(builder.applicationModule));
    this.uCoreAccessProviderMembersInjector = UCoreAccessProvider_MembersInjector.create(provideSharedPreferencesProvider);
    this.providesContextProvider = ScopedProvider.create(ApplicationModule_ProvidesContextFactory.create(builder.applicationModule));
    this.getUserProvider = RegistrationModule_GetUserFactory.create(builder.registrationModule, providesContextProvider);
    this.providesRegistrationHelperProvider = RegistrationModule_ProvidesRegistrationHelperFactory.create(builder.registrationModule);
    this.provideEventingProvider = ScopedProvider.create(CoreModule_ProvideEventingFactory.create(builder.coreModule));
    this.providesRegistrationConfigurationProvider = RegistrationModule_ProvidesRegistrationConfigurationFactory.create(builder.registrationModule);
    this.providedHSDPInfoProvider = BackendModule_ProvidedHSDPInfoFactory.create(builder.backendModule);
    this.userRegistrationFacadeImplProvider = ScopedProvider.create(UserRegistrationFacadeImpl_Factory.create(providesContextProvider, RegistrationLaunchHelperWrapper_Factory.create(), getUserProvider, providesRegistrationHelperProvider, provideEventingProvider, providesRegistrationConfigurationProvider, providedHSDPInfoProvider));
    this.providesUserRegistrationFacadeProvider = RegistrationModule_ProvidesUserRegistrationFacadeFactory.create(builder.registrationModule, userRegistrationFacadeImplProvider);
    this.uCoreAccessProvider = ScopedProvider.create(UCoreAccessProvider_Factory.create(uCoreAccessProviderMembersInjector, providesUserRegistrationFacadeProvider));
    this.provideRestAdapterBuilderProvider = BackendModule_ProvideRestAdapterBuilderFactory.create(builder.backendModule);
    this.providesUCoreAdapterProvider = BackendModule_ProvidesUCoreAdapterFactory.create(builder.backendModule, OkClientFactory_Factory.create(), provideRestAdapterBuilderProvider, providesContextProvider);
    this.providesDataCreatorProvider = ScopedProvider.create(DatabaseModule_ProvidesDataCreatorFactory.create(builder.databaseModule));
    this.momentsConverterProvider = MomentsConverter_Factory.create(MomentTypeMap_Factory.create(), MeasurementDetailValueMap_Factory.create(), providesDataCreatorProvider);
    this.providesMomentsGsonConverterProvider = BackendModule_ProvidesMomentsGsonConverterFactory.create(builder.backendModule);
    this.momentsDataSenderProvider = MomentsDataSender_Factory.create(uCoreAccessProvider, providesUCoreAdapterProvider, momentsConverterProvider, providesDataCreatorProvider, providesMomentsGsonConverterProvider, provideEventingProvider);
    this.momentsMonitorProvider = MomentsMonitor_Factory.create((MembersInjector) MembersInjectors.noOp(), momentsDataSenderProvider);
    this.providesBackendProvider = ScopedProvider.create(BackendModule_ProvidesBackendFactory.create(builder.backendModule, momentsMonitorProvider));
    this.providesDatabaseProvider = ScopedProvider.create(DatabaseModule_ProvidesDatabaseFactory.create(builder.databaseModule));
    this.loggingMonitorProvider = LoggingMonitor_Factory.create((MembersInjector) MembersInjectors.noOp());
    this.providesHandlerProvider = ApplicationModule_ProvidesHandlerFactory.create(builder.applicationModule);
    this.exceptionMonitorProvider = ExceptionMonitor_Factory.create((MembersInjector) MembersInjectors.noOp(), providesHandlerProvider);
    this.provideCoreProvider = ScopedProvider.create(CoreModule_ProvideCoreFactory.create(builder.coreModule, providesDatabaseProvider, providesBackendProvider, loggingMonitorProvider, exceptionMonitorProvider));
    this.dataSyncApplicationClassMembersInjector = DataSyncApplicationClass_MembersInjector.create((MembersInjector) MembersInjectors.noOp(), providesBackendProvider, provideCoreProvider, provideEventingProvider, providesUserRegistrationFacadeProvider);
    this.provideBackendIdProvider = ScopedProvider.create(CoreModule_ProvideBackendIdProviderFactory.create(builder.coreModule, uCoreAccessProvider));
    this.trackerProvider = Tracker_Factory.create(provideEventingProvider, providesDataCreatorProvider, provideBackendIdProvider);
    this.cdp_philips_com_mydemoapp_datasync_trackers_Proxy.baseTrackerMembersInjector = BaseTracker_MembersInjector.create(trackerProvider, provideEventingProvider);
    this.temperatureTrackerMembersInjector = MembersInjectors.delegatingTo(cdp_philips_com_mydemoapp_datasync_trackers_Proxy.baseTrackerMembersInjector);
    this.providesGsonConverterProvider = BackendModule_ProvidesGsonConverterFactory.create(builder.backendModule);
    this.momentsDataFetcherProvider = MomentsDataFetcher_Factory.create((MembersInjector) MembersInjectors.noOp(), providesUCoreAdapterProvider, uCoreAccessProvider, momentsConverterProvider, provideEventingProvider, providesGsonConverterProvider);
    this.provideBackgroundExecutorProvider = ScopedProvider.create(ApplicationModule_ProvideBackgroundExecutorFactory.create(builder.applicationModule));
    this.providesDataSynchroniseProvider = ScopedProvider.create(BackendModule_ProvidesDataSynchroniseFactory.create(builder.backendModule, uCoreAccessProvider, momentsDataFetcherProvider, provideEventingProvider, provideBackgroundExecutorProvider));
    this.providesDataPushSynchroniseProvider = ScopedProvider.create(BackendModule_ProvidesDataPushSynchroniseFactory.create(builder.backendModule, uCoreAccessProvider, momentsDataSenderProvider, provideEventingProvider));
    this.temperatureTimeLineFragmentMembersInjector = TemperatureTimeLineFragment_MembersInjector.create((MembersInjector) MembersInjectors.noOp(), provideEventingProvider, providesDataSynchroniseProvider, providesDataPushSynchroniseProvider);
  }

  @Override
  public void injectApplication(DataSyncApplicationClass app) {  
    dataSyncApplicationClassMembersInjector.injectMembers(app);
  }

  @Override
  public Eventing getEventing() {  
    return provideEventingProvider.get();
  }

  @Override
  public BaseAppDataCreator getDataCreator() {  
    return providesDataCreatorProvider.get();
  }

  @Override
  public void injectTemperature(TemperatureTracker baseTracker) {  
    temperatureTrackerMembersInjector.injectMembers(baseTracker);
  }

  @Override
  public void injectFragment(TemperatureTimeLineFragment temperatureTimeLineFragment) {  
    temperatureTimeLineFragmentMembersInjector.injectMembers(temperatureTimeLineFragment);
  }

  public static final class Builder {
    private ApplicationModule applicationModule;
    private CoreModule coreModule;
    private DatabaseModule databaseModule;
    private BackendModule backendModule;
    private RegistrationModule registrationModule;
  
    private Builder() {  
    }
  
    public AppComponent build() {  
      if (applicationModule == null) {
        throw new IllegalStateException("applicationModule must be set");
      }
      if (coreModule == null) {
        throw new IllegalStateException("coreModule must be set");
      }
      if (databaseModule == null) {
        throw new IllegalStateException("databaseModule must be set");
      }
      if (backendModule == null) {
        this.backendModule = new BackendModule();
      }
      if (registrationModule == null) {
        this.registrationModule = new RegistrationModule();
      }
      return new DaggerAppComponent(this);
    }
  
    public Builder applicationModule(ApplicationModule applicationModule) {  
      if (applicationModule == null) {
        throw new NullPointerException("applicationModule");
      }
      this.applicationModule = applicationModule;
      return this;
    }
  
    public Builder coreModule(CoreModule coreModule) {  
      if (coreModule == null) {
        throw new NullPointerException("coreModule");
      }
      this.coreModule = coreModule;
      return this;
    }
  
    public Builder databaseModule(DatabaseModule databaseModule) {  
      if (databaseModule == null) {
        throw new NullPointerException("databaseModule");
      }
      this.databaseModule = databaseModule;
      return this;
    }
  
    public Builder backendModule(BackendModule backendModule) {  
      if (backendModule == null) {
        throw new NullPointerException("backendModule");
      }
      this.backendModule = backendModule;
      return this;
    }
  
    public Builder registrationModule(RegistrationModule registrationModule) {  
      if (registrationModule == null) {
        throw new NullPointerException("registrationModule");
      }
      this.registrationModule = registrationModule;
      return this;
    }
  }
}

