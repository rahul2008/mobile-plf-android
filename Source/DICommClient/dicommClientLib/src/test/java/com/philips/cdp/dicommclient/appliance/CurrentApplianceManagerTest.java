/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.testutil.TestAppliance;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

public class CurrentApplianceManagerTest extends RobolectricTest {

    private CurrentApplianceManager mCurrentApplianceMan;

    private static final String APPLIANCE_IP = "198.168.1.145";
    private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";

    public void setUp() throws Exception {
        super.setUp();

        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        mCurrentApplianceMan = CurrentApplianceManager.getInstance();

        DICommApplianceListener mApplianceListener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(mApplianceListener);
    }

    public void tearDown() throws Exception {
        // Remove mock objects after tests
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        super.tearDown();
    }

    // ***** START TESTS TO TOGGLE SUBSCRIPTION WHEN APPLIANCE CHANGES *****
    @Test
    public void testSetFirstDisconnectedPurifier() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        verify(appliance1, never()).enableCommunication();
        verify(appliance1, never()).subscribe();

        verify(appliance1, never()).disableCommunication();
        verify(appliance1, never()).stopResubscribe();
    }

    @Test
    public void testSetFirstLocalPurifier() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        verifyAddedPurifier(appliance1);
    }

    @Test
    public void testSetFirstRemotePurifierNotPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        verifyAddedPurifier(appliance1);
    }

    @Test
    public void testSetFirstRemotePurifierPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        verifyAddedPurifier(appliance1);
    }

    @Test
    public void testSetDisconnectedPurifierAfterDisconnected() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
    }

    @Test
    public void testSetLocalPurifierAfterDisconnected() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetRemotePurifierNotPairedAfterDisconnected() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetRemotePurifierPairedAfterDisconnected() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        ConnectionState device2ConnectedState = ConnectionState.CONNECTED_REMOTELY;
        DICommAppliance appliance2 = createAppliance(null, null, null, -1, device2ConnectedState);
        appliance2 = Mockito.spy(appliance2);
        appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetDisconnectedPurifierAfterLocally() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
    }

    @Test
    public void testSetLocalPurifierAfterLocally() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetRemotePurifierNotPairedAfterLocally() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetRemotePurifierPairedAfterLocally() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance2 = Mockito.spy(appliance2);
        appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetDisconnectedPurifierAfterNotPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
    }

    @Test
    public void testSetLocalPurifierAfterNotPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetRemotePurifierNotPairedAfterNotPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetRemotePurifierPairedAfterNotPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance2 = Mockito.spy(appliance2);
        appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetDisconnectedPurifierAfterPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
    }

    @Test
    public void testSetLocalPurifierAfterPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetRemotePurifierNotPairedAfterPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance2 = Mockito.spy(appliance2);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testSetRemotePurifierPairedAfterPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        DICommAppliance appliance2 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance2 = Mockito.spy(appliance2);
        appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance2);

        verifyRemovedPurifier(appliance1);
        verifyAddedPurifier(appliance2);
    }

    @Test
    public void testRemovePurifierPairedAfterNoPurifier() {
        mCurrentApplianceMan.removeCurrentAppliance();

        assertNull(mCurrentApplianceMan.getCurrentAppliance());
    }

    @Test
    public void testRemovePurifierPairedAfterDisconnected() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        mCurrentApplianceMan.removeCurrentAppliance();

        verifyRemovedPurifier(appliance1);

        assertNull(mCurrentApplianceMan.getCurrentAppliance());
    }

    @Test
    public void testRemovePurifierPairedAfterLocal() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        mCurrentApplianceMan.removeCurrentAppliance();

        verifyRemovedPurifier(appliance1);

        assertNull(mCurrentApplianceMan.getCurrentAppliance());
    }

    @Test
    public void testRemovePurifierPairedAfterRemoteNotPaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        mCurrentApplianceMan.removeCurrentAppliance();

        verifyRemovedPurifier(appliance1);

        assertNull(mCurrentApplianceMan.getCurrentAppliance());
    }

    @Test
    public void testRemovePurifierPairedAfterRemotePaired() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);
        mCurrentApplianceMan.removeCurrentAppliance();

        verifyRemovedPurifier(appliance1);

        assertNull(mCurrentApplianceMan.getCurrentAppliance());
    }

    // ***** END TESTS TO TOGGLE SUBSCRIPTION WHEN APPLIANCE CHANGES *****

    // ***** START TESTS TO TOGGLE SUBSCRIPTION WHEN APPLIANCE CONNECTIONSTATE
    // CHANGES *****

    @Test
    public void testPurifierDisconnectedAfterLocal() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        mCurrentApplianceMan.addCurrentApplianceChangedListener(changedListener);

        reset(appliance1);
        appliance1.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);

        verify(appliance1).disableCommunication();
        verify(appliance1, never()).enableCommunication();

        verify(appliance1, never()).subscribe();
        verify(appliance1).stopResubscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testPurifierRemotedAfterLocal() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        mCurrentApplianceMan.addCurrentApplianceChangedListener(changedListener);

        reset(appliance1);
        appliance1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);

        verify(appliance1).disableCommunication();
        verify(appliance1).enableCommunication();

        verify(appliance1).subscribe();
        verify(appliance1).stopResubscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testPurifierLocalAfterDisconnected() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        mCurrentApplianceMan.addCurrentApplianceChangedListener(changedListener);

        reset(appliance1);
        appliance1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        // dicomm refactor: there is change in the behaviour, on app level it would call disable but internally it does nothing when we switch from disconnected state.
        verify(appliance1).disableCommunication();
        verify(appliance1).stopResubscribe();

        verify(appliance1).enableCommunication();
        verify(appliance1).subscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testPurifierRemoteAfterDisconnected() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.DISCONNECTED);
        appliance1 = Mockito.spy(appliance1);
        appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        mCurrentApplianceMan.addCurrentApplianceChangedListener(changedListener);

        reset(appliance1);
        appliance1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);

        // dicomm refactor: there is change in the behaviour, on app level it would call disable but internally it does nothing when we switch from disconnected state.
        verify(appliance1).disableCommunication();
        verify(appliance1).stopResubscribe();

        verify(appliance1).enableCommunication();
        verify(appliance1).subscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testPurifierLocalAfterRemote() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        mCurrentApplianceMan.addCurrentApplianceChangedListener(changedListener);

        reset(appliance1);
        appliance1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        // dicomm refactor: there is change in the behaviour, on app level it would call disable but internally it does nothing when we switch from disconnected state.
        verify(appliance1).disableCommunication();
        verify(appliance1).stopResubscribe();

        verify(appliance1).enableCommunication();
        verify(appliance1).subscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    @Test
    public void testPurifierDisconnectedAfterRemote() {
        DICommAppliance appliance1 = createAppliance(null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
        appliance1 = Mockito.spy(appliance1);
        appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);
        CurrentApplianceChangedListener changedListener = mock(CurrentApplianceChangedListener.class);
        mCurrentApplianceMan.addCurrentApplianceChangedListener(changedListener);

        reset(appliance1);
        appliance1.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);

        // dicomm refactor: there is change in the behaviour, on app level it would call disable but internally it does nothing when we switch to disconnected state.
        verify(appliance1).disableCommunication();
        verify(appliance1).stopResubscribe();

        verify(appliance1, never()).enableCommunication();
        verify(appliance1, never()).subscribe();

        verify(changedListener).onCurrentApplianceChanged();
    }

    // ***** END TESTS TO TOGGLE SUBSCRIPTION WHEN APPLIANCE CONNECTIONSTATE
    // CHANGES *****

    // ***** START TEST TO START/STOP SUBSCRIPTION WHEN ADDING
    // PURIFIEREVENTLISTENERS *****

    @Test
    public void testStartSubscriptionAddFirstEventListener() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        mCurrentApplianceMan = CurrentApplianceManager.getInstance();
        DICommAppliance appliance1 = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);

        verify(appliance1, never()).disableCommunication();
        verify(appliance1).enableCommunication();

        verify(appliance1).subscribe();
        verify(appliance1, never()).stopResubscribe();
    }

    @Test
    public void testStartSubscriptionAddSecondEventListener() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        mCurrentApplianceMan = CurrentApplianceManager.getInstance();
        DICommAppliance appliance1 = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);

        reset(appliance1);

        DICommApplianceListener listener2 = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener2);

        verify(appliance1, never()).disableCommunication();
        verify(appliance1, never()).enableCommunication();

        verify(appliance1, never()).subscribe();
        verify(appliance1, never()).stopResubscribe();
    }

    @Test
    public void testStopSubscriptionRemoveFirstEventListener() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        mCurrentApplianceMan = CurrentApplianceManager.getInstance();
        DICommAppliance appliance1 = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);
        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);

        reset(appliance1);

        mCurrentApplianceMan.removeApplianceListener(listener);

        verify(appliance1).disableCommunication();
        verify(appliance1, never()).enableCommunication();

        verify(appliance1, never()).subscribe();
        verify(appliance1).stopResubscribe();
    }

    @Test
    public void testStopSubscriptionRemoveSecondEventListener() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        mCurrentApplianceMan = CurrentApplianceManager.getInstance();
        DICommAppliance appliance1 = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);
        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        DICommApplianceListener listener2 = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);
        mCurrentApplianceMan.addApplianceListener(listener2);

        reset(appliance1);

        mCurrentApplianceMan.removeApplianceListener(listener);

        verify(appliance1, never()).disableCommunication();
        verify(appliance1, never()).enableCommunication();

        verify(appliance1, never()).subscribe();
        verify(appliance1, never()).stopResubscribe();
    }

    @Test
    public void testStopSubscriptionRemoveBothEventListeners() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        mCurrentApplianceMan = CurrentApplianceManager.getInstance();
        DICommAppliance appliance1 = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);
        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        DICommApplianceListener listener2 = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);
        mCurrentApplianceMan.addApplianceListener(listener2);

        reset(appliance1);

        mCurrentApplianceMan.removeApplianceListener(listener);
        mCurrentApplianceMan.removeApplianceListener(listener2);

        verify(appliance1).disableCommunication();
        verify(appliance1, never()).enableCommunication();

        verify(appliance1, never()).subscribe();
        verify(appliance1).stopResubscribe();
    }

    @Test
    public void testStartStopSubscriptionAddRemoveListenersSequence() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        mCurrentApplianceMan = CurrentApplianceManager.getInstance();
        DICommAppliance appliance1 = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
        appliance1 = Mockito.spy(appliance1);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        reset(appliance1);

        DICommApplianceListener listener = mock(DICommApplianceListener.class);
        DICommApplianceListener listener2 = mock(DICommApplianceListener.class);
        DICommApplianceListener listener3 = mock(DICommApplianceListener.class);
        mCurrentApplianceMan.addApplianceListener(listener);
        mCurrentApplianceMan.addApplianceListener(listener2);
        mCurrentApplianceMan.removeApplianceListener(listener);
        mCurrentApplianceMan.addApplianceListener(listener3);
        mCurrentApplianceMan.removeApplianceListener(listener2);
        mCurrentApplianceMan.removeApplianceListener(listener3);

        verify(appliance1).disableCommunication();
        verify(appliance1).enableCommunication();

        verify(appliance1).subscribe();
        verify(appliance1).stopResubscribe();
    }

    // ***** END TEST TO START/STOP SUBSCRIPTION WHEN ADDING
    // PURIFIEREVENTLISTENERS *****

    @Test
    public void testDeadlock() {
        CurrentApplianceManager.setDummyCurrentApplianceManagerForTesting(null);
        mCurrentApplianceMan = CurrentApplianceManager.getInstance();
        DICommAppliance appliance1 = createAppliance(APPLIANCE_CPPID, APPLIANCE_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
        mCurrentApplianceMan.setCurrentAppliance(appliance1);

        appliance1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
    }

    // -----------

    private void verifyAddedPurifier(DICommAppliance appliance) {
        verify(appliance).enableCommunication();
        verify(appliance).subscribe();

        verify(appliance, never()).disableCommunication();
        verify(appliance, never()).stopResubscribe();
    }

    private void verifyRemovedPurifier(DICommAppliance appliance) {
        verify(appliance).disableCommunication();
        verify(appliance).stopResubscribe();

        verify(appliance, never()).enableCommunication();
        verify(appliance, never()).subscribe();
    }

    private DICommAppliance createAppliance(String cppId, String ip, String name, long bootId, ConnectionState connectionState) {

        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(bootId);
        networkNode.setCppId(cppId);
        networkNode.setIpAddress(ip);
        networkNode.setName(name);
        networkNode.setConnectionState(connectionState);

        return new TestAppliance(networkNode);
    }
}
