/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.DICommLog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.PairingState.PAIRED;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_BOOT_ID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_DEVICE_NAME;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_DEVICE_TYPE;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_ENCRYPTION_KEY;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_HOME_SSID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_IP_ADDRESS;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_MODEL_ID;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkNodeTest extends RobolectricTest {

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
        createNetworkNode(mockPropertyChangeListener);

        verify(mockPropertyChangeListener, times(11)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void whenUpdatingNetworkNodeWithOtherCppId_ThenOriginalNetworkNodeShouldBeUnchanged() throws Exception {
        NetworkNode originalNetworkNode = createNetworkNode();
        originalNetworkNode.setCppId("ABC");
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setCppId("DEF");
        networkNodeForUpdate.setIpAddress("123.123.123.123");
        networkNodeForUpdate.setName("Dummy node");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verify(mockPropertyChangeListener, never()).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void whenUpdatingNetworkNodeWithOtherSsid_ThenSsidShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setHomeSsid("Some other ssid");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_HOME_SSID, "Some other ssid");
    }

    @Test
    public void whenUpdatingNetworkNodeWithOtherIpAddress_ThenIpAddressShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setIpAddress("10.10.10.10");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_IP_ADDRESS, "10.10.10.10");
    }

    @Test
    public void whenUpdatingNetworkNodeWithOtherName_ThenNameShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setName("My awesome appliance");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_DEVICE_NAME, "My awesome appliance");
    }

    @Test
    public void whenUpdatingNetworkNodeWithOtherModelId_ThenModelIdShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setModelId("My awesome modelId");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_MODEL_ID, "My awesome modelId");
    }

    @Test
    public void whenUpdatingNetworkNodeWithOtherDeviceType_ThenDeviceTypeShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setDeviceType("My awesome deviceType");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_DEVICE_TYPE, "My awesome deviceType");
    }

    @Test
    public void whenUpdatingNetworkNodeWithBootId_ThenBootIdShouldBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setBootId(10L);
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_BOOT_ID, 10L);
    }

    @Test
    public void whenUpdatingNetworkNodeWithNullEncryptionKey_ThenEncryptionKeyShouldNotBeChanged() throws Exception {
        NetworkNode originalNetworkNode = createNetworkNode();
        originalNetworkNode.setEncryptionKey("Some really secret key");
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setEncryptionKey(null);
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verify(mockPropertyChangeListener, never()).propertyChange((PropertyChangeEvent) anyObject());
    }

    @Test
    public void whenUpdatingNetworkNodeWithEncryptionKey_ThenEncryptionKeyShouldBeReset() throws Exception {
        NetworkNode originalNetworkNode = createNetworkNode();
        originalNetworkNode.setEncryptionKey("Some really secret key");
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setEncryptionKey("Some other secret key");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_ENCRYPTION_KEY, null);
    }

    @Test
    public void testNetworkNodeIsValid() {
        NetworkNode networkNode = createNetworkNode();

        assertThat(networkNode.isValid());
    }

    @Test
    public void whenNetworkNodeHasEmptyIpAddress_thenNetworkNodeIsValid() {
        NetworkNode networkNode = createNetworkNode();

        networkNode.setIpAddress("");
        assertThat(networkNode.isValid());

        networkNode.setIpAddress(null);
        assertThat(networkNode.isValid());
    }

    @Test
    public void whenNetworkNodeHasInvalidNonNullIpAddress_thenNetworkNodeIsInvalid() {
        NetworkNode networkNode = createNetworkNode();
        networkNode.setIpAddress("nope");

        assertThat(networkNode.isValid()).isFalse();
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

    @NonNull
    private NetworkNode createNetworkNode(@Nullable PropertyChangeListener propertyChangeListener) {
        NetworkNode networkNode = new NetworkNode();

        if (propertyChangeListener != null) {
            networkNode.addPropertyChangeListener(propertyChangeListener);
        }

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

        return networkNode;
    }

    @Nullable
    private NetworkNode createNetworkNode() {
        return createNetworkNode(null);
    }
}
