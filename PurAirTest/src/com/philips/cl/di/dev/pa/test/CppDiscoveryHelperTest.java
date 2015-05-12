package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.CppDiscoverEventListener;
import com.philips.cl.di.dev.pa.cpp.CppDiscoveryHelper;
import com.philips.cl.di.dicomm.util.MockitoTestCase;

public class CppDiscoveryHelperTest extends MockitoTestCase {

	private CppDiscoveryHelper mHelper;
	private CPPController mCppController;
	private CppDiscoverEventListener mDiscListener;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mCppController = mock(CPPController.class);
		mDiscListener = mock(CppDiscoverEventListener.class);
		mHelper = new CppDiscoveryHelper(mCppController, mDiscListener);
	}

	public void testCppDiscoveryConstructor() {
		verify(mCppController, never()).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController).addSignOnListener(mHelper);
		verify(mCppController).setCppDiscoverEventListener(mDiscListener);
		assertFalse(mHelper.getCppDiscoveryPendingForTesting());
	}
	
	public void testCppDiscoveryOnStartNoSignon() {
		mHelper.startDiscoveryViaCpp();

		verify(mCppController, never()).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController, never()).startDCSService();
		verify(mDiscListener, never()).onSignedOnViaCpp();
		verify(mDiscListener, never()).onSignedOffViaCpp();
		assertTrue(mHelper.getCppDiscoveryPendingForTesting());
	}
	
	public void testCppDiscoveryOnStartSignon() {
		when(mCppController.isSignOn()).thenReturn(true);
		
		mHelper.startDiscoveryViaCpp();

		verify(mCppController).publishEvent(isNull(String.class),eq(CppDiscoveryHelper.DISCOVERY_REQUEST), eq(CPPController.DISCOVER), eq(""), anyInt(), anyInt(), anyString());
		verify(mCppController).startDCSService();
		verify(mDiscListener).onSignedOnViaCpp();
		verify(mDiscListener, never()).onSignedOffViaCpp();
		assertFalse(mHelper.getCppDiscoveryPendingForTesting());
	}
	
	public void testCppDiscoveryOnStartStopNoSignon() {
		mHelper.startDiscoveryViaCpp();
		mHelper.stopDiscoveryViaCpp();

		verify(mCppController, never()).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController, never()).startDCSService();
		verify(mDiscListener, never()).onSignedOnViaCpp();
		verify(mDiscListener, never()).onSignedOffViaCpp();
		assertFalse(mHelper.getCppDiscoveryPendingForTesting());
	}
	
	public void testCppDiscoveryOnStartStopNoSignonWaitSignon() {
		mHelper.startDiscoveryViaCpp();
		mHelper.signonStatus(true);
		
		verify(mCppController).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController).startDCSService();
		verify(mDiscListener).onSignedOnViaCpp();
		verify(mDiscListener, never()).onSignedOffViaCpp();
		assertFalse(mHelper.getCppDiscoveryPendingForTesting());
	}
	
	public void testCppDiscoveryOnStartStopNoSignonWaitSignoff() {
		mHelper.startDiscoveryViaCpp();
		mHelper.signonStatus(false);

		verify(mCppController, never()).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController, never()).startDCSService();
		verify(mDiscListener, never()).onSignedOnViaCpp();
		verify(mDiscListener).onSignedOffViaCpp();
		assertTrue(mHelper.getCppDiscoveryPendingForTesting());
	}

}
