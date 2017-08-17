/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.appliance;

import android.os.Handler;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceChangedListener;
import com.philips.cdp.dicommclient.appliance.DICommApplianceListener;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.NullCommunicationStrategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static com.philips.cdp2.commlib.core.util.HandlerProvider.enableMockedHandler;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@Ignore // Is going to be removed, test kept for reference.
public class CurrentApplianceManagerTest {

    private CurrentApplianceManager currentApplianceManager;

    private static final String APPLIANCE_IP = "198.168.1.145";
    private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";

    @Mock
    private Handler handlerMock;

    @Mock
    private DICommApplianceListener applianceListener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();
        enableMockedHandler(handlerMock);

        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        currentApplianceManager = CurrentApplianceManager.getInstance();

        currentApplianceManager.addApplianceListener(applianceListener);
    }

    @After
    public void tearDown() throws Exception {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
    }

    @Test
    public void testSetFirstDisconnectedAppliance() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        verify(appliance, never()).enableCommunication();
        verify(appliance, never()).subscribe();

        verify(appliance, never()).disableCommunication();
        verify(appliance, never()).stopResubscribe();
    }

    @Test
    public void testSetFirstLocalAppliance() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        verifyAddedAppliance(appliance);
    }

    @Test
    public void testSetFirstRemoteApplianceNotPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        verifyAddedAppliance(appliance);
    }

    @Test
    public void testSetFirstRemoteAppliancePaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        appliance.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance);

        verifyAddedAppliance(appliance);
    }

    @Test
    public void testSetDisconnectedApplianceAfterDisconnected() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
    }

    @Test
    public void testSetLocalApplianceAfterDisconnected() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetRemoteApplianceNotPairedAfterDisconnected() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetRemoteAppliancePairedAfterDisconnected() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        appliance2.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetDisconnectedApplianceAfterLocally() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
    }

    @Test
    public void testSetLocalApplianceAfterLocally() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetRemoteApplianceNotPairedAfterLocally() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetRemoteAppliancePairedAfterLocally() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        appliance2.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetDisconnectedApplianceAfterNotPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
    }

    @Test
    public void testSetLocalApplianceAfterNotPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetRemoteApplianceNotPairedAfterNotPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetRemoteAppliancePairedAfterNotPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        appliance2.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetDisconnectedApplianceAfterPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        appliance.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
    }

    @Test
    public void testSetLocalApplianceAfterPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        appliance.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetRemoteApplianceNotPairedAfterPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        appliance.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testSetRemoteAppliancePairedAfterPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        appliance.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        Appliance appliance2 = createAppliance(null, null, null, -1);
        appliance2 = Mockito.spy(appliance2);
        appliance2.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance2);

        verifyRemovedAppliance(appliance);
        verifyAddedAppliance(appliance2);
    }

    @Test
    public void testRemoveAppliancePairedAfterNoAppliance() {
        currentApplianceManager.removeCurrentAppliance();

        assertNull(currentApplianceManager.getCurrentAppliance());
    }

    @Test
    public void testRemoveAppliancePairedAfterDisconnected() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        currentApplianceManager.removeCurrentAppliance();

        verifyRemovedAppliance(appliance);

        assertNull(currentApplianceManager.getCurrentAppliance());
    }

    @Test
    public void testRemoveAppliancePairedAfterLocal() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        currentApplianceManager.removeCurrentAppliance();

        verifyRemovedAppliance(appliance);

        assertNull(currentApplianceManager.getCurrentAppliance());
    }

    @Test
    public void testRemoveAppliancePairedAfterRemoteNotPaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        currentApplianceManager.removeCurrentAppliance();

        verifyRemovedAppliance(appliance);

        assertNull(currentApplianceManager.getCurrentAppliance());
    }

    @Test
    public void testRemoveAppliancePairedAfterRemotePaired() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        appliance.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);
        currentApplianceManager.removeCurrentAppliance();

        verifyRemovedAppliance(appliance);

        assertNull(currentApplianceManager.getCurrentAppliance());
    }

    @Test
    public void testApplianceDisconnectedAfterLocal() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        currentApplianceManager.addCurrentApplianceChangedListener(changedListener);

        reset(appliance);
        // TODO FIXME let appliance be available via LAN

        verify(appliance).disableCommunication();
        verify(appliance, never()).enableCommunication();

        verify(appliance, never()).subscribe();
        verify(appliance).stopResubscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testApplianceRemotedAfterLocal() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        appliance.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        currentApplianceManager.addCurrentApplianceChangedListener(changedListener);

        reset(appliance);
        // TODO FIXME let appliance be available via LAN

        verify(appliance).disableCommunication();
        verify(appliance).enableCommunication();

        verify(appliance).subscribe();
        verify(appliance).stopResubscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testApplianceLocalAfterDisconnected() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        currentApplianceManager.addCurrentApplianceChangedListener(changedListener);

        reset(appliance);
        // TODO FIXME let appliance be disconnected

        // dicomm refactor: there is change in the behaviour, on app level it would call disable but internally it does nothing when we switch from disconnected state.
        verify(appliance).disableCommunication();
        verify(appliance).stopResubscribe();

        verify(appliance).enableCommunication();
        verify(appliance).subscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testApplianceRemoteAfterDisconnected() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        appliance.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        currentApplianceManager.addCurrentApplianceChangedListener(changedListener);

        reset(appliance);
        // TODO FIXME let appliance be available via cloud

        // dicomm refactor: there is change in the behaviour, on app level it would call disable but internally it does nothing when we switch from disconnected state.
        verify(appliance).disableCommunication();
        verify(appliance).stopResubscribe();

        verify(appliance).enableCommunication();
        verify(appliance).subscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testApplianceLocalAfterRemote() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        currentApplianceManager.addCurrentApplianceChangedListener(changedListener);

        reset(appliance);
        // TODO FIXME let appliance be available via cloud

        // dicomm refactor: there is change in the behaviour, on app level it would call disable but internally it does nothing when we switch from disconnected state.
        verify(appliance).disableCommunication();
        verify(appliance).stopResubscribe();

        verify(appliance).enableCommunication();
        verify(appliance).subscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testApplianceDisconnectedAfterRemote() {
        Appliance appliance = createAppliance(null, null, null, -1);
        appliance = Mockito.spy(appliance);
        appliance.getNetworkNode().setPairedState(NetworkNode.PairingState.PAIRED);
        currentApplianceManager.setCurrentAppliance(appliance);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        currentApplianceManager.addCurrentApplianceChangedListener(changedListener);

        reset(appliance);
        // TODO FIXME let appliance be available via cloud

        // dicomm refactor: there is change in the behaviour, on app level it would call disable but internally it does nothing when we switch to disconnected state.
        verify(appliance).disableCommunication();
        verify(appliance).stopResubscribe();

        verify(appliance, never()).enableCommunication();
        verify(appliance, never()).subscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testStartSubscriptionAddFirstEventListener() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        currentApplianceManager = CurrentApplianceManager.getInstance();
        Appliance appliance = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);

        verify(appliance, never()).disableCommunication();
        verify(appliance).enableCommunication();

        verify(appliance).subscribe();
        verify(appliance, never()).stopResubscribe();
    }

    @Test
    public void testStartSubscriptionAddSecondEventListener() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        currentApplianceManager = CurrentApplianceManager.getInstance();
        Appliance appliance = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);

        reset(appliance);

        DICommApplianceListener listener2 = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener2);

        verify(appliance, never()).disableCommunication();
        verify(appliance, never()).enableCommunication();

        verify(appliance, never()).subscribe();
        verify(appliance, never()).stopResubscribe();
    }

    @Test
    public void testStopSubscriptionRemoveFirstEventListener() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        currentApplianceManager = CurrentApplianceManager.getInstance();
        Appliance appliance = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);
        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);

        reset(appliance);

        currentApplianceManager.removeApplianceListener(listener);

        verify(appliance).disableCommunication();
        verify(appliance, never()).enableCommunication();

        verify(appliance, never()).subscribe();
        verify(appliance).stopResubscribe();
    }

    @Test
    public void testStopSubscriptionRemoveSecondEventListener() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        currentApplianceManager = CurrentApplianceManager.getInstance();
        Appliance appliance = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);
        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        DICommApplianceListener listener2 = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);
        currentApplianceManager.addApplianceListener(listener2);

        reset(appliance);

        currentApplianceManager.removeApplianceListener(listener);

        verify(appliance, never()).disableCommunication();
        verify(appliance, never()).enableCommunication();

        verify(appliance, never()).subscribe();
        verify(appliance, never()).stopResubscribe();
    }

    @Test
    public void testStopSubscriptionRemoveBothEventListeners() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        currentApplianceManager = CurrentApplianceManager.getInstance();
        Appliance appliance = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);
        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        DICommApplianceListener listener2 = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);
        currentApplianceManager.addApplianceListener(listener2);

        reset(appliance);

        currentApplianceManager.removeApplianceListener(listener);
        currentApplianceManager.removeApplianceListener(listener2);

        verify(appliance).disableCommunication();
        verify(appliance, never()).enableCommunication();

        verify(appliance, never()).subscribe();
        verify(appliance).stopResubscribe();
    }

    @Test
    public void testStartStopSubscriptionAddRemoveListenersSequence() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        currentApplianceManager = CurrentApplianceManager.getInstance();
        Appliance appliance = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1);
        appliance = Mockito.spy(appliance);
        currentApplianceManager.setCurrentAppliance(appliance);

        reset(appliance);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        DICommApplianceListener listener2 = mock(DICommApplianceListener.class);
        DICommApplianceListener listener3 = mock(DICommApplianceListener.class);
        currentApplianceManager.addApplianceListener(listener);
        currentApplianceManager.addApplianceListener(listener2);
        currentApplianceManager.removeApplianceListener(listener);
        currentApplianceManager.addApplianceListener(listener3);
        currentApplianceManager.removeApplianceListener(listener2);
        currentApplianceManager.removeApplianceListener(listener3);

        verify(appliance).disableCommunication();
        verify(appliance).enableCommunication();

        verify(appliance).subscribe();
        verify(appliance).stopResubscribe();
    }

    @Test
    public void testDeadlock() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        currentApplianceManager = CurrentApplianceManager.getInstance();
        Appliance appliance = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1);
        currentApplianceManager.setCurrentAppliance(appliance);
    }

    private void verifyAddedAppliance(Appliance appliance) {
        verify(appliance).enableCommunication();
        verify(appliance).subscribe();

        verify(appliance, never()).disableCommunication();
        verify(appliance, never()).stopResubscribe();
    }

    private void verifyRemovedAppliance(Appliance appliance) {
        verify(appliance).disableCommunication();
        verify(appliance).stopResubscribe();

        verify(appliance, never()).enableCommunication();
        verify(appliance, never()).subscribe();
    }

    private Appliance createAppliance(String cppId, String ip, String name, long bootId) {

        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(bootId);
        networkNode.setCppId(cppId);
        networkNode.setIpAddress(ip);
        networkNode.setName(name);

        return new Appliance(networkNode, new NullCommunicationStrategy()) {
            @Override
            public String getDeviceType() {
                return null;
            }
        };
    }
}
