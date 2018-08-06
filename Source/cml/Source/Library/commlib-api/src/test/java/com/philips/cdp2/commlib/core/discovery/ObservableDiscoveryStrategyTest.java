package com.philips.cdp2.commlib.core.discovery;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy.DiscoveryListener;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class ObservableDiscoveryStrategyTest {

    @Mock
    private Handler handlerMock;

    @Mock
    private DiscoveryListener discoveryListenerMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    private ObservableDiscoveryStrategy strategy;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        HandlerProvider.enableMockedHandler(handlerMock);

        strategy = new ObservableDiscoveryStrategy() {
            @Override
            public void start() throws MissingPermissionException {

            }

            @Override
            public void start(@NonNull final Set<String> modelIds) throws MissingPermissionException {

            }

            @Override
            public void stop() {

            }

            @Override
            public void clearDiscoveredNetworkNodes() {

            }
        };

        strategy.addDiscoveryListener(discoveryListenerMock);
    }

    @Test
    public void whenRequestedToNotifyDiscoveryStarted_thenCallbackIsPostedOnMainThread() throws Exception {

        strategy.notifyDiscoveryStarted();

        verify(handlerMock).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(discoveryListenerMock).onDiscoveryStarted();
    }

    @Test
    public void whenRequestedToNotifyDiscoveryStopped_thenCallbackIsPostedOnMainThread() throws Exception {

        strategy.notifyDiscoveryStopped();

        verify(handlerMock).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(discoveryListenerMock).onDiscoveryStopped();
    }

    @Test
    public void whenRequestedToNotifyNodeDiscovered_thenCallbackIsPostedOnMainThread() throws Exception {

        final NetworkNode networkNode = new NetworkNode();
        strategy.notifyNetworkNodeDiscovered(networkNode);

        verify(handlerMock).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(discoveryListenerMock).onNetworkNodeDiscovered(networkNode);
    }

    @Test
    public void whenRequestedToNotifyNodeLost_thenCallbackIsPostedOnMainThread() throws Exception {

        final NetworkNode networkNode = new NetworkNode();
        strategy.notifyNetworkNodeLost(networkNode);

        verify(handlerMock).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(discoveryListenerMock).onNetworkNodeLost(networkNode);
    }

    @Test
    public void whenRequestedToNotifyDiscoveryFailedToStart_thenCallbackIsPostedOnMainThread() throws Exception {

        strategy.notifyDiscoveryFailedToStart();

        verify(handlerMock).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();
        verify(discoveryListenerMock).onDiscoveryFailedToStart();
    }

    @Test
    public void givenDiscoveryListenerIsAddedAndThenRemoved_whenRequestedToNotify_thenNotificationIsNoLongerSentToListener() throws Exception {
        strategy.removeDiscoveryListener(discoveryListenerMock);

        strategy.notifyDiscoveryFailedToStart();

        verifyZeroInteractions(handlerMock);
    }
}