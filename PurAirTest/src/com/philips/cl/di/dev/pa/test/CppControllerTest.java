package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.CppDiscoverEventListener;
import com.philips.cl.di.dev.pa.cpp.DCSEventListener;
import com.philips.cl.di.dicomm.util.MockitoTestCase;

public class CppControllerTest extends MockitoTestCase {

	private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";

	public void testNotifyDCSListenerNullData() {
		DCSEventListener dcsListener = mock(DCSEventListener.class);		
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners("dfasfa",discoveryListener, dcsListener);
		
		controller.notifyDCSListener(null, "dfasfa", "dfasfa",null);
		
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
		verify(discoveryListener, never()).onDiscoverEventReceived(anyString(),anyBoolean());
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
		verify(discoveryListener,never()).onDiscoverEventReceived(anyString(),anyBoolean());
	}

	public void testNotifyDCSListenerNullCppId() {
		String data = "valid dcs event";
		String action = "valid action";

		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners(null,discoveryListener, dcsListener);
		
		controller.notifyDCSListener(data, null, action,null);
		
		verify(dcsListener).onDCSEventReceived(data, null, action);
		verify(discoveryListener,never()).onDiscoverEventReceived(anyString(),anyBoolean());
	}
	
	public void testNotifyDCSListenerNullAction() {
		String data = "valid dcs event";
		String cppId = "valid cppId";

		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners(cppId,discoveryListener, dcsListener);


		controller.notifyDCSListener(data, cppId, null,null);

		verify(dcsListener,never()).onDCSEventReceived(anyString(), anyString(), anyString());
		verify(discoveryListener,never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testDCSEventReceivedDiscover() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CPPController controller = createCppControllerWithListeners(APPLIANCE_CPPID,discoveryListener, dcsListener);

		controller.notifyDCSListener(data, APPLIANCE_CPPID, "CHANGE",null);

		verify(discoveryListener).onDiscoverEventReceived(data, false);
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}
	
	public void testDCSEventReceivedDiscoverActionEmpty() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CPPController controller = createCppControllerWithListeners(APPLIANCE_CPPID,discoveryListener, dcsListener);

		controller.notifyDCSListener(data, APPLIANCE_CPPID, "",null);

		verify(discoveryListener).onDiscoverEventReceived(data, false);
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	public void testDCSEventReceivedDiscoverActionNull() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CPPController controller = createCppControllerWithListeners(APPLIANCE_CPPID,discoveryListener, dcsListener);

		controller.notifyDCSListener(data, APPLIANCE_CPPID, null,null);

		verify(discoveryListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	public void testDCSEventReceivedDiscoverCppIdNullRequested() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CPPController controller = createCppControllerWithListeners(null,discoveryListener, dcsListener);
		
		controller.notifyDCSListener(data, null, "DISCOVER",null);

		verify(discoveryListener).onDiscoverEventReceived(data, true);
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	public void testDCSEventReceivedDiscoverCppIdNull() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners(null,discoveryListener, dcsListener);
		
		controller.notifyDCSListener(data, null, "CHANGE",null);

		verify(discoveryListener).onDiscoverEventReceived(data, false);
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	public void testDCSEventReceivedDiscoverRequested() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CPPController controller = createCppControllerWithListeners(APPLIANCE_CPPID,discoveryListener, dcsListener);

		controller.notifyDCSListener(data, APPLIANCE_CPPID, "DISCOVER",null);

		verify(discoveryListener).onDiscoverEventReceived(data, true);
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	private CPPController createCppControllerWithListeners(String cppId,
			CppDiscoverEventListener discoveryListener,
			DCSEventListener dcsListener) {
		CPPController controller = CPPController.getCppControllerForTesting();
		controller.setCppDiscoverEventListener(discoveryListener);
		controller.addDCSEventListener(cppId, dcsListener);
		return controller;
	}


}
