package com.philips.platform.datasync.settings;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.events.SettingsBackendSaveRequest;
import com.philips.platform.core.events.SettingsBackendSaveResponse;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedString;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 31/01/17.
 */
public class SettingsDataSenderTest {

    private SettingsDataSender settingsDataSender;

    @Mock
    private Eventing eventingMock;

    @Mock
    private UCoreAccessProvider uCoreAccessProviderMock;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Mock
    private GetNonSynchronizedDataResponse getNonSynchronizedDataResponseMock;

    @Mock
    private Settings settingsMock;

    @Mock
    private SettingsBackendSaveResponse settingsBackendSaveResponseMock;

    @Captor
    private ArgumentCaptor<SettingsBackendSaveRequest> settingsBackendSaveRequestEventCaptor;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    GsonConverter gsonConverterMock;

    @Mock
    SettingsConverter settingsConverterMock;

    @Mock
    UCoreAdapter uCoreAdapterMock;

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String TEST_USER_ID = "TEST_USER_ID";
    private Response response;

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        settingsDataSender = new SettingsDataSender(uCoreAdapterMock, gsonConverterMock, settingsConverterMock);
        settingsDataSender.eventing = eventingMock;
        settingsDataSender.uCoreAccessProvider = uCoreAccessProviderMock;
        settingsDataSender.synchronisationManager=synchronisationManagerMock;
    }

    @Test
    public void ShouldNotPostAnEventToBackend_WhenSettingsListIsEmpty() throws Exception {

        settingsDataSender.sendDataToBackend(Collections.<Settings>emptyList());

        verify(eventingMock, never()).post(settingsBackendSaveRequestEventCaptor.capture());
    }

    @Test
    public void ShouldNotPostAnEventToBackend_WhenStateIsNotIdle() throws Exception {
        settingsDataSender.synchronizationState.set(DataSender.State.BUSY.getCode());

        settingsDataSender.sendDataToBackend(Collections.singletonList(settingsMock));

        verify(eventingMock, never()).post(settingsBackendSaveRequestEventCaptor.capture());
    }

    @Test
    public void ShouldPostSettingsBackendSaveRequest_WhenSettingsIsNotNull() throws Exception {

        settingsDataSender.sendDataToBackend(Collections.singletonList(settingsMock));

        verify(eventingMock).post(settingsBackendSaveRequestEventCaptor.capture());
//        assertThat(settingsBackendSaveRequestEventCaptor.getValue().getSettings()).isNotNull();

    }

    @Test
    public void ShouldPostSettingsBackendSaveRequest_WhenSettingsIsNotNullWithAccessToken() throws Exception {
        when(settingsDataSender.uCoreAccessProvider.getAccessToken()).thenReturn("ACCESS_TOKEN");
        settingsDataSender.sendDataToBackend(Collections.singletonList(settingsMock));
    }


    @Test
    public void ShouldPostSettingsBackendSaveRequest_WhenSettingsIsLoggedIn() throws Exception {
        when(settingsDataSender.uCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(settingsDataSender.uCoreAccessProvider.getAccessToken()).thenReturn("fdsfdsf");
    }

    @Test
    public void ShouldSendDataToBackend_WhenUserIsValid() throws Exception {
        response = new Response("", 200, "OK", new ArrayList<Header>(), null);
        verifySendData();
    }

    @Test
    public void ShouldSendDataToBackend_WhenResponseCodeIs201() throws Exception {
        response = new Response("", 201, "OK", new ArrayList<Header>(), null);
        verifySendData();
    }

    @Test
    public void ShouldSendDataToBackend_WhenResponseCodeIs204() throws Exception {
        response = new Response("", 204, "OK", new ArrayList<Header>(), null);
        verifySendData();
    }

    private void verifySendData() {
        final SettingsClient uCoreClientMock = mock(SettingsClient.class);
        when(uCoreAdapterMock.getAppFrameworkClient(SettingsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(uCoreClientMock);
        when(settingsDataSender.uCoreAccessProvider.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(settingsDataSender.uCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(settingsDataSender.uCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(uCoreClientMock.updateSettings(eq(TEST_USER_ID), eq(TEST_USER_ID), any(UCoreSettings.class))).thenReturn(response);


        settingsDataSender.sendDataToBackend(Collections.singletonList(settingsMock));

        verify(eventingMock).post(isA(SyncBitUpdateRequest.class));
    }

    @Test
    public void ShouldPostError_WhenSendDataFails() throws Exception {
        final RetrofitError retrofitErrorMock = mock(RetrofitError.class);
        response = new Response("", 401, "Unauthorised", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitErrorMock.getResponse()).thenReturn(response);
        final SettingsClient uCoreClientMock = mock(SettingsClient.class);
        when(uCoreAdapterMock.getAppFrameworkClient(SettingsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(uCoreClientMock);
        when(settingsDataSender.uCoreAccessProvider.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(settingsDataSender.uCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(settingsDataSender.uCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(uCoreClientMock.updateSettings(eq(TEST_USER_ID), eq(TEST_USER_ID), any(UCoreSettings.class))).thenThrow(retrofitErrorMock);


        settingsDataSender.sendDataToBackend(Collections.singletonList(settingsMock));

        verify(eventingMock).post(isA(BackendResponse.class));
    }

    @Test
    public void ShouldGetClassForSyncData_WhenCalled() throws Exception {
        settingsDataSender.getClassForSyncData();
    }

    @Test
    public void shouldReturnFaleIfAccessProviderIsNull_WhenisUserInvalidIsCalled() throws Exception {

        settingsDataSender.uCoreAccessProvider=null;
        settingsDataSender.isUserInvalid();
    }
}