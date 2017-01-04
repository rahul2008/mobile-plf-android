package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.SendUserCharacteristicsToBackendResponseEvent;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

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
    private Eventing eventingMock;
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String TEST_USER_ID = "TEST_USER_ID";
    private Response response;

    @Before
    public void setUp() {
        initMocks(this);

        userCharacteristicsSender = new UserCharacteristicsSender(uCoreAdapterMock, gsonConverterMock, eventingMock);
    }

    @Test
    public void ShouldNotSendDataToBackend_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        userCharacteristicsSender.sendDataToBackend(Collections.<Characteristics>emptyList());

        verifyZeroInteractions(uCoreAdapterMock);
    }

    @Test
    public void ShouldNotSendDataToBackend_WhenUserIdIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getUserId()).thenReturn("");

        userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));

        verifyZeroInteractions(uCoreAdapterMock);
    }

    @Test
    public void ShouldNotSendDataToBackend_WhenUserIdIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getUserId()).thenReturn(null);

        userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));

        verifyZeroInteractions(uCoreAdapterMock);
    }

    @Test
    public void ShouldNotSendDataToBackend_WhenDataToSendIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getUserId()).thenReturn(null);

        userCharacteristicsSender.sendDataToBackend(null);

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
        when(uCoreClientMock.createOrUpdateUserCharacteristics(eq(TEST_USER_ID), eq(TEST_USER_ID), any(UCoreUserCharacteristics.class), eq(9))).thenReturn(response);

        userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));

        verify(eventingMock).post(isA(SendUserCharacteristicsToBackendResponseEvent.class));
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
        //when(uCoreClientMock.createOrUpdateUserCharacteristics(eq(TEST_USER_ID), eq(TEST_USER_ID), (List<UCoreUserCharacteristics>) any(Characteristics.class), eq(9))).thenThrow(retrofitErrorMock);

        userCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsMock));

        verify(eventingMock).post(isA(BackendResponse.class));
    }

//    @Test
//    public void ShouldReturnClassForSyncData_WhenAsked() throws Exception {
//        assertThat(userCharacteristicsSender.getClassForSyncData()).isEqualTo(Characteristics.class);
//    }
}