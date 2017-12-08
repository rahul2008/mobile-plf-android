/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.api.listener.DcsEventListener;
import com.philips.cdp.cloudcontroller.api.listener.DcsResponseListener;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.icpinterface.CallbackHandler;
import com.philips.icpinterface.EventSubscription;
import com.philips.icpinterface.Provision;
import com.philips.icpinterface.SignOn;
import com.philips.icpinterface.configuration.Params;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EventSubscription.class, SignOn.class, Log.class})
public class CloudControllerTest extends RobolectricTest {

    @Mock
    DcsEventListener dcsListener;

    @Mock
    DcsResponseListener responseListenerMock;

    @Mock
    EventSubscription eventSubscriptionMock;

    @Mock
    private DefaultCloudController.DCSStartListener startedListener;

    @Mock
    private SignOn signOnMock;

    @Mock
    Context contextMock;

    @Mock
    KpsConfigurationInfo kpsConfigurationInfoMock;

    private final String cppId = "valid cppId";
    private DefaultCloudController cloudController;

    @Override
    @Before
    public void setUp() throws Exception {
        initMocks(this);

        mockStatic(Log.class);

        mockStatic(EventSubscription.class);
        when(EventSubscription.getInstance(any(CallbackHandler.class), anyInt())).thenReturn(eventSubscriptionMock);

        mockStatic(SignOn.class);
        when(SignOn.getInstance(any(CallbackHandler.class), any(Params.class))).thenReturn(signOnMock);

        mockStatic(Log.class);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testNotifyDCSListenerNullData() {
        CloudController controller = createCloudControllerWithListeners("dfasfa", dcsListener);

        controller.notifyDCSListener(null, "dfasfa", "dfasfa", null);

        verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingValidDataAndActionRESPONSE_thenShouldCallResponseListener() {
        String data = "valid dcs event";
        String cppId = "valid cppId";
        String action = "RESPONSE";

        CloudController controller = createCloudControllerWithListeners(cppId, dcsListener, responseListenerMock);

        controller.notifyDCSListener(data, cppId, action, null);

        verify(responseListenerMock).onDCSResponseReceived(eq(data), anyString());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingNullCppIdAndActionRESPONSE_thenShouldCallResponseListener() {
        String data = "valid dcs event";
        String action = "RESPONSE";

        CloudController controller = createCloudControllerWithListeners(null, dcsListener, responseListenerMock);

        controller.notifyDCSListener(data, null, action, null);

        verify(responseListenerMock).onDCSResponseReceived(eq(data), anyString());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingValidDataAndActionRESPONSE_thenShouldNotCallEventListener() {
        String data = "valid dcs event";
        String cppId = "valid cppId";
        String action = "RESPONSE";

        CloudController controller = createCloudControllerWithListeners(cppId, dcsListener, responseListenerMock);

        controller.notifyDCSListener(data, cppId, action, null);

        verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingNullCppIdAndActionRESPONSE_thenShouldNotCallEventListener() {
        String data = "valid dcs event";
        String action = "RESPONSE";

        CloudController controller = createCloudControllerWithListeners(null, dcsListener, responseListenerMock);

        controller.notifyDCSListener(data, null, action, null);

        verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingValidDataAndActionCHANGE_thenShouldCallListener() {
        String data = "valid dcs event";
        String cppId = "valid cppId";
        String action = "CHANGE";

        CloudController controller = createCloudControllerWithListeners(cppId, dcsListener);

        controller.notifyDCSListener(data, cppId, action, null);

        verify(dcsListener).onDCSEventReceived(data, cppId, action);
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingNullCppIdAndActionCHANGE_thenShouldCallListener() {
        String data = "valid dcs event";
        String action = "CHANGE";

        CloudController controller = createCloudControllerWithListeners(null, dcsListener);

        controller.notifyDCSListener(data, null, action, null);

        verify(dcsListener).onDCSEventReceived(data, null, action);
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingValidDataAndActionCHANGE_thenShouldNotCallResponseListener() {
        String data = "valid dcs event";
        String cppId = "valid cppId";
        String action = "CHANGE";

        CloudController controller = createCloudControllerWithListeners(cppId, dcsListener);

        controller.notifyDCSListener(data, cppId, action, null);

        verify(responseListenerMock, never()).onDCSResponseReceived(eq(data), anyString());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingNullCppIdAndActionCHANGE_thenShouldNotCallResponseListener() {
        String data = "valid dcs event";
        String action = "CHANGE";

        CloudController controller = createCloudControllerWithListeners(null, dcsListener);

        controller.notifyDCSListener(data, null, action, null);

        verify(responseListenerMock, never()).onDCSResponseReceived(eq(data), anyString());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingValidDataAndActionUnknown_thenShouldNotCallListener() {
        String data = "valid dcs event";
        String cppId = "valid cppId";
        String action = "some action";

        CloudController controller = createCloudControllerWithListeners(cppId, dcsListener);

        controller.notifyDCSListener(data, cppId, action, null);

        verify(dcsListener, never()).onDCSEventReceived(data, cppId, action);
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingNullCppIdAndActionUnknown_thenShouldNotCallListener() {
        String data = "valid dcs event";
        String action = "some action";

        CloudController controller = createCloudControllerWithListeners(null, dcsListener);

        controller.notifyDCSListener(data, null, action, null);

        verify(dcsListener, never()).onDCSEventReceived(data, null, action);
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingValidDataAndActionUnknown_thenShouldNotCallResponseListener() {
        String data = "valid dcs event";
        String cppId = "valid cppId";
        String action = "some action";

        CloudController controller = createCloudControllerWithListeners(cppId, dcsListener);

        controller.notifyDCSListener(data, cppId, action, null);

        verify(responseListenerMock, never()).onDCSResponseReceived(eq(data), anyString());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingNullCppIdAndActionUnknown_thenShouldNotCallResponseListener() {
        String data = "valid dcs event";
        String action = "some action";

        CloudController controller = createCloudControllerWithListeners(null, dcsListener);

        controller.notifyDCSListener(data, null, action, null);

        verify(responseListenerMock, never()).onDCSResponseReceived(eq(data), anyString());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingNullCppIdAndActionUnknown_thenShouldCallLogging() {
        String data = "valid dcs event";
        String action = "some action";

        CloudController controller = createCloudControllerWithListeners(null, dcsListener);

        controller.notifyDCSListener(data, null, action, null);

        PowerMockito.verifyStatic(atLeastOnce());
        Log.e(eq("DefaultCloudController"), anyString());
    }

    @Test
    public void testNotifyDCSListenerNullAction() {
        String data = "valid dcs event";

        CloudController controller = createCloudControllerWithListeners(cppId, dcsListener);

        controller.notifyDCSListener(data, cppId, null, null);

        verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
    }

    @NonNull
    private DefaultCloudController initCloudControllerAndPerformSignOn() {
        DefaultCloudController cloudController = createCloudControllerWithListeners(cppId, dcsListener);
        cloudController.onICPCallbackEventOccurred(Commands.SIGNON, Errors.SUCCESS, null);

        when(signOnMock.getSignOnStatus()).thenReturn(true);

        return cloudController;
    }

    @Test
    public void whenStartDCSIsCalledThenEventSubscriptionCommandIsExecuted() throws Exception {
        CloudController cloudController = initCloudControllerAndPerformSignOn();

        cloudController.startDCSService(startedListener);

        verify(eventSubscriptionMock).executeCommand();
    }

    @Test
    public void whenSubscribeIsSuccessfulThenListenerIsNotified() throws Exception {
        DefaultCloudController cloudController = initCloudControllerAndPerformSignOn();

        cloudController.startDCSService(startedListener);
        cloudController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenSubscribeIsSuccessfulThenStateIsStarted() throws Exception {
        whenDCSCommandWasExecutedThenDCSStateIsStarting();

        cloudController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        assertEquals(CloudController.ICPClientDCSState.STARTED, cloudController.getState());
    }

    @Test
    public void whenSubscribeIsUnsuccessfulThenListenerIsNotified() throws Exception {
        DefaultCloudController cloudController = initCloudControllerAndPerformSignOn();

        cloudController.startDCSService(startedListener);
        cloudController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.CONNECT_TIMEDOUT, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenSubscribeIsUnsuccessfulThenStateIs() throws Exception {
        DefaultCloudController cloudController = initCloudControllerAndPerformSignOn();

        cloudController.startDCSService(startedListener);
        cloudController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.CONNECT_TIMEDOUT, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenStartDCSIsCalledWhileNotSignedOnThenSignOnIsPerformed() throws Exception {
        cloudController = createCloudControllerWithListeners(cppId, dcsListener);

        cloudController.onICPCallbackEventOccurred(Commands.SIGNON, Errors.SUCCESS, null);
        Provision provisionMock = mock(Provision.class);
        cloudController.onICPCallbackEventOccurred(Commands.KEY_PROVISION, Errors.SUCCESS, provisionMock);
        cloudController.onICPCallbackEventOccurred(Commands.SIGNON, Errors.AUTHENTICATION_FAILED, null);

        cloudController.startDCSService(startedListener);

        verify(signOnMock).executeCommand();
    }

    @Test
    public void whenSignOnIsSuccessfulThenDCSIsStarted() throws Exception {
        whenStartDCSIsCalledWhileNotSignedOnThenSignOnIsPerformed();

        when(signOnMock.getSignOnStatus()).thenReturn(true);
        cloudController.onICPCallbackEventOccurred(Commands.SIGNON, Errors.SUCCESS, null);

        verify(eventSubscriptionMock).executeCommand();
    }

    @Test
    public void whenDCSCommandWasNotExecutedThenDCSStateIsStopped() throws Exception {
        CloudController cloudController = initCloudControllerAndPerformSignOn();
        when(eventSubscriptionMock.executeCommand()).thenReturn(Errors.AUTHENTICATION_FAILED);

        cloudController.startDCSService(startedListener);

        assertEquals(CloudController.ICPClientDCSState.STOPPED, cloudController.getState());
    }

    @Test
    public void whenDCSCommandWasExecutedThenDCSStateIsStarting() throws Exception {
        cloudController = initCloudControllerAndPerformSignOn();
        when(eventSubscriptionMock.executeCommand()).thenReturn(Errors.REQUEST_PENDING);

        cloudController.startDCSService(startedListener);

        assertEquals(CloudController.ICPClientDCSState.STARTING, cloudController.getState());
    }

    @Test
    public void whenStopDCSIsCalledWhenStopCommandIsExecuted() throws Exception {
        whenSubscribeIsSuccessfulThenStateIsStarted();

        cloudController.stopDCSService();

        verify(eventSubscriptionMock).stopCommand();
    }

    @Test
    public void whenStopDCSIsCalledWhenStateIsStopping() throws Exception {
        whenSubscribeIsSuccessfulThenStateIsStarted();

        cloudController.stopDCSService();

        assertEquals(CloudController.ICPClientDCSState.STOPPING, cloudController.getState());
    }

    @Test
    public void whenSubscribeEventWithDSCStoppedIsRecievedThenStateIsStopped() throws Exception {
        whenStopDCSIsCalledWhenStateIsStopping();

        when(eventSubscriptionMock.getState()).thenReturn(EventSubscription.SUBSCRIBE_EVENTS_STOPPED);
        cloudController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        assertEquals(CloudController.ICPClientDCSState.STOPPED, cloudController.getState());
    }

    private DefaultCloudController createCloudControllerWithListeners(String cppId, DcsEventListener dcsListener) {
        return createCloudControllerWithListeners(cppId, dcsListener, null);
    }

    private DefaultCloudController createCloudControllerWithListeners(String cppId, DcsEventListener dcsListener, DcsResponseListener responseListener) {
        DefaultCloudController controller = new DefaultCloudController();
        controller.addDCSEventListener(cppId, dcsListener);
        if(responseListener != null) {
            controller.addDCSResponseListener(responseListener);
        }
        return controller;
    }
}