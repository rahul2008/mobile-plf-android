package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.CppDiscoverEventListener;
import com.philips.cl.di.dev.pa.cpp.DCSEventListener;
import com.philips.cl.di.dicomm.util.MockitoTestCase;

public class CppControllerTest extends MockitoTestCase {

	public void testNotifyDCSListenerNullData() {
		DCSEventListener dcsListener = mock(DCSEventListener.class);		
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners("dfasfa",discoveryListener, dcsListener);
		
		controller.notifyDCSListener(null, "dfasfa", "dfasfa",null);
		
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}
	
	public void testNotifyDCSListenerAllValidData() {
		String data = "valid dcs event";
		String cppId = "valid cppId";
		String action = "valid action";

		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners(cppId,discoveryListener, dcsListener);

		controller.notifyDCSListener(data, cppId, action,null);

		verify(dcsListener).onDCSEventReceived(data, cppId, action);
	}

	public void testNotifyDCSListenerNullCppId() {
		String data = "valid dcs event";
		String action = "valid action";

		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners(null,discoveryListener, dcsListener);
		
		controller.notifyDCSListener(data, null, action,null);
		
		verify(dcsListener).onDCSEventReceived(data, null, action);
	}
	
	public void testNotifyDCSListenerNullAction() {
		String data = "valid dcs event";
		String cppId = "valid cppId";

		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners(cppId,discoveryListener, dcsListener);


		controller.notifyDCSListener(data, cppId, null,null);

		verify(dcsListener,never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	private CPPController createCppControllerWithListeners(String cppId,
			CppDiscoverEventListener discoveryListener,
			DCSEventListener dcsListener) {
		CPPController controller = CPPController.getCppControllerForTesting();
		controller.addDCSEventListener(cppId, dcsListener);
		return controller;
	}


}
