package com.philips.platform.core.injection;

import android.content.Context;

import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.ErrorMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.MomentGsonConverter;
import com.philips.platform.datasync.OkClientFactory;
import com.philips.platform.datasync.PushNotification.PushNotificationMonitor;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
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
import com.philips.platform.verticals.VerticalCreater;
import com.philips.platform.verticals.VerticalDBDeletingInterfaceImpl;
import com.philips.platform.verticals.VerticalDBFetchingInterfaceImpl;
import com.philips.platform.verticals.VerticalDBSavingInterface;
import com.philips.platform.verticals.VerticalDBUpdatingInterfaceImpl;
import com.philips.platform.verticals.VerticalUserRegistrationInterfaceImpl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataServicesManager.class})
public class BackendModuleTest {

    private BackendModule backendModule;

    @Mock
    private DataServicesManager dscMock;
    @Mock
    Eventing eventingMock;
    @Mock
    ConsentsMonitor consentsMonitor;
    @Mock
    UserCharacteristicsMonitor userCharacteristicsMonitor;
    @Mock
    PushNotificationMonitor pushNotificationMonitor;
    @Mock
    SettingsMonitor settingsMonitor;
    @Mock
    InsightMonitor mInsightMonitor;

    @Mock
    DevicePairingMonitor devicePairingMonitor;

    @Mock
    SubjectProfileMonitor subjectProfileMonitor;

    ExecutorService executorService;

    @Mock
    MomentsDataFetcher momentsDataFetcher;

    @Mock
    UserCharacteristicsFetcher userCharacteristicsFetcher;
    @Mock
    InsightDataFetcher mInsightDataFetcher;
    @Mock
    SettingsDataFetcher settingsDataFetcher;
    @Mock
    ConsentsDataFetcher consentsDataFetcher;

    @Mock
    MomentsDataSender momentsDataSender;

    @Mock
    ConsentDataSender consentDataSender;
    @Mock
    UserCharacteristicsSender userCharacteristicsSender;
    @Mock
    InsightDataSender mInsightDataSender;
    @Mock
    SettingsDataSender settingsDataSender;

    @Mock
    OkClientFactory okClientFactory;
    @Mock
    RestAdapter.Builder builder;
    @Mock
    Context context;

    @Mock
    SynchronisationCompleteListener mSynchronisationCompleteListener;

    @Mock
    private AppComponent appComponantMock;
    @Mock
    ArrayList<DataFetcher> fetchers;

    @Mock
    ArrayList<DataSender> senders;

    @Mock
    ErrorHandlingInterface errorHandlingInterface;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        // Static mock for DSC Singleton
        PowerMockito.mockStatic(DataServicesManager.class);
        when(DataServicesManager.getInstance()).thenReturn(dscMock);
        when(dscMock.getDataServiceContext()).thenReturn(context);
        when(dscMock.getAppComponent()).thenReturn(appComponantMock);

        VerticalCreater baseAppDataCreator = new VerticalCreater();
        VerticalUserRegistrationInterfaceImpl userRegistrationInterface = new VerticalUserRegistrationInterfaceImpl();
        VerticalDBDeletingInterfaceImpl dbDeletingInterface = new VerticalDBDeletingInterfaceImpl();
        VerticalDBFetchingInterfaceImpl dbFetchingInterface = new VerticalDBFetchingInterfaceImpl();
        VerticalDBSavingInterface dbSavingInterface = new VerticalDBSavingInterface();
        VerticalDBUpdatingInterfaceImpl dbUpdatingInterface = new VerticalDBUpdatingInterfaceImpl();
        DataServicesManager.getInstance().setAppComponent(appComponantMock);

        backendModule = new BackendModule(eventingMock, baseAppDataCreator, userRegistrationInterface,
                dbDeletingInterface, dbFetchingInterface, dbSavingInterface, dbUpdatingInterface,
                fetchers, senders, errorHandlingInterface);
    }

    @Test
    public void ShouldReturnOkHttpClient_WhenProvideOkHttpClientIsCalled() throws Exception {
        final OkHttpClient okHttpClient = backendModule.provideOkHttpClient(Collections.<Interceptor>emptyList());

        assertThat(okHttpClient).isNotNull();
        assertThat(okHttpClient).isInstanceOf(OkHttpClient.class);
    }

    @Test
    public void ShouldReturnOkHttpClient_WhenProvideOkHttpClientWithListIsCalled() throws Exception {
        List<Interceptor> list = new ArrayList<>();
        list.add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return null;
            }
        });
        final OkHttpClient okHttpClient = backendModule.provideOkHttpClient(list);

        assertThat(okHttpClient).isNotNull();
        assertThat(okHttpClient).isInstanceOf(OkHttpClient.class);
    }

    @Test
    public void ShouldReturnRestAdapterBuilder_WhenProvideRestAdapterBuilderIsCalled() throws Exception {
        final RestAdapter.Builder restAdapterBuilder = backendModule.provideRestAdapterBuilder();

        assertThat(restAdapterBuilder).isNotNull();
        assertThat(restAdapterBuilder).isInstanceOf(RestAdapter.Builder.class);
    }

    @Test
    public void ShouldReturnGsonConverter_WhenProvidesGsonConverterIsCalled() throws Exception {
        final GsonConverter gsonConverter = backendModule.providesGsonConverter();

        assertThat(gsonConverter).isNotNull();
        assertThat(gsonConverter).isInstanceOf(GsonConverter.class);
    }

    @Test
    public void ShouldReturnMomentGsonConverter_WhenProvidesMomentsGsonConverterIsCalled() throws Exception {
        final MomentGsonConverter momentGsonConverter = backendModule.providesMomentsGsonConverter();

        assertThat(momentGsonConverter).isNotNull();
        assertThat(momentGsonConverter).isInstanceOf(MomentGsonConverter.class);
    }

    @Test
    public void ShouldReturnBackend_WhenProvidesBackendIsCalled() throws Exception {
        final Backend backend = backendModule.providesBackend(consentsMonitor, userCharacteristicsMonitor, settingsMonitor, mInsightMonitor, pushNotificationMonitor, devicePairingMonitor, subjectProfileMonitor);
        assertThat(backend).isNotNull();
        assertThat(backend).isInstanceOf(Backend.class);
    }

    @Test
    public void ShouldReturnBackend_WhenProvidesDMMonitorsIsCalled() throws Exception {
        DBMonitors dbMonitors = backendModule.providesDMMonitors();
        assertThat(dbMonitors).isNotNull();
        assertThat(dbMonitors).isInstanceOf(DBMonitors.class);
    }

    @Test
    public void ShouldReturnBackend_WhenProvidesErrorMonitorIsCalled() throws Exception {
        ErrorMonitor errorMonitor = backendModule.providesErrorMonitor();
        assertThat(errorMonitor).isNotNull();
        assertThat(errorMonitor).isInstanceOf(ErrorMonitor.class);
    }

    @Test
    public void ShouldReturnBackend_WhenProvidesCoreIsCalled() throws Exception {
        BaseAppCore core = backendModule.providesCore();
        assertThat(core).isNotNull();
        assertThat(core).isInstanceOf(BaseAppCore.class);
    }

    @Test
    public void ShouldReturnBackend_WhenProvidesUserRegistrationInterfaceIsCalled() throws Exception {
        UserRegistrationInterface userRegistrationInterface = backendModule.providesUserRegistrationInterface();
        assertThat(userRegistrationInterface).isNotNull();
        assertThat(userRegistrationInterface).isInstanceOf(UserRegistrationInterface.class);
    }

    @Test
    public void ShouldReturnBackend_WhenProvidesAccessProviderIsCalled() throws Exception {
        UCoreAccessProvider uCoreAccessProvider = backendModule.providesAccessProvider();
        assertThat(uCoreAccessProvider).isNotNull();
        assertThat(uCoreAccessProvider).isInstanceOf(UCoreAccessProvider.class);
    }

    @Test
    public void ShouldReturnBackend_WhenProvidesErrorHandlingInterfaceIsCalled() throws Exception {
        ErrorHandlingInterface errorHandlingInterface = backendModule.providesErrorHandlingInterface();
        assertThat(errorHandlingInterface).isNotNull();
        assertThat(errorHandlingInterface).isInstanceOf(ErrorHandlingInterface.class);
    }

    @Test
    public void ShouldReturnPullSynchronise_WhenProvideCreaterIsCalled() throws Exception {
        BaseAppDataCreator baseAppDataCreator = backendModule.provideCreater();
        assertThat(baseAppDataCreator).isNotNull();
        assertThat(baseAppDataCreator).isInstanceOf(BaseAppDataCreator.class);
    }

    @Test
    public void ShouldReturnDataPullSynchronise_WhenProvidesDataPullSynchroniseIsCalled() throws Exception {
        final DataPullSynchronise dataPullSynchronise = backendModule.providesDataSynchronise(momentsDataFetcher,
                consentsDataFetcher, userCharacteristicsFetcher, settingsDataFetcher, mInsightDataFetcher);
        assertThat(dataPullSynchronise).isNotNull();
        assertThat(dataPullSynchronise).isInstanceOf(DataPullSynchronise.class);
    }

    @Test
    public void ShouldReturnDataPushSynchronise_WhenProvidesDataPushSynchroniseIsCalled() throws Exception {
        final DataPushSynchronise dataPushSynchronise = backendModule.providesDataPushSynchronise(momentsDataSender,
                consentDataSender, userCharacteristicsSender, settingsDataSender, mInsightDataSender);
        assertThat(dataPushSynchronise).isNotNull();
        assertThat(dataPushSynchronise).isInstanceOf(DataPushSynchronise.class);
    }


    @Test
    public void ShouldReturnUCoreAdapter_WhenProvidesUCoreAdapterIsCalled() throws Exception {
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        final UCoreAdapter uCoreAdapter = backendModule.providesUCoreAdapter(okClientFactory, builder, context);
        assertThat(uCoreAdapter).isNotNull();
        assertThat(uCoreAdapter).isInstanceOf(UCoreAdapter.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesEventingIsCalled() throws Exception {
        final Eventing eventing = backendModule.provideEventing();
        assertThat(eventing).isNotNull();
        assertThat(eventing).isInstanceOf(Eventing.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesSynchronizationMonitorIsCalled() throws Exception {
        SynchronisationMonitor synchronisationMonitor = backendModule.providesSynchronizationMonitor();
        assertThat(synchronisationMonitor).isNotNull();
        assertThat(synchronisationMonitor).isInstanceOf(SynchronisationMonitor.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesMomentsSegregaterIsCalled() throws Exception {
        MomentsSegregator momentsSegregator = backendModule.providesMomentsSegregater();
        assertThat(momentsSegregator).isNotNull();
        assertThat(momentsSegregator).isInstanceOf(MomentsSegregator.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesFetchigImplementationIsCalled() throws Exception {
        DBFetchingInterface dbFetchingInterface = backendModule.providesFetchigImplementation();
        assertThat(dbFetchingInterface).isNotNull();
        assertThat(dbFetchingInterface).isInstanceOf(DBFetchingInterface.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesUpdatingImplementationIsCalled() throws Exception {
        DBUpdatingInterface dbUpdatingInterface = backendModule.providesUpdatingImplementation();
        assertThat(dbUpdatingInterface).isNotNull();
        assertThat(dbUpdatingInterface).isInstanceOf(DBUpdatingInterface.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesDeletingImplementationIsCalled() throws Exception {
        DBDeletingInterface dbDeletingInterface = backendModule.providesDeletingImplementation();
        assertThat(dbDeletingInterface).isNotNull();
        assertThat(dbDeletingInterface).isInstanceOf(DBDeletingInterface.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesSettingsSegregaterIsCalled() throws Exception {
        SettingsSegregator settingsSegregator = backendModule.providesSettingsSegregater();
        assertThat(settingsSegregator).isNotNull();
        assertThat(settingsSegregator).isInstanceOf(SettingsSegregator.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesUserCharacteristicsSegregatorIsCalled() throws Exception {
        UserCharacteristicsSegregator userCharacteristicsSegregator = backendModule.providesUserCharacteristicsSegregator();
        assertThat(userCharacteristicsSegregator).isNotNull();
        assertThat(userCharacteristicsSegregator).isInstanceOf(UserCharacteristicsSegregator.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesConsentsSegregaterIsCalled() throws Exception {
        ConsentsSegregator consentsSegregator = backendModule.providesConsentsSegregater();
        assertThat(consentsSegregator).isNotNull();
        assertThat(consentsSegregator).isInstanceOf(ConsentsSegregator.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesSavingImplementationIsCalled() throws Exception {
        DBSavingInterface dbSavingInterface = backendModule.providesSavingImplementation();
        assertThat(dbSavingInterface).isNotNull();
        assertThat(dbSavingInterface).isInstanceOf(DBSavingInterface.class);
    }

    @Test
    public void ShouldReturnEventing_WhenProvidesSynchronisationManagerIsCalled() throws Exception {
        SynchronisationManager synchronisationManager = backendModule.providesSynchronisationManager();
        assertThat(synchronisationManager).isNotNull();
        assertThat(synchronisationManager).isInstanceOf(SynchronisationManager.class);
    }

    @Test
    public void ShouldReturnEventing_WhenprovidesInsightSegregaterIsCalled() throws Exception {
        InsightSegregator insightSegregator = backendModule.providesInsightSegregater();
        assertThat(insightSegregator).isNotNull();
        assertThat(insightSegregator).isInstanceOf(InsightSegregator.class);
    }
}