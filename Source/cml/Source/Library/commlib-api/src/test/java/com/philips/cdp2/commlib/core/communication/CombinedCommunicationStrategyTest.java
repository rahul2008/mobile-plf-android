/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.communication;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.philips.cdp.dicommclient.request.Error.NOT_CONNECTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CombinedCommunicationStrategyTest {

    private static final boolean AVAILABLE = true;
    private static final boolean UNAVAILABLE = false;

    @Mock
    CommunicationStrategy availableStrategyMock;
    @Mock
    CommunicationStrategy unavailableStrategyMock;
    @Mock
    ResponseHandler responseHandlerMock;
    @Mock
    SubscriptionEventListener subscriptionEventListenerMock;
    @Mock
    AvailabilityListener<CommunicationStrategy> strategyAvailabilityListenerMock;

    private AvailabilityListener<CommunicationStrategy> strategyAvailabilityListener;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @SuppressWarnings("unchecked")
    private CommunicationStrategy createCommunicationStrategy(boolean available) {
        DICommLog.disableLogging();
        CommunicationStrategy strategy = mock(CommunicationStrategy.class);
        when(strategy.isAvailable()).thenReturn(available);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                strategyAvailabilityListener = invocation.getArgument(0);
                return null;
            }
        }).when(strategy).addAvailabilityListener(isA(AvailabilityListener.class));

        return strategy;
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenConstructedWithZeroStrategies_ThenThrowsError() {
        new CombinedCommunicationStrategy();
    }

    @Test
    public void whenIsAvailableIsCalled_withAvailableStrategies_ThenReturnsTrue() {
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                createCommunicationStrategy(AVAILABLE),
                createCommunicationStrategy(AVAILABLE)
        );

        assertThat(strategy.isAvailable()).isTrue();
    }

    @Test
    public void whenIsAvailableIsCalled_withSomeAvailableStrategies_ThenReturnsTrue() {
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                createCommunicationStrategy(UNAVAILABLE),
                createCommunicationStrategy(AVAILABLE)
        );

        assertThat(strategy.isAvailable()).isTrue();
    }

    @Test
    public void whenIsAvailableIsCalled_withUnavailableStrategies_ThenReturnsTrue() {
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                createCommunicationStrategy(UNAVAILABLE),
                createCommunicationStrategy(UNAVAILABLE)
        );

        assertThat(strategy.isAvailable()).isFalse();
    }

    @Test
    public void whenPutPropsIsCalled_withAvailableStrategies_ThenCallsPreferredStrategy() {
        final CommunicationStrategy preferredStrategy = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                preferredStrategy,
                createCommunicationStrategy(AVAILABLE)
        );

        strategy.putProperties(null, null, 0, null);

        verify(preferredStrategy).putProperties(null, null, 0, null);
    }

    @Test
    public void whenPutPropsIsCalled_withAvailableStrategies_ThenDoesNotCallErrorOnResponseHandler() {
        final CommunicationStrategy preferredStrategy = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                preferredStrategy,
                createCommunicationStrategy(AVAILABLE)
        );

        strategy.putProperties(null, null, 0, responseHandlerMock);

        verify(responseHandlerMock, never()).onError(any(Error.class), anyString());
    }

    @Test
    public void whenPutPropsIsCalled_withSomeAvailableStrategies_ThenCallsPreferredStrategy() {
        final CommunicationStrategy preferredStrategy = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                createCommunicationStrategy(UNAVAILABLE),
                preferredStrategy
        );

        strategy.putProperties(null, null, 0, null);

        verify(preferredStrategy).putProperties(null, null, 0, null);
    }

    @Test
    public void whenPutPropsIsCalled_withSomeAvailableStrategies_ThenDoesNotCallErrorOnResponseHandler() {
        final CommunicationStrategy preferredStrategy = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                createCommunicationStrategy(UNAVAILABLE),
                preferredStrategy
        );

        strategy.putProperties(null, null, 0, responseHandlerMock);

        verify(responseHandlerMock, never()).onError(any(Error.class), anyString());
    }

    @Test
    public void whenPutPropsIsCalled_withUnavailableStrategies_ThenCallsErrorOnResponseHandler() {
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                createCommunicationStrategy(UNAVAILABLE),
                createCommunicationStrategy(UNAVAILABLE)
        );

        strategy.putProperties(null, null, 0, responseHandlerMock);

        verify(responseHandlerMock).onError(eq(NOT_CONNECTED), (String) any());
    }

    @Test
    public void whenAvailabilityChangesAndAStrategyStillAvailable_thenNoListenersAreCalled() {
        CommunicationStrategy one = createCommunicationStrategy(AVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        when(one.isAvailable()).thenReturn(false);
        reset(strategyAvailabilityListenerMock);

        strategyAvailabilityListener.onAvailabilityChanged(one);

        verify(strategyAvailabilityListenerMock, never()).onAvailabilityChanged(strategy);
    }

    @Test
    public void whenAvailabilityChangesAndAStrategyStillNotAvailable_thenNoListenersAreCalled() {
        CommunicationStrategy one = createCommunicationStrategy(UNAVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(UNAVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        reset(strategyAvailabilityListenerMock);

        strategyAvailabilityListener.onAvailabilityChanged(one);

        verify(strategyAvailabilityListenerMock, never()).onAvailabilityChanged(strategy);
    }

    @Test
    public void whenAvailabilityChangesAndAStrategyBecomesAvailable_thenListenersAreCalled() {
        CommunicationStrategy one = createCommunicationStrategy(UNAVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(UNAVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        when(two.isAvailable()).thenReturn(true);
        reset(strategyAvailabilityListenerMock);

        strategyAvailabilityListener.onAvailabilityChanged(one);

        verify(strategyAvailabilityListenerMock).onAvailabilityChanged(strategy);
    }

    @Test
    public void whenAvailabilityChangesAndNoStrategiesStillAvailable_thenListenersAreCalled() {
        CommunicationStrategy one = createCommunicationStrategy(AVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(UNAVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        when(one.isAvailable()).thenReturn(false);
        reset(strategyAvailabilityListenerMock);

        strategyAvailabilityListener.onAvailabilityChanged(one);

        verify(strategyAvailabilityListenerMock).onAvailabilityChanged(strategy);
    }

    @Test
    public void whenAvailabilityChangesAndAnotherStrategyStillAvailable_thenMoveSubscription() {
        CommunicationStrategy one = createCommunicationStrategy(AVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        whenSubscribeThenSuccess(one);
        strategy.subscribe("portname", 1, 0, responseHandlerMock);
        when(one.isAvailable()).thenReturn(false);

        strategyAvailabilityListener.onAvailabilityChanged(one);

        verify(one).unsubscribe(eq("portname"), eq(1), any(ResponseHandler.class));
        verify(two).subscribe(eq("portname"), eq(1), eq(0), any(ResponseHandler.class));
    }

    @Test
    public void whenUnsubscribingAfterAvailabilityChange_thenBothStrategiesUnsubscribed() {
        CommunicationStrategy one = createCommunicationStrategy(AVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        whenSubscribeThenSuccess(one);
        strategy.subscribe("portname", 1, 0, responseHandlerMock);
        when(one.isAvailable()).thenReturn(false);

        strategyAvailabilityListener.onAvailabilityChanged(one);
        strategy.unsubscribe("portname", 1, responseHandlerMock);

        verify(one).unsubscribe(eq("portname"), eq(1), any(ResponseHandler.class));
        verify(two).unsubscribe(eq("portname"), eq(1), any(ResponseHandler.class));
    }

    @Test
    public void whenAvailabilityChangeAfterSubscribe_thenNoSubscriptionMove() {
        CommunicationStrategy one = createCommunicationStrategy(AVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        whenSubscribeThenSuccess(one);
        strategy.subscribe("portname", 1, 0, responseHandlerMock);
        when(one.isAvailable()).thenReturn(false);

        strategy.unsubscribe("portname", 1, responseHandlerMock);
        reset(one, two);
        strategyAvailabilityListener.onAvailabilityChanged(one);

        verify(one, never()).unsubscribe(anyString(), anyInt(), any(ResponseHandler.class));
        verify(two, never()).unsubscribe(anyString(), anyInt(), any(ResponseHandler.class));
        verify(one, never()).subscribe(anyString(), anyInt(), anyInt(), any(ResponseHandler.class));
        verify(two, never()).subscribe(anyString(), anyInt(), anyInt(), any(ResponseHandler.class));
    }

    @Test
    public void whenSubscribingWithoutAvailability_thenErrorCalledOnResponseHandler() {
        CommunicationStrategy one = createCommunicationStrategy(UNAVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(UNAVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);

        strategy.subscribe("portname", 1, 0, responseHandlerMock);

        verify(responseHandlerMock).onError(eq(NOT_CONNECTED), anyString());
    }

    @Test
    public void whenSubscriptionFailedAvailabilityChangesAndAnotherStrategyStillAvailable_thenDontMoveSubscription() {
        CommunicationStrategy one = createCommunicationStrategy(AVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ResponseHandler handler = invocation.getArgument(3);
                handler.onError(NOT_CONNECTED, "");
                return null;
            }
        }).when(one).subscribe(anyString(), anyInt(), anyInt(), any(ResponseHandler.class));
        strategy.subscribe("portname", 1, 0, responseHandlerMock);
        when(one.isAvailable()).thenReturn(false);

        strategyAvailabilityListener.onAvailabilityChanged(one);

        verify(one, never()).unsubscribe(anyString(), anyInt(), any(ResponseHandler.class));
        verify(two, never()).subscribe(anyString(), anyInt(), anyInt(), any(ResponseHandler.class));
    }

    @Test
    public void whenAvailabilityChangesAndNoOtherStrategyAvailable_thenOnlyRemoveSubscription() {
        CommunicationStrategy one = createCommunicationStrategy(AVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(UNAVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        whenSubscribeThenSuccess(one);
        strategy.subscribe("portname", 1, 0, responseHandlerMock);
        when(one.isAvailable()).thenReturn(false);

        strategyAvailabilityListener.onAvailabilityChanged(one);

        verify(one).unsubscribe(eq("portname"), eq(1), any(ResponseHandler.class));
        verify(two, never()).subscribe(anyString(), anyInt(), anyInt(), any(ResponseHandler.class));
    }

    @Test
    public void whenAvailabilityChangesAndMorePreferredOtherStrategyAvailable_thenMoveSubscription() {
        CommunicationStrategy one = createCommunicationStrategy(UNAVAILABLE);
        CommunicationStrategy two = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(one, two);
        strategy.addAvailabilityListener(strategyAvailabilityListenerMock);
        whenSubscribeThenSuccess(two);
        strategy.subscribe("portname", 1, 0, responseHandlerMock);
        when(one.isAvailable()).thenReturn(true);

        strategyAvailabilityListener.onAvailabilityChanged(one);

        verify(two).unsubscribe(eq("portname"), eq(1), any(ResponseHandler.class));
        verify(one).subscribe(eq("portname"), eq(1), eq(0), any(ResponseHandler.class));
    }

    private void whenSubscribeThenSuccess(CommunicationStrategy two) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ResponseHandler handler = invocation.getArgument(3);
                handler.onSuccess("");
                return null;
            }
        }).when(two).subscribe(anyString(), anyInt(), anyInt(), any(ResponseHandler.class));
    }

    @Test
    public void whenEnablingCommunication_ThenCallsOnAllStrategies() {
        final CommunicationStrategy sub1 = createCommunicationStrategy(AVAILABLE);
        final CommunicationStrategy sub2 = createCommunicationStrategy(AVAILABLE);
        final CommunicationStrategy sub3 = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                sub1,
                sub2,
                sub3
        );

        strategy.addSubscriptionEventListener(subscriptionEventListenerMock);
        strategy.enableCommunication();

        verify(sub1).enableCommunication();
        verify(sub2).enableCommunication();
        verify(sub3).enableCommunication();
    }

    @Test
    public void whenDisablingCommunication_ThenCallsOnAllStrategies() {
        final CommunicationStrategy sub1 = createCommunicationStrategy(AVAILABLE);
        final CommunicationStrategy sub2 = createCommunicationStrategy(AVAILABLE);
        final CommunicationStrategy sub3 = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                sub1,
                sub2,
                sub3
        );

        strategy.disableCommunication();

        verify(sub1).disableCommunication();
        verify(sub2).disableCommunication();
        verify(sub3).disableCommunication();
    }

    @Test
    public void whenAddingSubscriptionListeners_thenPassedToSubStrategies() {
        final CommunicationStrategy sub1 = createCommunicationStrategy(AVAILABLE);
        final CommunicationStrategy sub2 = createCommunicationStrategy(AVAILABLE);
        final CommunicationStrategy sub3 = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                sub1,
                sub2,
                sub3
        );

        strategy.addSubscriptionEventListener(subscriptionEventListenerMock);

        verify(sub1).addSubscriptionEventListener(subscriptionEventListenerMock);
        verify(sub2).addSubscriptionEventListener(subscriptionEventListenerMock);
        verify(sub3).addSubscriptionEventListener(subscriptionEventListenerMock);
    }

    @Test
    public void whenRemovingSubscriptionListeners_thenPassedToSubStrategies() {
        final CommunicationStrategy sub1 = createCommunicationStrategy(AVAILABLE);
        final CommunicationStrategy sub2 = createCommunicationStrategy(AVAILABLE);
        final CommunicationStrategy sub3 = createCommunicationStrategy(AVAILABLE);
        final CombinedCommunicationStrategy strategy = new CombinedCommunicationStrategy(
                sub1,
                sub2,
                sub3
        );

        strategy.removeSubscriptionEventListener(subscriptionEventListenerMock);

        verify(sub1).removeSubscriptionEventListener(subscriptionEventListenerMock);
        verify(sub2).removeSubscriptionEventListener(subscriptionEventListenerMock);
        verify(sub3).removeSubscriptionEventListener(subscriptionEventListenerMock);
    }
}
