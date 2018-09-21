/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.api.listener.DcsEventListener;
import com.philips.cdp.cloudcontroller.api.listener.DcsResponseListener;
import com.philips.icpinterface.CallbackHandler;
import com.philips.icpinterface.DownloadData;
import com.philips.icpinterface.EventSubscription;
import com.philips.icpinterface.Provision;
import com.philips.icpinterface.SignOn;
import com.philips.icpinterface.configuration.Params;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
import java.nio.ByteBuffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EventSubscription.class, SignOn.class, Log.class })
public class DefaultCloudControllerTest {

    @Mock
    private DcsEventListener dcsListener;

    @Mock
    private DcsResponseListener responseListenerMock;

    @Mock
    private EventSubscription eventSubscriptionMock;

    @Mock
    private DefaultCloudController.DCSStartListener startedListener;

    @Mock
    private SignOn signOnMock;
    @Mock
    private Provision mockOldProvision;
    @Mock
    private Provision mockNewProvision;

    @Mock
    private DownloadData downloadDataMock;

    @Mock
    private KpsConfigurationInfo mockKpsConfiguration;

    @Mock
    private Context mockContext;

    private final String cppId = "valid cppId";
    private ByteBuffer downloadDataBuffer;

    private DefaultCloudController subject;

    private boolean performProvisionMock = true;

    @Before
    public void setUp() {
        initMocks(this);

        mockStatic(SignOn.class);
        when(SignOn.getInstance((CallbackHandler) any(), (Params) any())).thenReturn(signOnMock);

        mockStatic(EventSubscription.class);
        when(EventSubscription.getInstance((CallbackHandler) any(), anyInt())).thenReturn(eventSubscriptionMock);

        mockStatic(Log.class);

        when(signOnMock.init()).thenReturn(Errors.SUCCESS);

        when(mockKpsConfiguration.getAppId()).thenReturn("bogusId");
        when(mockKpsConfiguration.getAppVersion()).thenReturn(1337);

        initBufferUsedForTests();
    }

    private void createDefaultCloudController() {
        if (performProvisionMock) {
            when(mockOldProvision.executeCommand()).thenReturn(Errors.REQUEST_PENDING);
            when(mockNewProvision.executeCommand()).thenReturn(Errors.REQUEST_PENDING);
            subject = new DefaultCloudController(mockContext, mockKpsConfiguration) {
                @NonNull
                @Override
                Provision getOldProvision() {
                    return mockOldProvision;
                }

                @NonNull
                @Override
                Provision getNewProvision() {
                    return mockNewProvision;
                }
            };
        } else {
            subject = new DefaultCloudController(mockContext, mockKpsConfiguration);
        }
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

        verify(responseListenerMock).onDCSResponseReceived(eq(data), (String) any());
    }

    @Test
    public void givenCloudControllerCreated_whenProvidingNullCppIdAndActionRESPONSE_thenShouldCallResponseListener() {
        String data = "valid dcs event";
        String action = "RESPONSE";

        CloudController controller = createCloudControllerWithListeners(null, dcsListener, responseListenerMock);

        controller.notifyDCSListener(data, null, action, null);

        verify(responseListenerMock).onDCSResponseReceived(eq(data), (String) any());
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
    public void whenStartDCSIsCalledThenEventSubscriptionCommandIsExecuted() {
        CloudController cloudController = initCloudControllerAndPerformSignOn();

        cloudController.startDCSService(startedListener);

        verify(eventSubscriptionMock).executeCommand();
    }

    @Test
    public void whenSubscribeIsSuccessfulThenListenerIsNotified() {
        DefaultCloudController cloudController = initCloudControllerAndPerformSignOn();

        cloudController.startDCSService(startedListener);
        cloudController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenSubscribeIsSuccessfulThenStateIsStarted() {
        whenDCSCommandWasExecutedThenDCSStateIsStarting();

        subject.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        assertEquals(CloudController.ICPClientDCSState.STARTED, subject.getState());
    }

    @Test
    public void whenSubscribeIsUnsuccessfulThenListenerIsNotified() {
        DefaultCloudController cloudController = initCloudControllerAndPerformSignOn();

        cloudController.startDCSService(startedListener);
        cloudController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.CONNECT_TIMEDOUT, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenSubscribeIsUnsuccessfulThenStateIs() {
        DefaultCloudController cloudController = initCloudControllerAndPerformSignOn();

        cloudController.startDCSService(startedListener);
        cloudController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.CONNECT_TIMEDOUT, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenStartDCSIsCalledWhileNotSignedOnThenSignOnIsPerformed() {
        subject = createCloudControllerWithListeners(cppId, dcsListener);

        subject.onICPCallbackEventOccurred(Commands.SIGNON, Errors.SUCCESS, null);
        Provision provisionMock = mock(Provision.class);
        subject.onICPCallbackEventOccurred(Commands.KEY_PROVISION, Errors.SUCCESS, provisionMock);
        subject.onICPCallbackEventOccurred(Commands.SIGNON, Errors.AUTHENTICATION_FAILED, null);

        subject.startDCSService(startedListener);

        verify(signOnMock).executeCommand();
    }

    @Test
    public void whenSignOnIsSuccessfulThenDCSIsStarted() {
        whenStartDCSIsCalledWhileNotSignedOnThenSignOnIsPerformed();

        when(signOnMock.getSignOnStatus()).thenReturn(true);
        subject.onICPCallbackEventOccurred(Commands.SIGNON, Errors.SUCCESS, null);

        verify(eventSubscriptionMock).executeCommand();
    }

    @Test
    public void whenDCSCommandWasNotExecutedThenDCSStateIsStopped() {
        CloudController cloudController = initCloudControllerAndPerformSignOn();
        when(eventSubscriptionMock.executeCommand()).thenReturn(Errors.AUTHENTICATION_FAILED);

        cloudController.startDCSService(startedListener);

        assertEquals(CloudController.ICPClientDCSState.STOPPED, cloudController.getState());
    }

    @Test
    public void whenDCSCommandWasExecutedThenDCSStateIsStarting() {
        subject = initCloudControllerAndPerformSignOn();
        when(eventSubscriptionMock.executeCommand()).thenReturn(Errors.REQUEST_PENDING);

        subject.startDCSService(startedListener);

        assertEquals(CloudController.ICPClientDCSState.STARTING, subject.getState());
    }

    @Test
    public void whenStopDCSIsCalledWhenStopCommandIsExecuted() {
        whenSubscribeIsSuccessfulThenStateIsStarted();

        subject.stopDCSService();

        verify(eventSubscriptionMock).stopCommand();
    }

    @Test
    public void whenStopDCSIsCalledWhenStateIsStopping() {
        whenSubscribeIsSuccessfulThenStateIsStarted();

        subject.stopDCSService();

        assertEquals(CloudController.ICPClientDCSState.STOPPING, subject.getState());
    }

    @Test
    public void whenSubscribeEventWithDSCStoppedIsRecievedThenStateIsStopped() {
        whenStopDCSIsCalledWhenStateIsStopping();

        when(eventSubscriptionMock.getState()).thenReturn(EventSubscription.SUBSCRIBE_EVENTS_STOPPED);
        subject.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        assertEquals(CloudController.ICPClientDCSState.STOPPED, subject.getState());
    }

    @Test
    public void dataDownloadSuccessfullyProcessed() throws Exception {
        createDefaultCloudController();

        when(downloadDataMock.getBuffer()).thenReturn(downloadDataBuffer);

        subject.onICPCallbackEventOccurred(Commands.DOWNLOAD_DATA, Errors.SUCCESS, downloadDataMock);
    }

    @Test
    public void dataDownloadSuccessfullyProcessedWhenBufferIndexNotAtStart() throws Exception {
        createDefaultCloudController();
        downloadDataBuffer.get();

        when(downloadDataMock.getBuffer()).thenReturn(downloadDataBuffer);

        subject.onICPCallbackEventOccurred(Commands.DOWNLOAD_DATA, Errors.SUCCESS, downloadDataMock);
    }

    @Test
    public void givenSignOnWasCompleted_whenLocaleIsSet_thenLocaleIsForwardedToIcpClient() {
        createDefaultCloudController();
        subject.setNewLocale("", "");

        verify(signOnMock).setNewLocale("", "");
    }

    @Test(expected = IllegalStateException.class)
    public void givenSignOnWasNotCompleted_whenLocaleIsSet_thenIllegalStateExceptionIsThrown() {
        createDefaultCloudController();
        subject.setSignOn(null); // fake an error in the construction of SignOn

        subject.setNewLocale("", "");
    }

    /*
    Acceptance Criteria
     - given di-comm pairing relation does not exist for old provisioning, when provisioning is run, then the new strategy (fixed evidence) will be used
     - given di-comm pairing relation does exist for old provisioning, when provisioning is run, then old strategy (swapped appId and version) will remain in use
     - given evidence has changed, when provisioning is run, the new strategy will always be used
     */

    //@Test
    //public void givenDiCommPairingRelationshipDoesNotExistForOldProvisioning_whenProvisioningIsRan_thenTheNewStrategyWillBeUsed() {
    //
    //}

    @Test
    public void givenProvisioningStrategyIsStored_whenStartingProvisioning_thenStoredProvisioningStrategyShouldBeUsed() {
        fail();
    }

    @Test
    public void givenProvisioningStrategyIsNotStored_whenStartingProvisioning_thenOldProvisioningStrategyShouldBeUsed() {
        createDefaultCloudController();

        assertEquals(mockOldProvision, subject.getProvision());
    }

    @Test
    public void givenOldProvisioningStrategyIsUsed_whenPairingRelationsExist_thenOldProvisioningStrategyWillBeStored() {
        fail();
    }

    @Test
    public void givenOldProvisioningStrategyIsUsed_whenNoPairingRelationsExist_thenNewProvisioningStrategyWillBeStored() {
        fail();
    }

    @Test
    public void givenProvisioningStrategyIsNotStored_whenProvisioningIsFinished_thenProvisioningStrategyIsStored() {
        fail();
    }

    @Test
    public void givenProvisioningEvidenceExists_andProvisioningEvidenceHasChanged_whenStartingProvisioning_thenNewProvisioningStrategyWillBeStored() {
        fail();
    }

    private DefaultCloudController createCloudControllerWithListeners(String cppId, DcsEventListener dcsListener) {
        return createCloudControllerWithListeners(cppId, dcsListener, null);
    }

    private DefaultCloudController createCloudControllerWithListeners(String cppId, DcsEventListener dcsListener, DcsResponseListener responseListener) {
        createDefaultCloudController();
        subject.addDCSEventListener(cppId, dcsListener);
        if (responseListener != null) {
            subject.addDCSResponseListener(responseListener);
        }
        return subject;
    }

    private void initBufferUsedForTests() {
        final int DOWNLOAD_DATA_BUFFER_CAPACITY = 1000;
        downloadDataBuffer = ByteBuffer.allocateDirect(DOWNLOAD_DATA_BUFFER_CAPACITY);
        for (int i = 0; i < DOWNLOAD_DATA_BUFFER_CAPACITY; i++) {
            downloadDataBuffer.put((byte) 1);
        }
        downloadDataBuffer.rewind();
    }
}
