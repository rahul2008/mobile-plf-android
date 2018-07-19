/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.bluelib.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.cdp2.commlib.ble.request.BleGetRequest;
import com.philips.cdp2.commlib.ble.request.BlePutRequest;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.util.Availability;
import com.philips.cdp2.commlib.core.util.VerboseRunnable;
import com.philips.cdp2.commlib.util.VerboseExecutor;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.utility.Utilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IS_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MAC_ADDRESS;
import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static com.philips.pins.shinelib.utility.Utilities.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

public class BleCommunicationStrategyTest {

    private static final String PORT_NAME = "thePort";
    private static final String MAC_ADDRESS = "00:11:22:33:44:55";
    private static final int PRODUCT_ID = 0;
    private static final int SUBSCRIPTION_TTL = 5000;

    @Mock
    private VerboseExecutor executorMock;

    @Mock
    private SHNDevice deviceMock;

    @Mock
    private ResponseHandler responseHandlerMock;

    @Mock
    private Handler callbackHandlerMock;

    @Mock
    private SubscriptionEventListener subscriptionListenerMock;

    @Mock
    private Availability.AvailabilityListener<CommunicationStrategy> availabilityListenerMock;

    @Mock
    private SHNCentral centralMock;

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private PropertyChangeEvent propertyChangeEventMock;

    private List<PollingSubscription> pollingSubscriptionMocks = new ArrayList<>();

    private int lastSubscriptionTtl;

    private PortParameters lastPortParameters;

    private ResponseHandler lastResponseHandler;

    private BleCommunicationStrategy strategy;

    private SHNCentral.SHNCentralListener shnCentralListener;

    private PropertyChangeListener propertyChangeListener;


    @Before
    public void setUp() {
        ArgumentCaptor<SHNCentral.SHNCentralListener> shnCentralListenerArgumentCaptor = ArgumentCaptor.forClass(SHNCentral.SHNCentralListener.class);
        ArgumentCaptor<PropertyChangeListener> propertyChangeListenerArgumentCaptor = ArgumentCaptor.forClass(PropertyChangeListener.class);

        initMocks(this);
        disableLogging();

        when(networkNodeMock.getMacAddress()).thenReturn(MAC_ADDRESS);
        when(centralMock.createSHNDeviceForAddressAndDefinition(eq(MAC_ADDRESS), any(ReferenceNodeDeviceDefinitionInfo.class))).thenReturn(deviceMock);
        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(true);
        when(centralMock.isValidMacAddress(MAC_ADDRESS)).thenReturn(true);

        strategy = new BleCommunicationStrategy(centralMock, networkNodeMock, callbackHandlerMock, 2000, executorMock) {
            @NonNull
            @Override
            protected PollingSubscription createPollingSubscription(final int subscriptionTtl, final PortParameters portParameters, final ResponseHandler responseHandler) {
                lastSubscriptionTtl = subscriptionTtl;
                lastPortParameters = portParameters;
                lastResponseHandler = responseHandler;

                final PollingSubscription mock = mock(PollingSubscription.class);
                pollingSubscriptionMocks.add(mock);
                return mock;
            }
        };

        verify(centralMock).registerShnCentralListener(shnCentralListenerArgumentCaptor.capture());
        shnCentralListener = shnCentralListenerArgumentCaptor.getValue();

        verify(networkNodeMock).addPropertyChangeListener(propertyChangeListenerArgumentCaptor.capture());
        propertyChangeListener = propertyChangeListenerArgumentCaptor.getValue();

        strategy.addSubscriptionEventListener(subscriptionListenerMock);
        strategy.addAvailabilityListener(availabilityListenerMock);
        //noinspection unchecked
        reset(availabilityListenerMock);
    }

    @Test
    public void whenGetPropertiesIsCalled_thenAGetRequestIsScheduledForExecution() throws Exception {

        strategy.getProperties(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        ArgumentCaptor<VerboseRunnable> captor = ArgumentCaptor.forClass(VerboseRunnable.class);
        verify(executorMock).execute(captor.capture());
        BleGetRequest request = (BleGetRequest) captor.getValue().getWrappedRunnable();
        assertThat(request.getPortName()).isEqualTo(PORT_NAME);
        assertThat(request.getProductId()).isEqualTo(Integer.toString(PRODUCT_ID));
        assertThat(request.getBleDevice()).isEqualTo(deviceMock);
    }

    @Test
    public void givenCommunicationIsNotAvailable_whenGetPropertiesIsCalled_thenAnErrorIsReported() throws Exception {
        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(false);
        shnCentralListener.onStateUpdated(centralMock);

        strategy.getProperties(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onError(eq(Error.CANNOT_CONNECT), anyString());
    }

    @Test
    public void whenPutPropertiesIsCalled_thenAPutRequestIsScheduledForExecution() throws Exception {

        Map<String, Object> dataMap = new HashMap<>();
        strategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        ArgumentCaptor<VerboseRunnable> captor = ArgumentCaptor.forClass(VerboseRunnable.class);
        verify(executorMock).execute(captor.capture());
        BlePutRequest request = (BlePutRequest) captor.getValue().getWrappedRunnable();
        assertThat(request.getPortName()).isEqualTo(PORT_NAME);
        assertThat(request.getProductId()).isEqualTo(Integer.toString(PRODUCT_ID));
        assertThat(request.getBleDevice()).isEqualTo(deviceMock);
    }

    @Test
    public void givenCommunicationIsNotAvailable_whenPutPropertiesIsCalled_thenAnErrorIsReported() throws Exception {
        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(false);
        shnCentralListener.onStateUpdated(centralMock);

        Map<String, Object> dataMap = new HashMap<>();
        strategy.putProperties(dataMap, PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onError(eq(Error.CANNOT_CONNECT), anyString());
    }

    @Test
    public void testSubscribe() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, 5000, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenSubscribedWhenSubscribingAgainThenSuccessReturned() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void testUnsubscribe() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));
        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void whenSubscribing_thenAPollingSubscriptionIsCreated() {

        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        assertThat(lastSubscriptionTtl).isEqualTo(SUBSCRIPTION_TTL);
        assertThat(lastPortParameters).isEqualTo(new PortParameters(PORT_NAME, PRODUCT_ID));
        assertThat(lastResponseHandler).isNotNull();
    }

    @Test
    public void givenSubscribed_whenPollerGetsResponse_thenSubscriptionEventIsReported() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        final String SUBSCRIPTION_EVENT_DATA = "Rainer";
        lastResponseHandler.onSuccess(SUBSCRIPTION_EVENT_DATA);

        verify(subscriptionListenerMock).onSubscriptionEventReceived(PORT_NAME, SUBSCRIPTION_EVENT_DATA);
    }

    @Test
    public void whenSubscribing_thenSuccessIsReported() {

        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenSubscribed_whenUnsubscribing_thenSuccessIsReported() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenSubscribed_whenUnsubscribing_thenPollingIsStopped() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(pollingSubscriptionMocks.get(0)).cancel();
    }

    @Test
    public void givenSubscribed_whenSubscribingAgainForSamePort_thenOldPollerIsCancelledAndNewPollerCreated() {
        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        strategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, mock(ResponseHandler.class));

        verify(pollingSubscriptionMocks.get(0)).cancel();
        assertThat(pollingSubscriptionMocks.size()).isEqualTo(2);
    }

    @Test
    public void givenNoSubscriptionWhenUnsubscribingThenSuccessReturned() {
        strategy.unsubscribe(PORT_NAME, PRODUCT_ID, responseHandlerMock);

        verify(responseHandlerMock).onSuccess(anyString());
    }

    @Test
    public void givenStrategyIsAvailable_whenCommunicationIsEnabled_thenDeviceIsConnectedAndDisconnectAfterRequestIsFalse() {
        //taken care of by setup

        strategy.enableCommunication();

        assertThat(strategy.disconnectAfterRequest.get()).isFalse();
        verify(deviceMock).connect(anyLong());
    }

    @Test
    public void givenCommunicationIsEnabledAndExecutorIsIdle_whenCommunicationIsDisabled_thenDisconnectDeviceAndDisconnectAfterRequestIsSet() {
        when(executorMock.isIdle()).thenReturn(true);
        strategy.enableCommunication();

        strategy.disableCommunication();

        assertThat(strategy.disconnectAfterRequest.get()).isTrue();
        verify(deviceMock).disconnect();
    }

    @Test
    public void givenCommunicationIsEnabledAndExecutorIsNotIdle_whenCommunicationIsDisabled_thenNotDisconnectDeviceButDisconnectAfterRequestIsSet() {
        strategy.enableCommunication();
        when(executorMock.isIdle()).thenReturn(false);

        strategy.disableCommunication();

        assertThat(strategy.disconnectAfterRequest.get()).isTrue();
        verify(deviceMock, never()).disconnect();
    }

    @Test
    public void givenBleIsOnAndMacAddressIsNotAvailable_whenStrategyIsCreated_thenItIsNotAvailable() {
        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(true);
        when(isValidMacAddress(MAC_ADDRESS)).thenReturn(false);

        CommunicationStrategy strategy = new BleCommunicationStrategy(centralMock, networkNodeMock, callbackHandlerMock, 2000, executorMock);

        assertThat(strategy.isAvailable()).isFalse();
    }

    @Test
    public void givenBleIsOnAndMacAddressIsAvailable_whenStrategyIsCreated_thenItIsAvailable() {
        //taken care of by setup

        CommunicationStrategy strategy = new BleCommunicationStrategy(centralMock, networkNodeMock, callbackHandlerMock, 2000, executorMock);

        assertThat(strategy.isAvailable()).isTrue();
    }

    @Test
    public void givenBleIsOffAndMacAddressIsAvailable_whenStrategyIsCreated_thenItIsNotAvailable() {
        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(false);
        when(isValidMacAddress(MAC_ADDRESS)).thenReturn(true);

        CommunicationStrategy strategy = new BleCommunicationStrategy(centralMock, networkNodeMock, callbackHandlerMock, 2000, executorMock);

        assertThat(strategy.isAvailable()).isFalse();
    }

    @Test
    public void givenCommunicationIsAvailable_whenBleIsSwitchedOff_thenListenerIsNotifiedAboutChange() {
        shnCentralListener.onStateUpdated(centralMock);

        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(false);
        shnCentralListener.onStateUpdated(centralMock);

        verify(availabilityListenerMock).onAvailabilityChanged(strategy);
    }

    @Test
    public void givenBleIsOffAndNetworkNodeHasNoMac_whenBleIsTurnedOn_thenListenerIsNotNotified() {
        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(false);
        when(networkNodeMock.getMacAddress()).thenReturn(null);
        shnCentralListener.onStateUpdated(centralMock);
        //noinspection unchecked
        reset(availabilityListenerMock);

        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(true);
        shnCentralListener.onStateUpdated(centralMock);

        verifyZeroInteractions(availabilityListenerMock);
    }

    @Test
    public void givenBleIsOffAndNetworkNodeHasMac_whenBleIsTurnedOn_thenListenerIsNotified() {
        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(false);
        shnCentralListener.onStateUpdated(centralMock);
        //noinspection unchecked
        reset(availabilityListenerMock);

        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(true);
        shnCentralListener.onStateUpdated(centralMock);

        verify(availabilityListenerMock).onAvailabilityChanged(strategy);
        assertThat(strategy.isAvailable()).isTrue();
    }

    @Test
    public void givenBleIsOnAndNetworkNodeHasNoMac_whenMacBecomesKnown_thenListenerIsNotified() {
        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(true);
        when(isValidMacAddress(MAC_ADDRESS)).thenReturn(false);
        shnCentralListener.onStateUpdated(centralMock);
        //noinspection unchecked
        reset(availabilityListenerMock);

        when(isValidMacAddress(MAC_ADDRESS)).thenReturn(true);
        when(propertyChangeEventMock.getPropertyName()).thenReturn(KEY_MAC_ADDRESS);
        propertyChangeListener.propertyChange(propertyChangeEventMock);

        verify(availabilityListenerMock).onAvailabilityChanged(strategy);
        assertThat(strategy.isAvailable()).isTrue();
    }

    @Test
    public void givenBleIsOnAndNetworkNodeHasNoMac_whenOtherNetworkNodePropertyChanges_thenListenerIsNotNotified() {
        when(centralMock.isBluetoothAdapterEnabled()).thenReturn(true);
        when(networkNodeMock.getMacAddress()).thenReturn(null);
        shnCentralListener.onStateUpdated(centralMock);
        //noinspection unchecked
        reset(availabilityListenerMock);

        when(propertyChangeEventMock.getPropertyName()).thenReturn(KEY_IS_PAIRED);
        propertyChangeListener.propertyChange(propertyChangeEventMock);

        verifyZeroInteractions(availabilityListenerMock);
    }
}
