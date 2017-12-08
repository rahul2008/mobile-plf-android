package com.philips.platform.datasync.devicePairing;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.DevicePairingErrorResponseEvent;
import com.philips.platform.core.events.GetPairedDevicesResponseEvent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedString;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DevicePairingControllerTest {
    private String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    private String TEST_USER_ID = "TEST_USER_ID";

    private DevicePairingController mDevicePairingController;
    private Response mResponse;

    @Mock
    private Eventing mEventing;
    @Mock
    private RetrofitError mRetrofitError;
    @Mock
    private UCoreAccessProvider mUCoreAccessProvider;
    @Mock
    private UCoreAdapter mUCoreAdapter;
    @Mock
    private DevicePairingClient mDevicePairingClient;
    @Mock
    private GsonConverter mGsonConverter;
    @Mock
    private AppComponent mAppComponent;

    @Captor
    private ArgumentCaptor<BackendResponse> mBackendResponseArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(mAppComponent);
        mDevicePairingController = new DevicePairingController(mUCoreAdapter, mGsonConverter);
        mDevicePairingController.uCoreAccessProvider = mUCoreAccessProvider;
        mDevicePairingController.mEventing = mEventing;
    }

    @Test
    public void createSubjectProfileResponseOKTest() throws Exception {
        mResponse = new Response("", 200, "OK", new ArrayList<Header>(), null);
        pairDeviceTest();
    }

    @Test
    public void createSubjectProfileResponseNoContentTest() throws Exception {
        mResponse = new Response("", 201, "OK", new ArrayList<Header>(), null);
        pairDeviceTest();
    }

    @Test
    public void createSubjectProfileResponseCreatedTest() throws Exception {
        mResponse = new Response("", 204, "OK", new ArrayList<Header>(), null);
        pairDeviceTest();
    }

    private void pairDeviceTest() {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mDevicePairingClient);

        UCoreDevicePair uCoreDevicePair = new UCoreDevicePair();
        uCoreDevicePair.setSubjectIds(null);
        uCoreDevicePair.setStandardObservationNames(null);
        uCoreDevicePair.setDeviceType("DeviceType");
        uCoreDevicePair.setDeviceId("DeviceID");
        uCoreDevicePair.setRelationshipType("RelationshipType");

        assertTrue(uCoreDevicePair.getStandardObservationNames() == null);
        assertTrue(uCoreDevicePair.getSubjectIds() == null);
        assertTrue(uCoreDevicePair.getDeviceId() != null);
        assertTrue(uCoreDevicePair.getDeviceType() != null);
        assertTrue(uCoreDevicePair.getRelationshipType() != null);

        when(mDevicePairingClient.pairDevice(eq(TEST_USER_ID), eq(12), eq(TEST_USER_ID), any(UCoreDevicePair.class))).thenReturn(mResponse);
        mDevicePairingController.pairDevices(uCoreDevicePair);
        assertEquals(mResponse.getReason(), "OK");
    }

    @Test
    public void pairDeviceWithNullObjectTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mDevicePairingClient);
        assertFalse(mDevicePairingController.pairDevices(null));
    }

    @Test
    public void pairDeviceWithInvalidUserTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mDevicePairingController.isUserInvalid()).thenReturn(true);
        when(mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mDevicePairingClient);
        assertFalse(mDevicePairingController.pairDevices(null));
    }

    @Test
    public void unPairDeviceTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mDevicePairingClient);

        Response response = new Response("", 200, "OK", new ArrayList<Header>(), null);
        when(mDevicePairingClient.unPairDevice(eq(TEST_USER_ID), eq(12), eq(TEST_USER_ID), eq("1c2958989080098"))).thenReturn(response);
        mDevicePairingController.unPairDevice("Device ID");
        assertEquals(response.getReason(), "OK");
    }

    @Test
    public void getPairedDevicesTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mDevicePairingClient);
        mDevicePairingController.getPairedDevices();
        verify(mEventing).post(isA(GetPairedDevicesResponseEvent.class));
    }

    @Test
    public void userInvalidWhilePairDeviceTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(false);
        UCoreDevicePair uCoreDevicePair = new UCoreDevicePair();
        uCoreDevicePair.setSubjectIds(null);
        uCoreDevicePair.setStandardObservationNames(null);
        uCoreDevicePair.setDeviceType("DeviceType");
        uCoreDevicePair.setDeviceId("DeviceID");
        assertFalse(mDevicePairingController.pairDevices(uCoreDevicePair));
    }

    @Test
    public void userInvalidWhenUnPairDeviceTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(false);
        assertFalse(mDevicePairingController.unPairDevice(null));
    }

    @Test
    public void userInvalidWhenGetPairedDevicesTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(false);
        assertFalse(mDevicePairingController.getPairedDevices());
    }

    @Test
    public void nullAccessProviderTest() throws Exception {
        mDevicePairingController.uCoreAccessProvider = null;
        assertThat(mDevicePairingController.isUserInvalid()).isFalse();
    }

    @Test
    public void retrofitErrorWhilePairDeviceTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mDevicePairingClient);

        UCoreDevicePair uCoreDevicePair = new UCoreDevicePair();
        uCoreDevicePair.setSubjectIds(null);
        uCoreDevicePair.setStandardObservationNames(null);
        uCoreDevicePair.setDeviceType("DeviceType");
        uCoreDevicePair.setDeviceId("DeviceID");

        final RetrofitError retrofitError = mock(RetrofitError.class);
        mResponse = new Response("", 403, "Test error", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitError.getResponse()).thenReturn(mResponse);
        when(mDevicePairingClient.pairDevice(TEST_USER_ID, 12, TEST_USER_ID, uCoreDevicePair)).thenThrow(retrofitError);
        mDevicePairingController.pairDevices(uCoreDevicePair);
        verify(mEventing).post(isA(DevicePairingErrorResponseEvent.class));
    }

    @Test
    public void retrofitErrorWhileUnPairDeviceTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mDevicePairingClient);

        final RetrofitError retrofitError = mock(RetrofitError.class);
        mResponse = new Response("", 403, "Test error", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitError.getResponse()).thenReturn(mResponse);

        when(mDevicePairingClient.unPairDevice(TEST_USER_ID, 12, TEST_USER_ID, "deviceID")).thenThrow(retrofitError);
        mDevicePairingController.unPairDevice("deviceID");
        verify(mEventing).post(isA(DevicePairingErrorResponseEvent.class));
    }

    @Test
    public void retrofitErrorWhileGetPairedDevicesTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(DevicePairingClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mDevicePairingClient);

        final RetrofitError retrofitError = mock(RetrofitError.class);
        mResponse = new Response("", 403, "Test error", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitError.getResponse()).thenReturn(mResponse);

        when(mDevicePairingClient.getPairedDevices(TEST_USER_ID, 12, TEST_USER_ID)).thenThrow(retrofitError);
        mDevicePairingController.getPairedDevices();
        verify(mEventing).post(isA(DevicePairingErrorResponseEvent.class));
    }
}