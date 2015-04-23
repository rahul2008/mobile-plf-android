package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode.PAIRED_STATUS;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public class AirPurifierManagerTest extends InstrumentationTestCase {

    private AirPurifierManager mPurifierMan;
    private SubscriptionHandler mSubscriptionMan;
    private AirPurifierEventListener mEventListener;

    private static final String PURIFIER_IP = "198.168.1.145";
    private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";

    @Override
    protected void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        AirPurifierManager.setDummyPurifierManagerForTesting(null);
        mPurifierMan = AirPurifierManager.getInstance();
        mSubscriptionMan = mock(SubscriptionHandler.class);

        mEventListener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(mEventListener);

        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        // Remove mock objects after tests
        AirPurifierManager.setDummyPurifierManagerForTesting(null);
        super.tearDown();
    }

    private AirPurifier createMockDisconnectedPurifier() {
        AirPurifier device1 = mock(AirPurifier.class);
        NetworkNode networkNode = mock(NetworkNode.class);
        when(device1.getNetworkNode()).thenReturn(networkNode);
        when(networkNode.getConnectionState()).thenReturn(ConnectionState.DISCONNECTED);
        return device1;
    }

    public void testNoSubscriptionAtStartup() {
        verifyZeroInteractions(mSubscriptionMan);
    }

    public void testAddPurifierListener() {
        AirPurifier device1 = createMockDisconnectedPurifier();

        mPurifierMan.setCurrentPurifier(device1);

        verify(device1, times(1)).setPurifierListener(mPurifierMan);
    }

    public void testAddPurifierListenerAfterCurrentPurifierIsAlreadySet() {
        AirPurifier device1 = createMockDisconnectedPurifier();
        mPurifierMan.setCurrentPurifier(device1);
        AirPurifier device2 = createMockDisconnectedPurifier();
        mPurifierMan.setCurrentPurifier(device2);

        verify(device1, times(1)).setPurifierListener(null);
        verify(device2, times(1)).setPurifierListener(mPurifierMan);
    }

    public void testRemovePurifierListener() {
        AirPurifier device1 = createMockDisconnectedPurifier();
        mPurifierMan.setCurrentPurifier(device1);
        mPurifierMan.removeCurrentPurifier();

        verify(device1, times(1)).setPurifierListener(null);
    }

    // ***** START TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CHANGES *****
    public void testSetFirstDisconnectedPurifier() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        verifyAddedPurifier(device1);
    }

    public void testSetFirstLocalPurifier() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        verifyAddedPurifier(device1);
    }

    public void testSetFirstRemotePurifierNotPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        verifyAddedPurifier(device1);
    }

    public void testSetFirstRemotePurifierPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        device1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device1);

        verifyAddedPurifier(device1);
    }

    public void testSetDisconnectedPurifierAfterDisconnected() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetLocalPurifierAfterDisconnected() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetRemotePurifierNotPairedAfterDisconnected() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetRemotePurifierPairedAfterDisconnected() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        ConnectionState device2ConnectedState = ConnectionState.CONNECTED_REMOTELY;
        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, device2ConnectedState, mSubscriptionMan);
        device2 = Mockito.spy(device2);
        device2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetDisconnectedPurifierAfterLocally() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);

        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetLocalPurifierAfterLocally() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetRemotePurifierNotPairedAfterLocally() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);

        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetRemotePurifierPairedAfterLocally() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);

        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        device2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetDisconnectedPurifierAfterNotPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);

        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetLocalPurifierAfterNotPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetRemotePurifierNotPairedAfterNotPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetRemotePurifierPairedAfterNotPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        device2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetDisconnectedPurifierAfterPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        device1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetLocalPurifierAfterPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        device1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetRemotePurifierNotPairedAfterPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        device1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testSetRemotePurifierPairedAfterPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        device1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        AirPurifier device2 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device2 = Mockito.spy(device2);
        device2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device2);

        verifyRemovedPurifier(device1);
        verifyAddedPurifier(device2);
    }

    public void testRemovePurifierPairedAfterNoPurifier() {
        mPurifierMan.removeCurrentPurifier();

        assertNull(mPurifierMan.getCurrentPurifier());
    }

    public void testRemovePurifierPairedAfterDisconnected() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        mPurifierMan.removeCurrentPurifier();

        verifyRemovedPurifier(device1);

        assertNull(mPurifierMan.getCurrentPurifier());
    }

    public void testRemovePurifierPairedAfterLocal() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        mPurifierMan.removeCurrentPurifier();

        verifyRemovedPurifier(device1);

        assertNull(mPurifierMan.getCurrentPurifier());
    }

    public void testRemovePurifierPairedAfterRemoteNotPaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        mPurifierMan.removeCurrentPurifier();

        verifyRemovedPurifier(device1);

        assertNull(mPurifierMan.getCurrentPurifier());
    }

    public void testRemovePurifierPairedAfterRemotePaired() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        device1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);
        mPurifierMan.removeCurrentPurifier();

        verifyRemovedPurifier(device1);

        assertNull(mPurifierMan.getCurrentPurifier());
    }

    // ***** END TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CHANGES *****

    // ***** START TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CONNECTIONSTATE
    // CHANGES *****

    public void testPurifierDisconnectedAfterLocal() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);

        reset(device1);
        device1.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);

        verify(device1).disableLocalSubscription();
        verify(device1, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
        verify(device1, never()).enableLocalSubscription();
        verify(device1, never()).disableRemoteSubscription(any(Context.class));

        verify(device1, never()).subscribe();
        verify(device1).stopResubscribe();

        verify(listener).onAirPurifierChanged();
    }

    public void testPurifierRemotedAfterLocal() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        device1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);

        reset(device1);
        device1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);

        verify(device1).disableLocalSubscription();
        verify(device1).enableRemoteSubscription(PurAirApplication.getAppContext());
        verify(device1, never()).enableLocalSubscription();
        verify(device1, never()).disableRemoteSubscription(any(Context.class));

        verify(device1).subscribe();
        verify(device1).stopResubscribe();

        verify(listener).onAirPurifierChanged();
    }

    public void testPurifierLocalAfterDisconnected() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);

        reset(device1);
        device1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        verify(device1, never()).disableLocalSubscription();
        verify(device1, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
        verify(device1).enableLocalSubscription();
        verify(device1, never()).disableRemoteSubscription(any(Context.class));

        verify(device1).subscribe();
        verify(device1, never()).stopResubscribe();

        verify(listener).onAirPurifierChanged();
    }

    public void testPurifierRemoteAfterDisconnected() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        device1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);

        reset(device1);
        device1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);

        verify(device1, never()).disableLocalSubscription();
        verify(device1).enableRemoteSubscription(PurAirApplication.getAppContext());
        verify(device1, never()).enableLocalSubscription();
        verify(device1, never()).disableRemoteSubscription(any(Context.class));

        verify(device1).subscribe();
        verify(device1, never()).stopResubscribe();

        verify(listener).onAirPurifierChanged();
    }

    public void testPurifierLocalAfterRemote() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.DISCONNECTED, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);

        reset(device1);
        device1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_LOCALLY);

        verify(device1, never()).disableLocalSubscription();
        verify(device1, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
        verify(device1).enableLocalSubscription();
        verify(device1, never()).disableRemoteSubscription(any(Context.class));

        verify(device1).subscribe();
        verify(device1, never()).stopResubscribe();

        verify(listener).onAirPurifierChanged();
    }

    public void testPurifierDisconnectedAfterRemote() {
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,
                mSubscriptionMan);
        device1 = Mockito.spy(device1);
        device1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);

        reset(device1);
        device1.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);

        verify(device1, never()).disableLocalSubscription();
        verify(device1, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
        verify(device1, never()).enableLocalSubscription();
        verify(device1, never()).disableRemoteSubscription(any(Context.class));

        verify(device1, never()).subscribe();
        verify(device1).stopResubscribe();

        verify(listener).onAirPurifierChanged();
    }

    // ***** END TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CONNECTIONSTATE
    // CHANGES *****

    // ***** START TEST TO START/STOP SUBSCRIPTION WHEN ADDING
    // PURIFIEREVENTLISTENERS *****
    
    public void testStartSubscriptionAddFirstEventListener() {
        AirPurifierManager.setDummyPurifierManagerForTesting(null);
        mPurifierMan = AirPurifierManager.getInstance();
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64, null, PURIFIER_IP, null, -1,
                ConnectionState.CONNECTED_LOCALLY, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);

        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);

        verify(device1, never()).disableLocalSubscription();
        verify(device1).enableLocalSubscription();

        verify(device1).subscribe();
        verify(device1, never()).stopResubscribe();
    }

    public void testStartSubscriptionAddSecondEventListener() {
        AirPurifierManager.setDummyPurifierManagerForTesting(null);
        mPurifierMan = AirPurifierManager.getInstance();
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64, null, PURIFIER_IP, null, -1,
                ConnectionState.CONNECTED_LOCALLY, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);
        
        reset(device1);
        
        AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener2);

        verify(device1, never()).disableLocalSubscription();
        verify(device1, never()).enableLocalSubscription();

        verify(device1, never()).subscribe();
        verify(device1, never()).stopResubscribe();
    }

    public void testStopSubscriptionRemoveFirstEventListener() {
        AirPurifierManager.setDummyPurifierManagerForTesting(null);
        mPurifierMan = AirPurifierManager.getInstance();
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64, null, PURIFIER_IP, null, -1,
                ConnectionState.CONNECTED_LOCALLY, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);
        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);

        reset(device1);
        
        mPurifierMan.removeAirPurifierEventListener(listener);

        verify(device1).disableLocalSubscription();
        verify(device1, never()).enableLocalSubscription();

        verify(device1, never()).subscribe();
        verify(device1).stopResubscribe();
    }

    public void testStopSubscriptionRemoveSecondEventListener() {
        AirPurifierManager.setDummyPurifierManagerForTesting(null);
        mPurifierMan = AirPurifierManager.getInstance();
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64, null, PURIFIER_IP, null, -1,
                ConnectionState.CONNECTED_LOCALLY, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);
        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);
        mPurifierMan.addAirPurifierEventListener(listener2);

        reset(device1);
        
        mPurifierMan.removeAirPurifierEventListener(listener);

        verify(device1, never()).disableLocalSubscription();
        verify(device1, never()).enableLocalSubscription();

        verify(device1, never()).subscribe();
        verify(device1, never()).stopResubscribe();
    }

    public void testStopSubscriptionRemoveBothEventListeners() {
        AirPurifierManager.setDummyPurifierManagerForTesting(null);
        mPurifierMan = AirPurifierManager.getInstance();
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64, null, PURIFIER_IP, null, -1,
                ConnectionState.CONNECTED_LOCALLY, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);
        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);
        mPurifierMan.addAirPurifierEventListener(listener2);

        reset(device1);
        
        mPurifierMan.removeAirPurifierEventListener(listener);
        mPurifierMan.removeAirPurifierEventListener(listener2);

        verify(device1).disableLocalSubscription();
        verify(device1, never()).enableLocalSubscription();

        verify(device1, never()).subscribe();
        verify(device1).stopResubscribe();
    }

    public void testStartStopSubscriptionAddRemoveListenersSequence() {
        AirPurifierManager.setDummyPurifierManagerForTesting(null);
        mPurifierMan = AirPurifierManager.getInstance();
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64, null, PURIFIER_IP, null, -1,
                ConnectionState.CONNECTED_LOCALLY, mSubscriptionMan);
        device1 = Mockito.spy(device1);
        mPurifierMan.setCurrentPurifier(device1);

        reset(device1);

        AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
        AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
        AirPurifierEventListener listener3 = mock(AirPurifierEventListener.class);
        mPurifierMan.addAirPurifierEventListener(listener);
        mPurifierMan.addAirPurifierEventListener(listener2);
        mPurifierMan.removeAirPurifierEventListener(listener);
        mPurifierMan.addAirPurifierEventListener(listener3);
        mPurifierMan.removeAirPurifierEventListener(listener2);
        mPurifierMan.removeAirPurifierEventListener(listener3);

        verify(device1).disableLocalSubscription();
        verify(device1).enableLocalSubscription();

        verify(device1).subscribe();
        verify(device1).stopResubscribe();
    }

    // ***** END TEST TO START/STOP SUBSCRIPTION WHEN ADDING
    // PURIFIEREVENTLISTENERS *****

    public void testDeadlock() {
        AirPurifierManager.setDummyPurifierManagerForTesting(null);
        mPurifierMan = AirPurifierManager.getInstance();
        AirPurifier device1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64, null, PURIFIER_IP, null, -1,
                ConnectionState.CONNECTED_LOCALLY, mSubscriptionMan);
        mPurifierMan.setCurrentPurifier(device1);

        device1.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
    }

    // -----------

    private void verifyAddedPurifier(AirPurifier device) {
        ConnectionState connectionState = device.getNetworkNode().getConnectionState();
        PAIRED_STATUS pairedStatus = device.getNetworkNode().getPairedState();
        if (connectionState == ConnectionState.CONNECTED_REMOTELY && pairedStatus == PAIRED_STATUS.PAIRED) {
            verify(device).enableRemoteSubscription(PurAirApplication.getAppContext());
            verify(device, never()).enableLocalSubscription();
            verify(device).subscribe();
        } else if (connectionState == ConnectionState.CONNECTED_LOCALLY) {
            verify(device, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
            verify(device).enableLocalSubscription();
            verify(device).subscribe();
        } else {
            verify(device, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
            verify(device, never()).enableLocalSubscription();
            verify(device, never()).subscribe();
        }
        verify(device, never()).disableLocalSubscription();
        verify(device, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
        verify(device, never()).stopResubscribe();
    }

    private void verifyRemovedPurifier(AirPurifier device) {
        ConnectionState connectionState = device.getNetworkNode().getConnectionState();
        PAIRED_STATUS pairedStatus = device.getNetworkNode().getPairedState();
        if (connectionState == ConnectionState.CONNECTED_REMOTELY && pairedStatus == PAIRED_STATUS.PAIRED) {
            verify(device, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
            // TODO DIComm Refactor - This should actually be called
            verify(device, never()).disableLocalSubscription();
            verify(device).stopResubscribe();
        } else if (connectionState == ConnectionState.CONNECTED_LOCALLY) {
            verify(device, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
            verify(device).disableLocalSubscription();
            verify(device).stopResubscribe();
        } else {
            verify(device, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
            verify(device, never()).disableLocalSubscription();
            verify(device, never()).stopResubscribe();
        }
        verify(device, never()).enableLocalSubscription();
        verify(device, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
        verify(device, never()).subscribe();
    }

    private AirPurifier createAirPurifier(CommunicationStrategy communicationStrategy, String purifierEui641, String usn, String ip, String name, long bootId,
        ConnectionState connectionState, SubscriptionHandler mSubscriptionMan) {
    
        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(bootId);
        networkNode.setCppId(purifierEui641);
        networkNode.setIpAddress(ip);
        networkNode.setName(name);
        networkNode.setConnectionState(connectionState);
        
        return new AirPurifier(networkNode, communicationStrategy, usn, mSubscriptionMan);
    }
}
