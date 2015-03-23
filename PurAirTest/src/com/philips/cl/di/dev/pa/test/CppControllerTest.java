package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.CppDiscoverEventListener;
import com.philips.cl.di.dev.pa.cpp.DCSEventListener;

public class CppControllerTest extends InstrumentationTestCase {
	
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		super.setUp();
	}

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
		String eui64 = "valid eui64";
		String action = "valid action";
		
		DCSEventListener dcsListener = mock(DCSEventListener.class);		
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners(eui64,discoveryListener, dcsListener);

		controller.notifyDCSListener(data, eui64, action,null);
		
		verify(dcsListener).onDCSEventReceived(data, eui64, action);
		verify(discoveryListener,never()).onDiscoverEventReceived(anyString(),anyBoolean());
	}
	
	public void testNotifyDCSListenerNullEui64() {
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
		String eui64 = "valid eui64";
		
		DCSEventListener dcsListener = mock(DCSEventListener.class);		
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		CPPController controller = createCppControllerWithListeners(eui64,discoveryListener, dcsListener);
		
		
		controller.notifyDCSListener(data, eui64, null,null);
		
		verify(dcsListener,never()).onDCSEventReceived(anyString(), anyString(), anyString());
		verify(discoveryListener,never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testDCSEventReceivedDiscover() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CPPController controller = createCppControllerWithListeners(PURIFIER_EUI64,discoveryListener, dcsListener);
		
		controller.notifyDCSListener(data, PURIFIER_EUI64, "CHANGE",null);

		verify(discoveryListener).onDiscoverEventReceived(data, false);
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}
	
	public void testDCSEventReceivedDiscoverActionEmpty() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CPPController controller = createCppControllerWithListeners(PURIFIER_EUI64,discoveryListener, dcsListener);
		
		controller.notifyDCSListener(data, PURIFIER_EUI64, "",null);

		verify(discoveryListener).onDiscoverEventReceived(data, false);
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	public void testDCSEventReceivedDiscoverActionNull() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CPPController controller = createCppControllerWithListeners(PURIFIER_EUI64,discoveryListener, dcsListener);
		
		controller.notifyDCSListener(data, PURIFIER_EUI64, null,null);

		verify(discoveryListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}
	
	public void testDCSEventReceivedDiscoverEui64NullRequested() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		CppDiscoverEventListener discoveryListener = mock(CppDiscoverEventListener.class);
		DCSEventListener dcsListener = mock(DCSEventListener.class);
		CPPController controller = createCppControllerWithListeners(null,discoveryListener, dcsListener);
		
		controller.notifyDCSListener(data, null, "DISCOVER",null);

		verify(discoveryListener).onDiscoverEventReceived(data, true);
		verify(dcsListener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}

	public void testDCSEventReceivedDiscoverEui64Null() {
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
		CPPController controller = createCppControllerWithListeners(PURIFIER_EUI64,discoveryListener, dcsListener);
		
		controller.notifyDCSListener(data, PURIFIER_EUI64, "DISCOVER",null);

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
