/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.communication;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.cdp.dicommclient.request.Error.NOT_CONNECTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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

    @Before
    public void setUp() {
        initMocks(this);
    }

    private CommunicationStrategy createCommunicationStrategy(boolean available) {
        CommunicationStrategy strategy = mock(CommunicationStrategy.class);
        when(strategy.isAvailable()).thenReturn(available);
        return strategy;
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

        verify(responseHandlerMock).onError(eq(NOT_CONNECTED), anyString());
    }
}