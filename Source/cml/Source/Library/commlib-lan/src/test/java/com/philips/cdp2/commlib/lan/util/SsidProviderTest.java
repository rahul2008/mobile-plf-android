package com.philips.cdp2.commlib.lan.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.philips.cdp.dicommclient.util.DICommLog;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SsidProviderTest {

    public static final String SSID = "ssid";
    public static final String DIFFERENT_SSID = "ssid2";
    @Mock
    private Context mockContext;

    @Mock
    private WifiManager wifiManagerMock;

    @Mock
    private WifiInfo wifiInfoInfo;

    @Mock
    private SsidProvider.NetworkChangeListener networkChangeListenerMock;

    @Captor
    private ArgumentCaptor<BroadcastReceiver> broadcastReceiverArgumentCaptor;

    private SsidProvider ssidProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();

        when(wifiManagerMock.getConnectionInfo()).thenReturn(wifiInfoInfo);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSystemService(Context.WIFI_SERVICE)).thenReturn(wifiManagerMock);

        when(wifiInfoInfo.getSupplicantState()).thenReturn(SupplicantState.COMPLETED);
        when(wifiInfoInfo.getSSID()).thenReturn(SSID);

        ssidProvider = new SsidProvider(mockContext);
        verify(mockContext).registerReceiver(broadcastReceiverArgumentCaptor.capture(), any(IntentFilter.class));
        ssidProvider.addNetworkChangeListener(networkChangeListenerMock);
        reset(networkChangeListenerMock);
    }

    @Test
    public void whenSsidIsAvailable_thenItIsAvailable() {
        assertThat(SSID).isEqualTo(ssidProvider.getCurrentSsid());
    }

    @Test
    public void whenWifiInfoIsNotAvailable_thenHomeSsidIsNotAvailable() {
        when(wifiManagerMock.getConnectionInfo()).thenReturn(null);

        assertThat(ssidProvider.getCurrentSsid()).isNull();
    }

    @Test
    public void whenSupplicantStateIsNotCompleted_thenHomeSsidIsNotAvailable() {
        when(wifiInfoInfo.getSupplicantState()).thenReturn(SupplicantState.INACTIVE);

        assertThat(ssidProvider.getCurrentSsid()).isNull();
    }

    @Test
    public void whenHomeSsidIsNull_thenHomeSsidIsNotAvailable() {
        when(wifiInfoInfo.getSSID()).thenReturn(null);

        assertThat(ssidProvider.getCurrentSsid()).isNull();
    }

    @Test
    public void whenSsidHasChanged_thenListenerIsNotified() {
        when(wifiInfoInfo.getSSID()).thenReturn(DIFFERENT_SSID);
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, null);

        verify(networkChangeListenerMock).onNetworkChanged();
    }

    @Test
    public void whenSsidHasChangedTwiceToSameValue_thenListenerIsNotifiedOnce() {
        when(wifiInfoInfo.getSSID()).thenReturn(DIFFERENT_SSID);
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, null);
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, null);

        verify(networkChangeListenerMock).onNetworkChanged();
    }

    @Test
    public void whenSsidHasNotChanged_thenListenerIsNotNotified() {
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, null);

        verifyZeroInteractions(networkChangeListenerMock);
    }

    @Test
    public void whenSsidHasChangedToUnavailable_thenListenerIsNotified() {
        when(wifiInfoInfo.getSSID()).thenReturn(null);
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, null);

        verify(networkChangeListenerMock).onNetworkChanged();
    }

    @Test
    public void givenListenerIsUnregistered_whenSsidHasChanged_thenListenerIsNotNotified() {
        ssidProvider.removeNetworkChangeListener(networkChangeListenerMock);

        when(wifiInfoInfo.getSSID()).thenReturn(DIFFERENT_SSID);
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, null);

        verifyZeroInteractions(networkChangeListenerMock);
    }
}