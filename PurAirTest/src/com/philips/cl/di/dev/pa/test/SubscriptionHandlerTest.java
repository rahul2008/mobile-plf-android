package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

public class SubscriptionHandlerTest extends InstrumentationTestCase {
	
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	private static final String PURIFIER_KEY = "75B9424B0EA8089428915EB0AA1E4B5E";
	
	private static final String VALID_ENCRYPTED_LOCALAIRPORTVENT = "kFn051qs6876EV0Q2JItzPE+OUKBRnfUMWFLnbCw1B7yWm0YH8cvZ1yRnolygyCqJqPSD1QGaKZzp6jJ53AfQ5H0i/Xl1Ek3cglWuoeAjpWpL0lWv4hcEb3jgc0x3LUnrrurrlsqhj1w8bcuwWUhrxTFSJqKUGr15E3gRGPkE+lyRJpXb2RoDDgjIL7KwS3Zrre45+UEr9udE8tfqSQILhbPqjfm/7I9KefpKEmHoz3uNkDCvUlvnpyja8gWueBa9Z3LeW2DApHWflvNLHnFEOsH3rgD/XJC2dIrBWn1qQM=";
	private static final String VALID_ENCRYPTED_LOCALFWEVENT = "sBmgcZ7YiMa/eNNbDLrMDyBcdEVzKY6DJq2IYfoUNfZYacDwEsD0dfAvnbSamcUCAiqc6GNGSPndyegm3WKwwwRh52MyQ6rAe2CqvFibPVXuxlEH31qVBnwqOTdU3J363qgHVR8Z3/1FFyHXGy2nN6s7mAO4Z80WMcyIc2jIRGw=";
	private static final String VALID_DECRYPTED_LOCALAIRPORTEVENT = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"0\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"108\",\"fs2\":\"953\",\"fs3\":\"2874\",\"fs4\":\"2873\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"2693\",\"psens\":\"3\"}";
	private static final String VALID_DECRYPTED_LOCALFWEVENT = "{\"name\":\"AC4373DEV\",\"version\":\"24\",\"upgrade\":\"25\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
	
	
	private SubscriptionHandler mSubscriptionHandler;
	private ResponseHandler mResponseHandler;
	private NetworkNode mNetworkNode;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

		mResponseHandler = mock(ResponseHandler.class);
		mNetworkNode = mock(NetworkNode.class);
		when(mNetworkNode.getIpAddress()).thenReturn(PURIFIER_IP);		
		when(mNetworkNode.getCppId()).thenReturn(PURIFIER_EUI64);
		when(mNetworkNode.getEncryptionKey()).thenReturn(PURIFIER_KEY);
		
		mSubscriptionHandler = new SubscriptionHandler(mNetworkNode, mResponseHandler);
		super.setUp();
	}

	public void testUDPEventReceivedDataNull() {
		mSubscriptionHandler.onUDPEventReceived(null, PURIFIER_IP);
		verify(mResponseHandler, never()).onSuccess(anyString());
	}
	
	public void testUDPEventReceivedDataEmptyString() {
		mSubscriptionHandler.onUDPEventReceived("", PURIFIER_IP);
		verify(mResponseHandler, never()).onSuccess(anyString());
	}
	
	public void testUDPEventReceivedDataNonDecryptableString() {
		String expected = "dfjalsjdfl";
		mSubscriptionHandler.onUDPEventReceived(expected, PURIFIER_IP);

		verify(mResponseHandler,never()).onSuccess(expected);
	}
	
	public void testUDPEventReceivedIpNull() {
		String expected = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mSubscriptionHandler.onUDPEventReceived(expected, null);

		verify(mResponseHandler, never()).onSuccess(anyString());
	}
	
	public void testUDPEventReceivedIpEmptyString() {
		String expected = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mSubscriptionHandler.onUDPEventReceived(expected, "");

		verify(mResponseHandler, never()).onSuccess(anyString());
	}
	
	public void testUDPEventReceivedWrongIp() {
		String expected = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mSubscriptionHandler.onUDPEventReceived(expected, "0.0.0.0");

		verify(mResponseHandler, never()).onSuccess(anyString());
	}

	
	public void testUDPEncryptedAPEvent() {
		String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;		
		mSubscriptionHandler.onUDPEventReceived(data, PURIFIER_IP);
		
		verify(mResponseHandler).onSuccess(VALID_DECRYPTED_LOCALAIRPORTEVENT);
	}
	
	public void testUDPEncryptedFWEvent() {
		String data = VALID_ENCRYPTED_LOCALFWEVENT;		
		mSubscriptionHandler.onUDPEventReceived(data, PURIFIER_IP);
		
		verify(mResponseHandler).onSuccess(VALID_DECRYPTED_LOCALFWEVENT);
	}
	
	public void testUDPEncryptedAPEventWrongKey() {
		when(mNetworkNode.getEncryptionKey()).thenReturn("726783627");
		String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;		
		mSubscriptionHandler.onUDPEventReceived(data, PURIFIER_IP);
		
		verify(mResponseHandler,never()).onSuccess(VALID_DECRYPTED_LOCALAIRPORTEVENT);
	}
	
	public void testDCSEventReceivedDataNull() {
		mSubscriptionHandler.onDCSEventReceived(null, PURIFIER_EUI64, null);

		verify(mResponseHandler, never()).onSuccess(anyString());
	}
	
	public void testDCSEventReceivedDataEmptyString() {
		mSubscriptionHandler.onDCSEventReceived("", PURIFIER_EUI64, null);

		verify(mResponseHandler, never()).onSuccess(anyString());
	}
	
	public void testDCSEventReceivedDataRandomString() {
		String expected = "dfjalsjdfl";
		mSubscriptionHandler.onDCSEventReceived(expected, PURIFIER_EUI64, null);

		verify(mResponseHandler).onSuccess(expected);
	}
	
	public void testDCSEventReceivedEui64Null() {
		String data = "dfjalsjdfl";
		mSubscriptionHandler.onDCSEventReceived(data, null, null);

		verify(mResponseHandler, never()).onSuccess(anyString());
	}
	
	public void testDCSEventReceivedEui64EmptyString() {
		String data = "dfjalsjdfl";
		mSubscriptionHandler.onDCSEventReceived(data, "", null);

		verify(mResponseHandler, never()).onSuccess(anyString());
	}
	
	public void testDCSEventReceivedRightEui64() {
		String data = "dfjalsjdfl";
		mSubscriptionHandler.onDCSEventReceived(data, PURIFIER_EUI64, null);

		verify(mResponseHandler).onSuccess(data);
	}
	
	public void testDCSEventReceivedWrongEui64() {
		String data = "dfjalsjdfl";
		mSubscriptionHandler.onDCSEventReceived(data, "0.0.0.0", null);

		verify(mResponseHandler,never()).onSuccess(data);
	}
	
	
}
