/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.cpp.listener.DcsEventListener;
import com.philips.cdp.dicommclient.discovery.CppDiscoverEventListener;
import com.philips.cdp.dicommclient.testutil.MockitoTestCase;

public class CppControllerTest extends MockitoTestCase {

	public void testNotifyDCSListenerNullData() {
		DcsEventListener dcsListener = mock(DcsEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CppController controller = createCppControllerWithListeners("dfasfa",discoveryListener, dcsListener);

		controller.notifyDCSListener(null, "dfasfa", "dfasfa",null);

		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	public void testNotifyDCSListenerAllValidData() {
		String data = "valid dcs event";
		String cppId = "valid cppId";
		String action = "valid action";

		DcsEventListener dcsListener = mock(DcsEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CppController controller = createCppControllerWithListeners(cppId,discoveryListener, dcsListener);

		controller.notifyDCSListener(data, cppId, action,null);

		verify(dcsListener).onDCSEventReceived(data, cppId, action);
	}

	public void testNotifyDCSListenerNullCppId() {
		String data = "valid dcs event";
		String action = "valid action";

		DcsEventListener dcsListener = mock(DcsEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CppController controller = createCppControllerWithListeners(null,discoveryListener, dcsListener);

		controller.notifyDCSListener(data, null, action,null);

		verify(dcsListener).onDCSEventReceived(data, null, action);
	}

	public void testNotifyDCSListenerNullAction() {
		String data = "valid dcs event";
		String cppId = "valid cppId";

		DcsEventListener dcsListener = mock(DcsEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CppController controller = createCppControllerWithListeners(cppId,discoveryListener, dcsListener);


		controller.notifyDCSListener(data, cppId, null,null);

		verify(dcsListener,never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	private CppController createCppControllerWithListeners(String cppId,
			CppDiscoverEventListener discoveryListener,
			DcsEventListener dcsListener) {
		CppController controller = CppController.getCppControllerForTesting();
		controller.addDCSEventListener(cppId, dcsListener);
		return controller;
	}


}
