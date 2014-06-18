package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.DCSEventListener;

public class CppControllerTest extends InstrumentationTestCase {
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		super.setUp();
	}

	public void testNotifyDCSListenerNullData() {
		CPPController controller = CPPController.getCppControllerForTesting();
		DCSEventListener listener = mock(DCSEventListener.class);
		
		controller.setDCSEventListener(listener);
		controller.notifyDCSListener(null, "dfasfa", "dfasfa");
		
		verify(listener, never()).onDCSEventReceived(anyString(), anyString(), anyString());
	}
	
	public void testNotifyDCSListenerData() {
		CPPController controller = CPPController.getCppControllerForTesting();
		DCSEventListener listener = mock(DCSEventListener.class);
		
		String data = "valid dcs event";
		String eui64 = "valid eui64";
		String action = "valid action";
		controller.setDCSEventListener(listener);
		controller.notifyDCSListener(data, eui64, action);
		
		verify(listener).onDCSEventReceived(data, eui64, action);
	}
	
	public void testNotifyDCSListenerNullEui64() {
		CPPController controller = CPPController.getCppControllerForTesting();
		DCSEventListener listener = mock(DCSEventListener.class);
		
		String data = "valid dcs event";
		String action = "valid action";
		controller.setDCSEventListener(listener);
		controller.notifyDCSListener(data, null, action);
		
		verify(listener).onDCSEventReceived(data, null, action);
	}
	
	public void testNotifyDCSListenerValidEui64() {
		CPPController controller = CPPController.getCppControllerForTesting();
		DCSEventListener listener = mock(DCSEventListener.class);
		
		String data = "valid dcs event";
		String eui64 = "valid eui64";
		String action = "valid action";
		controller.setDCSEventListener(listener);
		controller.notifyDCSListener(data, eui64, action);
		
		verify(listener).onDCSEventReceived(data, eui64, action);
	}
	
	public void testNotifyDCSListenerNullAction() {
		CPPController controller = CPPController.getCppControllerForTesting();
		DCSEventListener listener = mock(DCSEventListener.class);
		
		String data = "valid dcs event";
		String eui64 = "valid eui64";
		controller.setDCSEventListener(listener);
		controller.notifyDCSListener(data, eui64, null);
		
		verify(listener).onDCSEventReceived(data, eui64, null);
	}
	
	
}
