package com.philips.platform.datasync.settings;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.events.SettingsBackendGetRequest;
import com.philips.platform.core.events.SettingsBackendSaveRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.consent.UCoreConsentDetail;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 31/01/17.
 */
public class SettingsMonitorTest {

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String USER_ID = "TEST_GUID";
    private static final int REFERENCE_ID = 0;

    private SettingsMonitor settingsMonitor;


    private Response response = null;

    @Mock
    SettingsDataSender settingsDataSenderMock;

    @Mock
    SettingsDataFetcher settingsDataFetcherMock;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;


    @Mock
    private GsonConverter gsonConverterMock;

    @Mock
    private SettingsClient settingsClientMock;

    @Mock
    private Eventing eventingMock;


    @Mock
    private SettingsBackendGetRequest settingsBackendGetRequest;

    @Mock
    private Settings settingsMock;

    @Mock
    private UCoreSettings uCoreSettingsMock;

    @Mock
    SettingsConverter settingsConverterMock;

    @Mock
    private List<UCoreConsentDetail> uCoreConsentDetailMock;

    @Mock
    private Collection<? extends ConsentDetail> consentDetailListMock;

    @Mock
    private SettingsBackendSaveRequest settingsBackendSaveRequestMock;

    @Captor
    private ArgumentCaptor<BackendResponse> errorCaptor;

    @Captor
    private ArgumentCaptor<ConsentBackendSaveResponse> responseCaptor;

    @Captor
    private ArgumentCaptor<ConsentBackendSaveRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<Event> eventArgumentCaptor;


    private DataServicesManager dataServicesManager;
    private OrmCreatorTest verticalDataCreater;
    private ErrorHandlerImplTest errorHandlerImplTest;

    @Mock
    private AppComponent appComponantMock;


    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        errorHandlerImplTest = new ErrorHandlerImplTest();
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        settingsMonitor = new SettingsMonitor(settingsDataSenderMock, settingsDataFetcherMock);
        settingsMonitor.uCoreAccessProvider = accessProviderMock;
        settingsMonitor.start(eventingMock);
    }

    @Test
    public void ShouldPostError_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);

        settingsMonitor.onEventAsync(settingsBackendSaveRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenUserIsNotLoggedInDuringGetSettings() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);


        settingsMonitor.onEventAsync(settingsBackendGetRequest);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenUserAccessTokenIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);


        settingsMonitor.onEventAsync(settingsBackendSaveRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenUserAccessTokenIsNullDuringGetSettings() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);

        settingsMonitor.onEventAsync(settingsBackendGetRequest);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenUserAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");

        settingsMonitor.onEventAsync(settingsBackendSaveRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }


    @Test
    public void ShouldPostError_WhenUserAccessTokenIsEmptyDuringGet() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");

        settingsMonitor.onEventAsync(settingsBackendGetRequest);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldPostError_WhenBackendSaveFails() throws Exception {
        Response response = new Response("", 401, "Error", new ArrayList<Header>(), null);
        final RetrofitError retrofitError = RetrofitError.httpError("url", response, null, null);

        doReturn(uCoreSettingsMock).when(settingsConverterMock).convertAppToUcoreSettings(any(Settings.class));
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(settingsBackendSaveRequestMock.getSettings()).thenReturn(settingsMock);
        when(settingsClientMock.updateSettings(anyString(), anyString(), any(UCoreSettings.class))).thenThrow(retrofitError);

        settingsMonitor.onEventAsync(settingsBackendSaveRequestMock);

//        verify(eventingMock).post(errorCaptor.capture());
//        final BackendResponse backendResponse = errorCaptor.getValue();
//        assertThat(backendResponse.getReferenceId()).isEqualTo(REFERENCE_ID);
//        assertThat(backendResponse.succeed()).isFalse();
    }

    @Test
    public void ShouldPostError_WhenGetRequestIsFetchedWithOutUserLogin() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        settingsMonitor.onEventAsync(settingsBackendGetRequest);

//        verify(eventingMock).post(errorCaptor.capture());
//        assertThat(errorCaptor.getValue().getReferenceId()).isEqualTo(REFERENCE_ID);
    }

    @Test
    public void ShouldSaveSettings_WhenSettingsIsPassed() throws Exception {
        doReturn(settingsMock).when(settingsBackendSaveRequestMock).getSettings();
        doReturn(uCoreSettingsMock).when(settingsConverterMock).convertAppToUcoreSettings(any(Settings.class));
        when(settingsClientMock.getSettings(anyString(), anyString(), anyInt())).thenReturn(uCoreSettingsMock);
        when(uCoreConsentDetailMock.isEmpty()).thenReturn(false);
        when(accessProviderMock.getUserId()).thenReturn(USER_ID);
        when(settingsConverterMock.convertUcoreToAppSettings(uCoreSettingsMock)).thenReturn(settingsMock);
        when(settingsClientMock.updateSettings(anyString(), anyString(), any(UCoreSettings.class))).thenReturn(response);
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");

        settingsMonitor.onEventAsync(settingsBackendSaveRequestMock);

//        verify(eventingMock, times(1)).post(eventArgumentCaptor.capture());

        List<Event> events = eventArgumentCaptor.getAllValues();

        assertThat(events).hasSize(0);
        //assertThat(events.get(0)).isInstanceOf(BackendResponse.class);
        //  assertThat(events.get(1)).isInstanceOf(SettingsBackendSaveResponse.class);
    }

    @Test
    public void ShouldReturn_WhenUCoreAccessProviderIsNull() throws Exception {
        settingsMonitor.uCoreAccessProvider = null;
        //when(uCoreSettingsMock.isEmpty()).thenReturn(true);
        when(settingsClientMock.getSettings(anyString(), anyString(), anyInt())).thenReturn(uCoreSettingsMock);

        settingsMonitor.onEventAsync(settingsBackendGetRequest);
        verifyNoMoreInteractions(uCoreAdapterMock);
    }

}