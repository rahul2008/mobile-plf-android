/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import com.philips.cdp.dicommclient.networknode.NetworkNode.EncryptionKeyUpdatedListener;
import com.philips.cdp.dicommclient.util.DICommLog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.PairingState.PAIRED;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkNodeTest {

    private static final String TEST_KEY = "TEST_KEY";

    @Mock
    EncryptionKeyUpdatedListener listener;

    @Mock
    private PropertyChangeListener mockPropertyChangeListener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();
    }

    @Test
    public void test_ShouldReturnKey_WhenKeyIsSet_AndGetIsCalled() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKey(TEST_KEY);

        String encryptionKey = networkNode.getEncryptionKey();

        assertEquals(TEST_KEY, encryptionKey);
    }

    @Test
    public void test_ShouldInformListener_WhenKeyIsSet() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKeyUpdatedListener(listener);

        networkNode.setEncryptionKey(TEST_KEY);

        verify(listener).onKeyUpdate();
    }

    @Test
    public void test_ShouldInformListener_WhenKeyIsReset() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKeyUpdatedListener(listener);

        networkNode.setEncryptionKey(TEST_KEY);
        verify(listener, times(1)).onKeyUpdate();

        networkNode.setEncryptionKey(null);
        verify(listener, times(2)).onKeyUpdate();
    }

    @Test
    public void test_ShouldNotInformListener_WhenTheSameKeyIsSetTwice() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKeyUpdatedListener(listener);

        networkNode.setEncryptionKey(TEST_KEY);
        networkNode.setEncryptionKey(TEST_KEY);
        verify(listener, times(1)).onKeyUpdate();
    }

    @Test
    public void whenPropertyChanges_thenPropertyChangeEventIsFired() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.addPropertyChangeListener(mockPropertyChangeListener);

        networkNode.setBootId(42L);
        networkNode.setCppId("super unique");
        networkNode.setDeviceType("don't care");
        networkNode.setEncryptionKey("H4X0R");
        networkNode.setHomeSsid("virus.exe");
        networkNode.setIpAddress("127.0.0.1");
        networkNode.setLastPairedTime(1337L);
        networkNode.setModelId("BFG9K");
        networkNode.setName("Anton");
        networkNode.setPairedState(PAIRED);
        networkNode.setPin("ALL YOUR BASE ARE BELONG TO US");

        verify(mockPropertyChangeListener, times(11)).propertyChange(any(PropertyChangeEvent.class));
    }
}
