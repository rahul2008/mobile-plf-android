/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import com.philips.cdp.dicommclient.util.DICommLog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.PairingState.PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_BOOT_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_DEVICE_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_ENCRYPTION_KEY;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_HOME_SSID;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.KEY_IP_ADDRESS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkNodeTest {

    private static final String TEST_KEY = "TEST_KEY";

    @Mock
    private PropertyChangeListener mockPropertyChangeListener;

    @Captor
    private ArgumentCaptor<PropertyChangeEvent> propertyChangeEventCaptor;

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

        networkNode.addPropertyChangeListener(mockPropertyChangeListener);

        networkNode.setEncryptionKey(TEST_KEY);

        verify(mockPropertyChangeListener).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void test_ShouldInformListener_WhenKeyIsReset() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.addPropertyChangeListener(mockPropertyChangeListener);

        networkNode.setEncryptionKey(TEST_KEY);
        verify(mockPropertyChangeListener, times(1)).propertyChange(any(PropertyChangeEvent.class));

        networkNode.setEncryptionKey(null);
        verify(mockPropertyChangeListener, times(2)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void test_ShouldNotInformListener_WhenTheSameKeyIsSetTwice() throws Exception {
        NetworkNode networkNode = new NetworkNode();

        networkNode.addPropertyChangeListener(mockPropertyChangeListener);

        networkNode.setEncryptionKey(TEST_KEY);
        networkNode.setEncryptionKey(TEST_KEY);
        verify(mockPropertyChangeListener, times(1)).propertyChange(any(PropertyChangeEvent.class));
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

    @Test
    public void whenUpdatingNetworkNodeWithOtherCppId_ThenOriginalNetworkNodeShouldBeUnchanged() throws Exception {
        NetworkNode originalNetworkNode = createDummyNetworkNode();
        originalNetworkNode.setCppId("ABC");
        NetworkNode networkNodeForUpdate = createDummyNetworkNode();
        networkNodeForUpdate.setCppId("DEF");
        networkNodeForUpdate.setIpAddress("123.123.123.123");
        networkNodeForUpdate.setName("Dummy node");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verify(mockPropertyChangeListener, never()).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void whenUpdatingNetworkNodeWithOtherSsid_ThenSsidShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createDummyNetworkNode();
        NetworkNode networkNodeForUpdate = createDummyNetworkNode();
        networkNodeForUpdate.setHomeSsid("Some other ssid");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_HOME_SSID, "Some other ssid");
    }

    @Test
    public void whenUpdatingNetworkNodeWithOtherIpAddress_ThenIpAddressShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createDummyNetworkNode();
        NetworkNode networkNodeForUpdate = createDummyNetworkNode();
        networkNodeForUpdate.setIpAddress("10.10.10.10");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_IP_ADDRESS, "10.10.10.10");
    }

    @Test
    public void whenUpdatingNetworkNodeWithOtherName_ThenNameShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createDummyNetworkNode();
        NetworkNode networkNodeForUpdate = createDummyNetworkNode();
        networkNodeForUpdate.setName("My awesome appliance");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_DEVICE_NAME, "My awesome appliance");
    }

    @Test
    public void whenUpdatingNetworkNodeWithBootId_ThenBootIdShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createDummyNetworkNode();
        NetworkNode networkNodeForUpdate = createDummyNetworkNode();
        networkNodeForUpdate.setBootId(10L);
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_BOOT_ID, 10L);
    }

    @Test
    public void whenUpdatingNetworkNodeWithNullEncryptionKey_ThenEncryptionKeyShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createDummyNetworkNode();
        originalNetworkNode.setEncryptionKey("Some really secret key");
        NetworkNode networkNodeForUpdate = createDummyNetworkNode();
        networkNodeForUpdate.setEncryptionKey(null);
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_ENCRYPTION_KEY, null);
    }

    @Test
    public void whenUpdatingNetworkNodeWithEncryptionKey_ThenEncryptionKeyShouldNotBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createDummyNetworkNode();
        originalNetworkNode.setEncryptionKey("Some really secret key");
        NetworkNode networkNodeForUpdate = createDummyNetworkNode();
        networkNodeForUpdate.setEncryptionKey("Some other secret key");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verify(mockPropertyChangeListener, never()).propertyChange((PropertyChangeEvent) anyObject());
    }

    private void verifyPropertyChangeCalled(String propertyName, Object value) {
        verify(mockPropertyChangeListener, atLeastOnce()).propertyChange(propertyChangeEventCaptor.capture());
        boolean foundProperty = false;
        for (PropertyChangeEvent propertyChangeEvent : propertyChangeEventCaptor.getAllValues()) {
            if (propertyChangeEvent.getPropertyName().equals(propertyName)) {
                assertEquals(value, propertyChangeEvent.getNewValue());
                foundProperty = true;
            }
        }
        assertTrue("PropertyChange not called for '" + propertyName + "' property.", foundProperty);
    }

    private NetworkNode createDummyNetworkNode() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setCppId("cpp");

        return networkNode;
    }
}
