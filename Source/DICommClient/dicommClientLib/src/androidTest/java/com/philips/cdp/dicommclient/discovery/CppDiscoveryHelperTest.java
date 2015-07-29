/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.testutil.MockitoTestCase;

public class CppDiscoveryHelperTest extends MockitoTestCase {

	private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";

	private CppDiscoveryHelper mCppDiscoveryHelper;
	private CppController mCppController;
	private CppDiscoverEventListener mCppDiscoveryEventListener;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mCppController = mock(CppController.class);
		mCppDiscoveryEventListener = mock(CppDiscoverEventListener.class);
		mCppDiscoveryHelper = new CppDiscoveryHelper(mCppController);
		mCppDiscoveryHelper.setCppDiscoverEventListener(mCppDiscoveryEventListener);
	}

	public void testCppDiscoveryConstructor() {
		verify(mCppController, never()).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController).addSignOnListener(mCppDiscoveryHelper);
		verify(mCppController).setDCSDiscoverEventListener(mCppDiscoveryHelper);
		assertFalse(mCppDiscoveryHelper.getCppDiscoveryPendingForTesting());
	}

	public void testCppDiscoveryOnStartNoSignon() {
		mCppDiscoveryHelper.startDiscoveryViaCpp();

		verify(mCppController, never()).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController, never()).startDCSService();
		verify(mCppDiscoveryEventListener, never()).onSignedOnViaCpp();
		verify(mCppDiscoveryEventListener, never()).onSignedOffViaCpp();
		assertTrue(mCppDiscoveryHelper.getCppDiscoveryPendingForTesting());
	}

	public void testCppDiscoveryOnStartSignon() {
		when(mCppController.isSignOn()).thenReturn(true);

		mCppDiscoveryHelper.startDiscoveryViaCpp();

		verify(mCppController).publishEvent(isNull(String.class),eq(CppDiscoveryHelper.DISCOVERY_REQUEST), eq(CppDiscoveryHelper.ACTION_DISCOVER), eq(""), anyInt(), anyInt(), anyString());
		verify(mCppController).startDCSService();
		verify(mCppDiscoveryEventListener).onSignedOnViaCpp();
		verify(mCppDiscoveryEventListener, never()).onSignedOffViaCpp();
		assertFalse(mCppDiscoveryHelper.getCppDiscoveryPendingForTesting());
	}

	public void testCppDiscoveryOnStartStopNoSignon() {
		mCppDiscoveryHelper.startDiscoveryViaCpp();
		mCppDiscoveryHelper.stopDiscoveryViaCpp();

		verify(mCppController, never()).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController, never()).startDCSService();
		verify(mCppDiscoveryEventListener, never()).onSignedOnViaCpp();
		verify(mCppDiscoveryEventListener, never()).onSignedOffViaCpp();
		assertFalse(mCppDiscoveryHelper.getCppDiscoveryPendingForTesting());
	}

	public void testCppDiscoveryOnStartStopNoSignonWaitSignon() {
		mCppDiscoveryHelper.startDiscoveryViaCpp();
		mCppDiscoveryHelper.signonStatus(true);

		verify(mCppController).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController).startDCSService();
		verify(mCppDiscoveryEventListener).onSignedOnViaCpp();
		verify(mCppDiscoveryEventListener, never()).onSignedOffViaCpp();
		assertFalse(mCppDiscoveryHelper.getCppDiscoveryPendingForTesting());
	}

	public void testCppDiscoveryOnStartStopNoSignonWaitSignoff() {
		mCppDiscoveryHelper.startDiscoveryViaCpp();
		mCppDiscoveryHelper.signonStatus(false);

		verify(mCppController, never()).publishEvent(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt(), anyString());
		verify(mCppController, never()).startDCSService();
		verify(mCppDiscoveryEventListener, never()).onSignedOnViaCpp();
		verify(mCppDiscoveryEventListener).onSignedOffViaCpp();
		assertTrue(mCppDiscoveryHelper.getCppDiscoveryPendingForTesting());
	}

	public void testDCSEventReceivedDiscover() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mCppDiscoveryHelper.onDCSEventReceived(data, APPLIANCE_CPPID, "CHANGE");
		verify(mCppDiscoveryEventListener).onDiscoverEventReceived(any(DiscoverInfo.class), eq(false));
	}

	public void testDCSEventReceivedDiscoverActionEmpty() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mCppDiscoveryHelper.onDCSEventReceived(data, APPLIANCE_CPPID, "");
		verify(mCppDiscoveryEventListener).onDiscoverEventReceived(any(DiscoverInfo.class), eq(false));
	}

	public void testDCSEventReceivedDiscoverActionNull() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mCppDiscoveryHelper.onDCSEventReceived(data, APPLIANCE_CPPID, null);
		verify(mCppDiscoveryEventListener).onDiscoverEventReceived(any(DiscoverInfo.class), anyBoolean());
	}

	public void testDCSEventReceivedDiscoverCppIdNullRequested() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mCppDiscoveryHelper.onDCSEventReceived(data, null, "DISCOVER");
		verify(mCppDiscoveryEventListener).onDiscoverEventReceived(any(DiscoverInfo.class), eq(true));
	}

	public void testDCSEventReceivedDiscoverCppIdNull() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mCppDiscoveryHelper.onDCSEventReceived(data, null, "CHANGE");
		verify(mCppDiscoveryEventListener).onDiscoverEventReceived(any(DiscoverInfo.class), eq(false));
	}

	public void testDCSEventReceivedDiscoverRequested() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mCppDiscoveryHelper.onDCSEventReceived(data, APPLIANCE_CPPID, "DISCOVER");
		verify(mCppDiscoveryEventListener).onDiscoverEventReceived(any(DiscoverInfo.class), eq(true));
	}

	public void testParseDiscoverInfoNullParam() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo(null);
		assertNull(discoverInfo);
	}

	public void testParseDiscoverInfoValidEvent() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		assertNotNull(discoverInfo);
	}

	public void testParseDiscoverInfoEmptyString() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("");
		assertNull(discoverInfo);
	}

	public void testParseDiscoverInfoRandomString() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("fhaksjdfkljashl");
		assertNull(discoverInfo);
	}

	public void testParseDiscoverInfoRandomJSON() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"key\":\"value\"}");
		assertNull(discoverInfo);
	}

	public void testParseDiscoverInfoNoState() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("\"ClientIds\":\"\"}");
		assertNull(discoverInfo);
	}

	public void testParseDiscoverInfoNoClientIds() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"\"}");
		assertNull(discoverInfo);
	}

	public void testParseDiscoveryInfoNoClientId() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Disconnected\"}");
		assertNull(discoverInfo);
	}

	public void testParseDiscoverInfoRandomState() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Random\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		assertNull(discoverInfo);
	}

	public void testParseDiscoverInfoConnectedState() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		assertNotNull(discoverInfo);
	}

	public void testParseDiscoverInfoDisConnectedState() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Disconnected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");
		assertNotNull(discoverInfo);
	}

	public void testParseDiscoverEmptyClientId() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":\"\"\"}");
		assertNull(discoverInfo);
	}

	public void testParseDiscoverEmptyClientIdArray() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[]}");
		assertNull(discoverInfo);
	}

	public void testParseDiscoverSingleClientIdArray() {
		String[] expected = {"1c5a6bfffe63436c"};
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\"]}");

		assertNotNull(discoverInfo);
		assertEquals(expected[0], discoverInfo.getClientIds()[0]);
		assertTrue(discoverInfo.getClientIds().length == 1);
	}

	public void testParseDiscoverDoubleClientIdArray() {
		String[] expected = {"1c5a6bfffe63436c", "1c5a6bfffe634357"};
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");

		assertNotNull(discoverInfo);
		assertEquals(expected[0], discoverInfo.getClientIds()[0]);
		assertEquals(expected[1], discoverInfo.getClientIds()[1]);
		assertTrue(discoverInfo.getClientIds().length == 2);
	}

	public void testParseDiscoverConnectedEvent() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");

		assertNotNull(discoverInfo);
		assertTrue(discoverInfo.isConnected());
	}

	public void testParseDiscoverDisconnectedEvent() {
		DiscoverInfo discoverInfo = CppDiscoveryHelper.parseDiscoverInfo("{\"State\":\"Disconnected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}");

		assertNotNull(discoverInfo);
		assertFalse(discoverInfo.isConnected());
	}
}
