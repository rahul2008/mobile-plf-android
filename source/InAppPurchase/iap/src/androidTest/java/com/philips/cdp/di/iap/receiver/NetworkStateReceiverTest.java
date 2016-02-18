package com.philips.cdp.di.iap.receiver;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.InstrumentationTestCase;

import org.junit.Test;
import org.mockito.Mockito;

public class NetworkStateReceiverTest extends InstrumentationTestCase {

    public void testShouldNetworkIsConnected() throws Exception {
        //When
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        Intent mockIntent = Mockito.mock(Intent.class);
        ConnectivityManager mockConnectivityManager = Mockito.mock(ConnectivityManager.class);
        NetworkInfo mockNetworkInfo = Mockito.mock(NetworkInfo.class);
        Mockito.when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        NetworkStateReceiver spyNetworkStateReceiver1 = Mockito.spy(networkStateReceiver);

        spyNetworkStateReceiver1.onReceive(getInstrumentation().getTargetContext(), mockIntent);
        assertNotNull(mockNetworkInfo);
        assertTrue("Network is Connected", mockNetworkInfo.isConnectedOrConnecting());
    }

    @Test
    public void testShouldNetworkIsNotConnected() throws Exception {
        //When
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        Intent mockIntent = Mockito.mock(Intent.class);
        ConnectivityManager mockConnectivityManager = Mockito.mock(ConnectivityManager.class);
        NetworkInfo mockNetworkInfo = null;
        Mockito.when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        NetworkStateReceiver spyNetworkStateReceiver1 = Mockito.spy(networkStateReceiver);

        spyNetworkStateReceiver1.onReceive(getInstrumentation().getTargetContext(), mockIntent);
        assertNull(mockNetworkInfo);
        assertTrue("Network is Not Connected", mockNetworkInfo.isConnectedOrConnecting());
    }
}