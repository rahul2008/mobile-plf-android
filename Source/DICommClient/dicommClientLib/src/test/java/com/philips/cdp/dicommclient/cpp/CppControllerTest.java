/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.cpp.listener.DcsEventListener;
import com.philips.cdp.dicommclient.discovery.CppDiscoverEventListener;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.icpinterface.CallbackHandler;
import com.philips.icpinterface.EventSubscription;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
@PrepareForTest({EventSubscription.class})
public class CppControllerTest extends RobolectricTest {

    @Mock
    DcsEventListener dcsListener;

    @Mock
    CppDiscoverEventListener discoveryListener;

    @Mock
    EventSubscription eventSubscriptionMock;

    @Mock
    private CppController.DCSStartListener startedListener;

    private final String cppId = "valid cppId";

    @Override
    @Before
    public void setUp() throws Exception {
        initMocks(this);

        DICommLog.disableLogging();

        mockStatic(EventSubscription.class);
        when(EventSubscription.getInstance(any(CallbackHandler.class), anyInt())).thenReturn(eventSubscriptionMock);
    }

    @Test
    public void testNotifyDCSListenerNullData() {
        CppController controller = createCppControllerWithListeners("dfasfa", discoveryListener, dcsListener);

        controller.notifyDCSListener(null, "dfasfa", "dfasfa", null);

        verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
    }

    @Test
    public void testNotifyDCSListenerAllValidData() {
        String data = "valid dcs event";
        String cppId = "valid cppId";
        String action = "valid action";

        CppController controller = createCppControllerWithListeners(cppId, discoveryListener, dcsListener);

        controller.notifyDCSListener(data, cppId, action, null);

        verify(dcsListener).onDCSEventReceived(data, cppId, action);
    }

    @Test
    public void testNotifyDCSListenerNullCppId() {
        String data = "valid dcs event";
        String action = "valid action";

        CppController controller = createCppControllerWithListeners(null, discoveryListener, dcsListener);

        controller.notifyDCSListener(data, null, action, null);

        verify(dcsListener).onDCSEventReceived(data, null, action);
    }

    @Test
    public void testNotifyDCSListenerNullAction() {
        String data = "valid dcs event";

        CppController controller = createCppControllerWithListeners(cppId, discoveryListener, dcsListener);

        controller.notifyDCSListener(data, cppId, null, null);

        verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
    }

    private CppController createCppControllerWithListeners(String cppId,
                                                           CppDiscoverEventListener discoveryListener,
                                                           DcsEventListener dcsListener) {
        CppController controller = CppController.getCppControllerForTesting();
        controller.addDCSEventListener(cppId, dcsListener);
        return controller;
    }

    @NonNull
    private CppController initCPPControllerAndPerformSignOn() {
        CppController cppController = createCppControllerWithListeners(cppId, discoveryListener, dcsListener);
        cppController.onICPCallbackEventOccurred(Commands.SIGNON, Errors.SUCCESS, null);
        return cppController;
    }

    @Test
    public void whenStartDCSIsCalledThenEventSubscriptionCommandIsExecuted() throws Exception {
        CppController cppController = initCPPControllerAndPerformSignOn();

        cppController.startDCSService(startedListener);

        verify(eventSubscriptionMock).executeCommand();
    }

    @Test
    public void whenSubscribeIsSuccessfulThenLockIsNotified() throws Exception {
        CppController cppController = initCPPControllerAndPerformSignOn();

        cppController.startDCSService(startedListener);
        cppController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.SUCCESS, null);

        verify(startedListener).onResponseReceived();
    }

    @Test
    public void whenSubscribeIsUnsuccessfulThenLockIsNotified() throws Exception {
        CppController cppController = initCPPControllerAndPerformSignOn();

        cppController.startDCSService(startedListener);
        cppController.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, Errors.CONNECT_TIMEDOUT, null);

        verify(startedListener).onResponseReceived();
    }
}
