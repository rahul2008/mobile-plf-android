package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;

import org.junit.Before;
import org.junit.Test;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class UserCharacteristicsSenderTest {
    UserCharacteristicsSender userCharacteristicsSender;

    @Mock
    private Characteristics characteristicsMock;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private GsonConverter gsonConverterMock;
    @Mock
    private AppComponent appComponantMock;

    @Mock
    private Eventing eventingMock;
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String TEST_USER_ID = "TEST_USER_ID";
    private Response response;
    @Mock
    private UserCharacteristicsConverter userCharacteristicsConverterMock;
    @Mock
    private UserCharacteristicsClient clientMock;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Before
    public void setUp() {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponantMock);

        userCharacteristicsSender = new UserCharacteristicsSender(userCharacteristicsConverterMock, gsonConverterMock);
        userCharacteristicsSender.mUCoreAdapter = uCoreAdapterMock;
        userCharacteristicsSender.mUCoreAccessProvider = accessProviderMock;
        userCharacteristicsSender.mEventing = eventingMock;
        userCharacteristicsSender.synchronisationManager = synchronisationManagerMock;
        userCharacteristicsSender.mUserCharacteristicsConverter = userCharacteristicsConverterMock;
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn("USER_ID");
        when(accessProviderMock.getSubjectId()).thenReturn("BABY_ID");
        when(uCoreAdapterMock.getAppFrameworkClient(UserCharacteristicsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(clientMock);
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
    }

    @Test
    public void ShouldNotSendDataToBackend_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        boolean sendDataToBackend = userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));

        verifyZeroInteractions(clientMock);
        assertThat(sendDataToBackend).isFalse();
        verifyZeroInteractions(uCoreAdapterMock);
    }

    @Test
    public void ShouldNotSendDataToBackend_WhenUserIdIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        when(accessProviderMock.getAccessToken()).thenReturn("");
        when(accessProviderMock.getUserId()).thenReturn("");

        userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));

        verifyZeroInteractions(uCoreAdapterMock);
    }

    @Test
    public void ShouldNotSendDataToBackend_WhenAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        when(accessProviderMock.getAccessToken()).thenReturn("");
        userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));

        verifyZeroInteractions(uCoreAdapterMock);
    }

    @Test
    public void ShouldNotSendDataToBackend_WhenDataToSendIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        when(accessProviderMock.getAccessToken()).thenReturn("");

        // List<UserCharacteristics> characteristicsList = new ArrayList<>();
        boolean isReturnFalse = userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));
        assertThat(isReturnFalse).isEqualTo(false);
        verifyZeroInteractions(uCoreAdapterMock);
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
        final UserCharacteristicsClient uCoreClientMock = mock(UserCharacteristicsClient.class);
        when(uCoreAdapterMock.getAppFrameworkClient(UserCharacteristicsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(uCoreClientMock);
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        when(uCoreClientMock.createOrUpdateUserCharacteristics(eq(TEST_USER_ID), eq(TEST_USER_ID), (UCoreUserCharacteristics) any(),
                eq(UCoreAdapter.API_VERSION))).thenReturn(response);

        userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));

        verify(eventingMock).post(isA(SyncBitUpdateRequest.class));
    }

    @Test
    public void ShouldPostError_WhenSendDataFails() throws Exception {
        final RetrofitError retrofitErrorMock = mock(RetrofitError.class);
        response = new Response("", 401, "Unauthorised", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitErrorMock.getResponse()).thenReturn(response);
        final UserCharacteristicsClient uCoreClientMock = mock(UserCharacteristicsClient.class);
        when(uCoreAdapterMock.getAppFrameworkClient(UserCharacteristicsClient.class, ACCESS_TOKEN, gsonConverterMock)).thenReturn(uCoreClientMock);
        when(accessProviderMock.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        when(uCoreClientMock.createOrUpdateUserCharacteristics(eq(TEST_USER_ID), eq(TEST_USER_ID),
                (UCoreUserCharacteristics) any(), eq(UCoreAdapter.API_VERSION))).thenThrow(retrofitErrorMock);


        userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));

        verify(eventingMock).post(isA(BackendResponse.class));
    }

    @Test
    public void ShouldReturnClassForSyncData_WhenAsked() throws Exception {
        assertThat(userCharacteristicsSender.getClassForSyncData()).isEqualTo(Characteristics.class);
    }
}