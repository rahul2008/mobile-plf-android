/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.cpp.listener.DcsEventListener;
import com.philips.cdp.dicommclient.discovery.CppDiscoverEventListener;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.DICommLog;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EventSubscription.class, SignOn.class})
public class CppControllerTest extends RobolectricTest {

    @Mock
    DcsEventListener dcsListener;

    @Mock
    CppDiscoverEventListener discoveryListener;

    @Mock
    EventSubscription eventSubscriptionMock;

    @Mock
    private CppController.DCSStartListener startedListener;

    @Mock
    private SignOn signOnMock;

    @Mock
    Context contextMock;

    @Mock
    KpsConfigurationInfo kpsConfigurationInfoMock;

    @Mock
    PackageManager packageManagerMock;

    private final String cppId = "valid cppId";
    private CppController cppController;

    @Override
    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();

        mockStatic(EventSubscription.class);
        when(EventSubscription.getInstance(any(CallbackHandler.class), anyInt())).thenReturn(eventSubscriptionMock);

        mockStatic(SignOn.class);
        when(SignOn.getInstance(any(CallbackHandler.class), any(Params.class))).thenReturn(signOnMock);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testNotifyDCSListenerNullData() {
        CppController controller = createCppControllerWithListeners("dfasfa", dcsListener);

        controller.notifyDCSListener(null, "dfasfa", "dfasfa", null);

        verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
    }

    @Test
    public void testNotifyDCSListenerAllValidData() {
        String data = "valid dcs event";
        String cppId = "valid cppId";
        String action = "valid action";

        CppController controller = createCppControllerWithListeners(cppId, dcsListener);

        controller.notifyDCSListener(data, cppId, action, null);

        verify(dcsListener).onDCSEventReceived(data, cppId, action);
    }

    @Test
    public void testNotifyDCSListenerNullCppId() {
        String data = "valid dcs event";
        String action = "valid action";

        CppController controller = createCppControllerWithListeners(null, dcsListener);

        controller.notifyDCSListener(data, null, action, null);

        verify(dcsListener).onDCSEventReceived(data, null, action);
    }

    @Test
    public void testNotifyDCSListenerNullAction() {
        String data = "valid dcs event";

        CppController controller = createCppControllerWithListeners(cppId, dcsListener);

        controller.notifyDCSListener(data, cppId, null, null);

        verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
    }

    private CppController createCppControllerWithListeners(String cppId,
                                                           DcsEventListener dcsListener) {
        CppController controller = CppController.getCppControllerForTesting();
        controller.addDCSEventListener(cppId, dcsListener);
        return controller;
    }

    @NonNull
    private CppController initCPPControllerAndPerformSignOn() {
        CppController cppController = createCppControllerWithListeners(cppId, dcsListener);
        cppController.onICPCallbackEventOccurred(Commands.SIGNON, Errors.SUCCESS, null);

        when(signOnMock.getSignOnStatus()).thenReturn(true);

        return cppController;
    }

    @Test
    public void whenStartDCSIsCalledThenEventSubscriptionCommandIsExecuted() throws Exception {
        CppController cppController = initCPPControllerAndPerformSignOn();

        cppController.startDCSService(startedListener);

        verify(eventSubscriptionMock).executeCommand();
    }

    @Test
    public void whenSubscribeIsSuccessfulThenListenerIsNotified() throws Exception {
        CppController cppController = initCPPControllerAndPerformSignOn();

        cppController.startDCSService(startedListener);
        cppController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenSubscribeIsSuccessfulThenStateIsStarted() throws Exception {
        whenDCSCommandWasExecutedThenDCSStateIsStarting();

        cppController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        assertEquals(CppController.ICP_CLIENT_DCS_STATE.STARTED, cppController.getState());
    }

    @Test
    public void whenSubscribeIsUnsuccessfulThenListenerIsNotified() throws Exception {
        CppController cppController = initCPPControllerAndPerformSignOn();

        cppController.startDCSService(startedListener);
        cppController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.CONNECT_TIMEDOUT, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenSubscribeIsUnsuccessfulThenStateIs() throws Exception {
        CppController cppController = initCPPControllerAndPerformSignOn();

        cppController.startDCSService(startedListener);
        cppController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.CONNECT_TIMEDOUT, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenStartDCSIsCalledWhileNotSignedOnThenSignOnIsPerformed() throws Exception {
        cppController = createCppControllerWithListeners(cppId, dcsListener);

        cppController.onICPCallbackEventOccurred(Commands.SIGNON, Errors.SUCCESS, null);
        Provision provisionMock = mock(Provision.class);
        cppController.onICPCallbackEventOccurred(Commands.KEY_PROVISION, Errors.SUCCESS, provisionMock);
        cppController.onICPCallbackEventOccurred(Commands.SIGNON, Errors.AUTHENTICATION_FAILED, null);

        cppController.startDCSService(startedListener);

        verify(signOnMock).executeCommand();
    }

    @Test
    public void whenSignOnIsSuccessfulThenDCSIsStarted() throws Exception {
        whenStartDCSIsCalledWhileNotSignedOnThenSignOnIsPerformed();

        when(signOnMock.getSignOnStatus()).thenReturn(true);
        cppController.onICPCallbackEventOccurred(Commands.SIGNON, Errors.SUCCESS, null);

        verify(eventSubscriptionMock).executeCommand();
    }

    @Test
    public void whenDCSCommandWasNotExecutedThenDCSStateIsStopped() throws Exception {
        CppController cppController = initCPPControllerAndPerformSignOn();
        when(eventSubscriptionMock.executeCommand()).thenReturn(Errors.AUTHENTICATION_FAILED);

        cppController.startDCSService(startedListener);

        assertEquals(CppController.ICP_CLIENT_DCS_STATE.STOPPED, cppController.getState());
    }

    @Test
    public void whenDCSCommandWasExecutedThenDCSStateIsStarting() throws Exception {
        cppController = initCPPControllerAndPerformSignOn();
        when(eventSubscriptionMock.executeCommand()).thenReturn(Errors.REQUEST_PENDING);

        cppController.startDCSService(startedListener);

        assertEquals(CppController.ICP_CLIENT_DCS_STATE.STARTING, cppController.getState());
    }

    @Test
    public void whenStopDCSIsCalledWhenStopCommandIsExecuted() throws Exception {
        whenSubscribeIsSuccessfulThenStateIsStarted();

        cppController.stopDCSService();

        verify(eventSubscriptionMock).stopCommand();
    }

    @Test
    public void whenStopDCSIsCalledWhenStateIsStopping() throws Exception {
        whenSubscribeIsSuccessfulThenStateIsStarted();

        cppController.stopDCSService();

        assertEquals(CppController.ICP_CLIENT_DCS_STATE.STOPPING, cppController.getState());
    }

    @Test
    public void whenSubscribeEventWithDSCStoppedIsRecievedThenStateIsStopped() throws Exception {
        whenStopDCSIsCalledWhenStateIsStopping();

        when(eventSubscriptionMock.getState()).thenReturn(EventSubscription.SUBSCRIBE_EVENTS_STOPPED);
        cppController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        assertEquals(CppController.ICP_CLIENT_DCS_STATE.STOPPED, cppController.getState());
    }

    @Test
    public void whenStartingKeyProvisioningThenRetriveRelationshipId() throws Exception {
        when(contextMock.getPackageManager()).thenReturn(packageManagerMock);
        when(contextMock.getPackageName()).thenReturn("com.philips.cdp.dicommclient.cpp.testing");
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.versionCode = 1;
        when(packageManagerMock.getPackageInfo("com.philips.cdp.dicommclient.cpp.testing", 0)).thenReturn(packageInfo);

        cppController = new CppController(contextMock, kpsConfigurationInfoMock);

        verify(kpsConfigurationInfoMock).getRelationshipId();
    }
}
