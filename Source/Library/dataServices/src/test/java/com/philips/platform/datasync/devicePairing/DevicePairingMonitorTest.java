package com.philips.platform.datasync.devicePairing;

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

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(mAppComponent);
        mDevicePairingMonitor = new DevicePairingMonitor(mDevicePairingController);
    }

    @Test
    public void pairDevicesRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mDevicePairingMonitor.onEventAsync(mPairDevicesRequestEvent);
    }

    @Test
    public void unPairDeviceRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mDevicePairingMonitor.onEventAsync(mUnPairDeviceRequestEvent);
    }

    @Test
    public void getPairedDeviceRequestEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mDevicePairingMonitor.onEventAsync(mGetPairedDeviceRequestEvent);
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
    }

}