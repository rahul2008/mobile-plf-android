/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import android.os.Parcel;
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

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_BOOT_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_TYPE;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_ENCRYPTION_KEY;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_HOME_SSID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IP_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.PairingState.PAIRED;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
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
    }

    @Test
    public void test_ShouldReturnKey_WhenKeyIsSet_AndGetIsCalled() {
        NetworkNode networkNode = new NetworkNode();

        networkNode.setEncryptionKey(TEST_KEY);

        String encryptionKey = networkNode.getEncryptionKey();

        assertEquals(TEST_KEY, encryptionKey);
    }

    @Test
    public void test_ShouldInformListener_WhenKeyIsSet() {
        NetworkNode networkNode = new NetworkNode();

        networkNode.addPropertyChangeListener(mockPropertyChangeListener);

        networkNode.setEncryptionKey(TEST_KEY);

        verify(mockPropertyChangeListener).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void test_ShouldInformListener_WhenKeyIsReset() {
        NetworkNode networkNode = new NetworkNode();

        networkNode.addPropertyChangeListener(mockPropertyChangeListener);

        networkNode.setEncryptionKey(TEST_KEY);
        verify(mockPropertyChangeListener, times(1)).propertyChange(any(PropertyChangeEvent.class));

        networkNode.setEncryptionKey(null);
        verify(mockPropertyChangeListener, times(2)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void test_ShouldNotInformListener_WhenTheSameKeyIsSetTwice() {
        NetworkNode networkNode = new NetworkNode();

        networkNode.addPropertyChangeListener(mockPropertyChangeListener);

        networkNode.setEncryptionKey(TEST_KEY);
        networkNode.setEncryptionKey(TEST_KEY);
        verify(mockPropertyChangeListener, times(1)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void whenPropertyChanges_thenPropertyChangeEventIsFired() {
        createNetworkNode(mockPropertyChangeListener);

        verify(mockPropertyChangeListener, times(13)).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void whenUpdatingNetworkNodeWithNetworkNodeThatHasOtherCppId_ThenOriginalNetworkNodeShouldBeUnchanged() {
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
    public void whenUpdatingNetworkNodeWithNetworkNodeThatHasOtherSsid_ThenSsidShouldBeChanged() {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setHomeSsid("Some other ssid");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_HOME_SSID, "Some other ssid");
    }

    @Test
    public void whenUpdatingNetworkNodeWithNetworkNodeThatHasOtherIpAddress_ThenIpAddressShouldBeChanged() {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setIpAddress("10.10.10.10");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_IP_ADDRESS, "10.10.10.10");
    }

    @Test
    public void whenUpdatingNetworkNodeWithNetworkNodeThatHasOtherName_ThenNameShouldBeChanged() {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setName("My awesome appliance");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_DEVICE_NAME, "My awesome appliance");
    }

    @Test
    public void whenUpdatingNetworkNodeWithNetworkNodeThatHasOtherModelId_ThenModelIdShouldBeChanged() {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setModelId("My awesome modelId");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_MODEL_ID, "My awesome modelId");
    }

    @Test
    public void whenUpdatingNetworkNodeWithNetworkNodeThatHasOtherDeviceType_ThenDeviceTypeShouldBeChanged() {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setDeviceType("My awesome deviceType");
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_DEVICE_TYPE, "My awesome deviceType");
    }

    @Test
    public void whenUpdatingNetworkNodeWithBootId_ThenBootIdShouldBeChanged() {
        NetworkNode originalNetworkNode = createNetworkNode();
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setBootId(10L);
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verifyPropertyChangeCalled(KEY_BOOT_ID, 10L);
    }

    @Test
    public void whenUpdatingNetworkNodeWithNullEncryptionKey_ThenEncryptionKeyShouldNotBeChanged() {
        NetworkNode originalNetworkNode = createNetworkNode();
        originalNetworkNode.setEncryptionKey("Some really secret key");
        NetworkNode networkNodeForUpdate = createNetworkNode();
        networkNodeForUpdate.setEncryptionKey(null);
        originalNetworkNode.addPropertyChangeListener(mockPropertyChangeListener);

        originalNetworkNode.updateWithValuesFrom(networkNodeForUpdate);

        verify(mockPropertyChangeListener, never()).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    public void whenUpdatingNetworkNodeWithEncryptionKey_ThenEncryptionKeyShouldBeReset() {
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

    @Test
    public void givenNetworkNodeIsSerialized_whenItIsDEsirialized_thenNodeIsTheSame() {
        NetworkNode networkNode = createNetworkNode();

        Parcel parcel = Parcel.obtain();
        networkNode.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        NetworkNode createdFromParcel = NetworkNode.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertThat(networkNode).isEqualTo(createdFromParcel);
        assertThat(networkNode.getMacAddress()).isEqualTo(createdFromParcel.getMacAddress());
        assertThat(networkNode.getIpAddress()).isEqualTo(createdFromParcel.getIpAddress());
        assertThat(networkNode.getDeviceType()).isEqualTo(createdFromParcel.getDeviceType());
        assertThat(networkNode.getModelId()).isEqualTo(createdFromParcel.getModelId());
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
        networkNode.setMacAddress("00:11:22:33:44:55");
        networkNode.setMismatchedPin("mismatched pin");

        return networkNode;
    }

    @NonNull
    private NetworkNode createNetworkNode() {
        return createNetworkNode(null);
    }
}
