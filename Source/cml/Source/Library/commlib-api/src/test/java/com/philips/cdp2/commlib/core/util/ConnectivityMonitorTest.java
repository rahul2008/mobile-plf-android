package com.philips.cdp2.commlib.core.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.philips.cdp.dicommclient.util.DICommLog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ConnectivityMonitorTest {

    @Mock
    private Context mockContext;

    @Mock
    private ConnectivityManager connectivityManagerMock;

    @Mock
    private NetworkInfo networkInfoMock;

    @Mock
    private Availability.AvailabilityListener<ConnectivityMonitor> availabilityListenerMock;

    @Captor
    private ArgumentCaptor<BroadcastReceiver> broadcastReceiverArgumentCaptor;

    private ConnectivityMonitor connectivityMonitor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();

        when(connectivityManagerMock.getActiveNetworkInfo()).thenReturn(networkInfoMock);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManagerMock);

        when(networkInfoMock.getType()).thenReturn(ConnectivityManager.TYPE_WIFI);
        when(networkInfoMock.isConnected()).thenReturn(true);

        connectivityMonitor = ConnectivityMonitor.forNetworkTypes(mockContext, ConnectivityManager.TYPE_WIFI);
        connectivityMonitor.addAvailabilityListener(availabilityListenerMock);
        reset(availabilityListenerMock);
    }

    @Test
    public void givenSelectedTypeIsAvailable_whenConnectivityMonitorIsInitialised_thenItIsAvailable() {
        assertThat(connectivityMonitor.isAvailable()).isTrue();
    }

    @Test
    public void givenOneOfSelectedTypesIsAvailable_whenConnectivityMonitorIsInitialised_thenItIsAvailable() {
        when(networkInfoMock.getType()).thenReturn(ConnectivityManager.TYPE_MOBILE);
        when(networkInfoMock.isConnected()).thenReturn(true);

        ConnectivityMonitor connectivityMonitor = ConnectivityMonitor.forNetworkTypes(mockContext, ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE);

        assertThat(connectivityMonitor.isAvailable()).isTrue();
    }

    @Test
    public void givenSelectedTypeIsNotAvailable_whenConnectivityMonitorIsInitialised_thenItIsNotAvailable() {
        when(networkInfoMock.getType()).thenReturn(ConnectivityManager.TYPE_MOBILE);

        ConnectivityMonitor connectivityMonitor = ConnectivityMonitor.forNetworkTypes(mockContext, ConnectivityManager.TYPE_WIFI);

        assertThat(connectivityMonitor.isAvailable()).isFalse();
    }

    @Test
    public void whenAvailabilityChanges_thenListenerIsNotified() {
        verify(mockContext).registerReceiver(broadcastReceiverArgumentCaptor.capture(), any(IntentFilter.class));
        when(networkInfoMock.isConnected()).thenReturn(false);
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, null);

        verify(availabilityListenerMock).onAvailabilityChanged(connectivityMonitor);
    }

    @Test
    public void whenAvailabilityChangesButStateIsTheSame_thenListenerIsNotNotified() {
        verify(mockContext).registerReceiver(broadcastReceiverArgumentCaptor.capture(), any(IntentFilter.class));
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, null);

        verifyZeroInteractions(availabilityListenerMock);
    }

    @Test
    public void givenListenerIsRemoved_whenAvailabilityChanges_thenListenerIsNotNotified() {
        connectivityMonitor.removeAvailabilityListener(availabilityListenerMock);

        verify(mockContext).registerReceiver(broadcastReceiverArgumentCaptor.capture(), any(IntentFilter.class));
        when(networkInfoMock.isConnected()).thenReturn(false);
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, null);

        verifyZeroInteractions(availabilityListenerMock);
    }

    @Test
    public void givenConnectivityServiceIsNotAvailable_whenConnectivityMonitorIsInitialised_thenItIsNotAvailable() {
        when(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(null);

        ConnectivityMonitor connectivityMonitor = ConnectivityMonitor.forNetworkTypes(mockContext, ConnectivityManager.TYPE_WIFI);

        assertThat(connectivityMonitor.isAvailable()).isFalse();
    }

    @Test
    public void givenConnectivityServiceIsNotAvailable_whenNetworkIsRequested_thenNetworkIsNull() {
        when(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(null);

        ConnectivityMonitor connectivityMonitor = ConnectivityMonitor.forNetworkTypes(mockContext, ConnectivityManager.TYPE_WIFI);

        assertThat(connectivityMonitor.getNetwork()).isNull();
    }
}