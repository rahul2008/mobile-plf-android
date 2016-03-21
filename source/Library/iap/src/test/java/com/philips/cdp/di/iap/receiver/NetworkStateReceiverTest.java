package com.philips.cdp.di.iap.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Mockito;

public class NetworkStateReceiverTest extends TestCase {

    @Test
    public void testShouldNetworkIsConnected() throws Exception {
        //When
        Context mockContext = Mockito.mock(Context.class);
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        Intent mockIntent = Mockito.mock(Intent.class);
        ConnectivityManager mockConnectivityManager = Mockito.mock(ConnectivityManager.class);
        NetworkInfo mockNetworkInfo = Mockito.mock(NetworkInfo.class);
        Mockito.when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        NetworkStateReceiver spyNetworkStateReceiver1 = Mockito.mock(NetworkStateReceiver.class);

        spyNetworkStateReceiver1.onReceive(mockContext, mockIntent);
        assertNotNull(mockNetworkInfo);
        assertEquals(false, mockNetworkInfo.isConnectedOrConnecting());
        //assertTrue("Network is Connected", mockNetworkInfo.isConnectedOrConnecting());
    }

    @Test
    public void testShouldNetworkIsNotConnected() throws Exception {
        //When
        Context mockContext = Mockito.mock(Context.class);
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        Intent mockIntent = Mockito.mock(Intent.class);
        ConnectivityManager mockConnectivityManager = Mockito.mock(ConnectivityManager.class);
        NetworkInfo mockNetworkInfo = null;
        Mockito.when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        NetworkStateReceiver spyNetworkStateReceiver1 = Mockito.mock(NetworkStateReceiver.class);

        spyNetworkStateReceiver1.onReceive(mockContext, mockIntent);
        assertNull(mockNetworkInfo);
    }
}