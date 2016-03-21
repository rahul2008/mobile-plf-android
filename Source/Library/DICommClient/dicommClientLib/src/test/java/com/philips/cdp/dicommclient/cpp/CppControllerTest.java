/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

import com.philips.cdp.dicommclient.cpp.listener.DcsEventListener;
import com.philips.cdp.dicommclient.discovery.CppDiscoverEventListener;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CppControllerTest extends RobolectricTest {

    @Test
    public void testNotifyDCSListenerNullData() {
        DcsEventListener dcsListener = mock(DcsEventListener.class);
        CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
        CppController controller = createCppControllerWithListeners("dfasfa", discoveryListener, dcsListener);

        controller.notifyDCSListener(null, "dfasfa", "dfasfa", null);

        verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
    }

    @Test
    public void testNotifyDCSListenerAllValidData() {
        String data = "valid dcs event";
        String cppId = "valid cppId";
        String action = "valid action";

        DcsEventListener dcsListener = mock(DcsEventListener.class);
        CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
        CppController controller = createCppControllerWithListeners(cppId, discoveryListener, dcsListener);

        controller.notifyDCSListener(data, cppId, action, null);

        verify(dcsListener).onDCSEventReceived(data, cppId, action);
    }

    @Test
    public void testNotifyDCSListenerNullCppId() {
        String data = "valid dcs event";
        String action = "valid action";

        DcsEventListener dcsListener = mock(DcsEventListener.class);
        CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
        CppController controller = createCppControllerWithListeners(null, discoveryListener, dcsListener);

        controller.notifyDCSListener(data, null, action, null);

        verify(dcsListener).onDCSEventReceived(data, null, action);
    }

    @Test
    public void testNotifyDCSListenerNullAction() {
        String data = "valid dcs event";
        String cppId = "valid cppId";

        DcsEventListener dcsListener = mock(DcsEventListener.class);
        CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
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
}
