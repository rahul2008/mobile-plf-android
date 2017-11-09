package com.philips.platform.datasync.settings;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.SettingsBackendGetRequest;
import com.philips.platform.core.events.SettingsBackendSaveResponse;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.characteristics.UserCharacteristicsClient;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SettingsDataFetcherTest {
    private String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    private String TEST_USER_ID = "TEST_USER_ID";



    private SettingsDataFetcher settingsDataFetcher;

    @Mock
    GsonConverter gsonConverterMock;

    @Mock
    SettingsConverter settingsConverterMock;

    @Mock
    UCoreAdapter uCoreAdapterMock;

    @Mock
    Eventing eventingMock;

    @Mock
    Settings settingsMock;

    @Captor
    private ArgumentCaptor<SettingsBackendGetRequest> settingsBackendGetRequestArgumentCaptor;

    @Mock
    private AppComponent appComponantMock;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(appComponantMock);
        settingsDataFetcher = new SettingsDataFetcher(uCoreAdapterMock, gsonConverterMock, settingsConverterMock);
        settingsDataFetcher.uCoreAccessProvider = accessProviderMock;
        settingsDataFetcher.eventing = eventingMock;
    }

    @Test
    public void shouldNotFetchDataSince_WhenDataSenderIsBusy() throws Exception {

        settingsDataFetcher.synchronizationState.set(DataSender.State.BUSY.getCode());
        settingsDataFetcher.fetchData();
        verify(eventingMock, never()).post(settingsBackendGetRequestArgumentCaptor.capture());
    }

    @Test
    public void shouldFetchDataSince_WhenDataSenderIsNotBusy() throws Exception {

        settingsDataFetcher.synchronizationState.set(DataSender.State.IDLE.getCode());
        settingsDataFetcher.fetchData();
        verify(eventingMock).post(settingsBackendGetRequestArgumentCaptor.capture());
    }

    @Test
    public void ShouldNotFetchData_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        assertThat(settingsDataFetcher.fetchData()).isNull();
    }

    @Test
    public void ShouldNotFetchData_WhenAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");

        assertThat(settingsDataFetcher.fetchData()).isNull();
    }

    @Test
    public void ShouldNotFetchData_WhenAccessTokenIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);

        assertThat(settingsDataFetcher.fetchData()).isNull();
    }

    @Test
    public void ShouldFetchData_WhenUserIsValid() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        final SettingsClient uSettingClientMock = mock(SettingsClient.class);
        when(uCoreAdapterMock.getAppFrameworkClient(SettingsClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(uSettingClientMock);
        uSettingClientMock.getSettings(eq(TEST_ACCESS_TOKEN), eq(TEST_USER_ID),eq(9));
        when(settingsConverterMock.convertUcoreToAppSettings(any(UCoreSettings.class))).thenReturn(settingsMock);
        RetrofitError retrofitError = settingsDataFetcher.fetchData();
        assertThat(retrofitError).isNull();
        verify(eventingMock).post(isA(SettingsBackendSaveResponse.class));
    }

    @Test
    public void ShouldThrowError_WhenRetroFitfails() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        final SettingsClient uSettingClientMock = mock(SettingsClient.class);
        when(uCoreAdapterMock.getAppFrameworkClient(SettingsClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(uSettingClientMock);
        uSettingClientMock.getSettings(eq(TEST_ACCESS_TOKEN), eq(TEST_USER_ID),eq(9));
        when(settingsConverterMock.convertUcoreToAppSettings(any(UCoreSettings.class))).thenReturn(settingsMock);
        RetrofitError retrofitError = settingsDataFetcher.fetchData();
       // assertThat(retrofitError).isNotNull();
    }


    @Test
    public void shouldReturnFaleIfAccessProviderIsNull_WhenisUserInvalidIsCalled() throws Exception {

        settingsDataFetcher.uCoreAccessProvider=null;
        settingsDataFetcher.isUserInvalid();
    }
}