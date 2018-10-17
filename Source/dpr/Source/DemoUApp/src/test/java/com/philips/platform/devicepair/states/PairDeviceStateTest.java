package com.philips.platform.devicepair.states;

import android.app.Activity;

import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.devicepair.pojo.PairDevice;
import com.philips.platform.devicepair.ui.IDevicePairingListener;
import com.philips.platform.devicepair.utils.Utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataServicesManager.class, Utility.class})
public class PairDeviceStateTest {

    @Test
    public void start() {
        givenOnline();
        givenThereIsADevice();
        givenDataServiceManager();
        whenStartForPairDeviceState();
        thenPairDeviceIsCalled();
    }

    @Test
    public void start_NotOnline() {
        whenStartForPairDeviceState();
        thenOnInternetErrorIsCalled();
    }

    private void givenOnline() {
        PowerMockito.mockStatic(Utility.class);
        when(Utility.isOnline(any(Activity.class))).thenReturn(true);
    }

    private void givenThereIsADevice() {
        when(pairDevice.getDeviceID()).thenReturn(DUMMY_DEVICE_ID);
        when(pairDevice.getDeviceType()).thenReturn(DUMMY_DEVICE_TYPE);
        when(pairDevice.getRelationshipType()).thenReturn(DUMMY_RELATIONSHIP_TYPE);
    }

    private void givenDataServiceManager() {
        PowerMockito.mockStatic(DataServicesManager.class);
        when(DataServicesManager.getInstance()).thenReturn(dataServicesManager);
    }

    private void whenStartForPairDeviceState() {
        pairDeviceState.start(stateContext);
    }

    private void thenPairDeviceIsCalled() {
        verify(dataServicesManager, times(1)).pairDevices(anyString(),
                anyString(), ArgumentMatchers.<String>anyList(), ArgumentMatchers.<String>anyList(),
                eq(DUMMY_RELATIONSHIP_TYPE), any(DevicePairingListener.class));
    }

    private void thenOnInternetErrorIsCalled() {
        verify(iDevicePairingListener, times(1)).onInternetError();
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        pairDeviceState = new PairDeviceState(pairDevice, iDevicePairingListener, activity);
    }

    @Mock
    private Activity activity;
    @Mock
    private PairDevice pairDevice;
    @Mock
    private IDevicePairingListener iDevicePairingListener;
    @Mock
    private StateContext stateContext;
    @Mock
    private DataServicesManager dataServicesManager;
    private PairDeviceState pairDeviceState;
    private static final String DUMMY_DEVICE_ID = "1";
    private static final String DUMMY_DEVICE_TYPE = "DEVICE_TYPE";
    private static final String DUMMY_RELATIONSHIP_TYPE = "URN:CDP|SDADADA";
}