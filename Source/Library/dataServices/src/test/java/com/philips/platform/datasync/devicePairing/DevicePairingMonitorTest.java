package com.philips.platform.datasync.devicePairing;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.DevicePairingErrorResponseEvent;
import com.philips.platform.core.events.DevicePairingResponseEvent;
import com.philips.platform.core.events.GetPairedDeviceRequestEvent;
import com.philips.platform.core.events.GetPairedDevicesResponseEvent;
import com.philips.platform.core.events.PairDevicesRequestEvent;
import com.philips.platform.core.events.UnPairDeviceRequestEvent;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import retrofit.converter.GsonConverter;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DevicePairingMonitorTest {

    private DevicePairingMonitor mDevicePairingMonitor;
    @Mock
    private DevicePairingController mDevicePairingController;
    @Mock
    private GsonConverter mGsonConverter;
    @Mock
    private UCoreAdapter mUCoreAdapter;
    @Mock
    private PairDevicesRequestEvent mPairDevicesRequestEvent;
    @Mock
    private UnPairDeviceRequestEvent mUnPairDeviceRequestEvent;
    @Mock
    private GetPairedDeviceRequestEvent mGetPairedDeviceRequestEvent;
    @Mock
    private DevicePairingResponseEvent mDevicePairingResponseEvent;
    @Mock
    private DevicePairingErrorResponseEvent mDevicePairingErrorResponseEvent;
    @Mock
    private GetPairedDevicesResponseEvent mGetPairedDevicesResponseEvent;
    @Mock
    private AppComponent mAppComponent;
    @Mock
    private UCoreAccessProvider mUCoreAccessProvider;
    @Mock
    private Eventing mEventing;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(mAppComponent);
        mDevicePairingMonitor = new DevicePairingMonitor(mDevicePairingController);
    }

    @Test
    public void pairDevicesRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mDevicePairingMonitor.onEventAsync(mPairDevicesRequestEvent);
        verify(mEventing, never()).post(isA(PairDevicesRequestEvent.class));
    }

    @Test
    public void unPairDeviceRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mDevicePairingMonitor.onEventAsync(mUnPairDeviceRequestEvent);
        verify(mEventing, never()).post(isA(UnPairDeviceRequestEvent.class));
    }

    @Test
    public void getPairedDeviceRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mDevicePairingMonitor.onEventAsync(mGetPairedDeviceRequestEvent);
        verify(mEventing, never()).post(isA(GetPairedDeviceRequestEvent.class));
    }

    @Test
    public void devicePairingResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        PairDevicesRequestEvent pairDevicesRequestEvent = new PairDevicesRequestEvent("", "", null, null, "", new DevicePairingListener() {
            @Override
            public void onResponse(boolean status) {

            }

            @Override
            public void onError(DataServicesError dataServicesError) {

            }

            @Override
            public void onGetPairedDevicesResponse(List<String> pairedDevices) {

            }
        });
        mDevicePairingMonitor.onEventAsync(pairDevicesRequestEvent);
        mDevicePairingMonitor.onEventAsync(mDevicePairingResponseEvent);
        verify(mEventing, never()).post(isA(DevicePairingResponseEvent.class));
    }

    @Test
    public void devicePairingErrorResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        PairDevicesRequestEvent pairDevicesRequestEvent = new PairDevicesRequestEvent("", "", null, null, "", new DevicePairingListener() {
            @Override
            public void onResponse(boolean status) {

            }

            @Override
            public void onError(DataServicesError dataServicesError) {

            }

            @Override
            public void onGetPairedDevicesResponse(List<String> pairedDevices) {

            }
        });
        mDevicePairingMonitor.onEventAsync(pairDevicesRequestEvent);
        mDevicePairingMonitor.onEventAsync(mDevicePairingErrorResponseEvent);
        verify(mEventing, never()).post(isA(DevicePairingErrorResponseEvent.class));
    }

    @Test
    public void getPairedDevicesResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        GetPairedDeviceRequestEvent getPairedDeviceRequestEvent = new GetPairedDeviceRequestEvent(new DevicePairingListener() {
            @Override
            public void onResponse(boolean status) {

            }

            @Override
            public void onError(DataServicesError dataServicesError) {

            }

            @Override
            public void onGetPairedDevicesResponse(List<String> pairedDevices) {

            }
        });
        mDevicePairingMonitor.onEventAsync(getPairedDeviceRequestEvent);
        mDevicePairingMonitor.onEventAsync(mGetPairedDevicesResponseEvent);
        verify(mEventing, never()).post(isA(GetPairedDevicesResponseEvent.class));
    }

}