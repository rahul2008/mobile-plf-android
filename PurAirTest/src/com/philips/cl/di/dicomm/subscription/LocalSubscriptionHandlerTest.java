package com.philips.cl.di.dicomm.subscription;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.LocalSubscriptionHandler;
import com.philips.cl.di.dicomm.communication.SubscriptionEventListener;

public class LocalSubscriptionHandlerTest extends InstrumentationTestCase {
	
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	private static final String PURIFIER_KEY = "75B9424B0EA8089428915EB0AA1E4B5E";
	
	private static final String VALID_ENCRYPTED_LOCALAIRPORTVENT = "kFn051qs6876EV0Q2JItzPE+OUKBRnfUMWFLnbCw1B7yWm0YH8cvZ1yRnolygyCqJqPSD1QGaKZzp6jJ53AfQ5H0i/Xl1Ek3cglWuoeAjpWpL0lWv4hcEb3jgc0x3LUnrrurrlsqhj1w8bcuwWUhrxTFSJqKUGr15E3gRGPkE+lyRJpXb2RoDDgjIL7KwS3Zrre45+UEr9udE8tfqSQILhbPqjfm/7I9KefpKEmHoz3uNkDCvUlvnpyja8gWueBa9Z3LeW2DApHWflvNLHnFEOsH3rgD/XJC2dIrBWn1qQM=";
	private static final String VALID_ENCRYPTED_LOCALFWEVENT = "sBmgcZ7YiMa/eNNbDLrMDyBcdEVzKY6DJq2IYfoUNfZYacDwEsD0dfAvnbSamcUCAiqc6GNGSPndyegm3WKwwwRh52MyQ6rAe2CqvFibPVXuxlEH31qVBnwqOTdU3J363qgHVR8Z3/1FFyHXGy2nN6s7mAO4Z80WMcyIc2jIRGw=";
	private static final String VALID_DECRYPTED_LOCALAIRPORTEVENT = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"0\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"108\",\"fs2\":\"953\",\"fs3\":\"2874\",\"fs4\":\"2873\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"2693\",\"psens\":\"3\"}";
	private static final String VALID_DECRYPTED_LOCALFWEVENT = "{\"name\":\"AC4373DEV\",\"version\":\"24\",\"upgrade\":\"25\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
	
	
	private LocalSubscriptionHandler mLocalSubscriptionHandler;
	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mNetworkNode;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

		mSubscriptionEventListener = mock(SubscriptionEventListener.class);
		mNetworkNode = mock(NetworkNode.class);
		when(mNetworkNode.getIpAddress()).thenReturn(PURIFIER_IP);		
		when(mNetworkNode.getCppId()).thenReturn(PURIFIER_EUI64);
		when(mNetworkNode.getEncryptionKey()).thenReturn(PURIFIER_KEY);
		
		mLocalSubscriptionHandler = new LocalSubscriptionHandler();
		
		mLocalSubscriptionHandler.registerSubscriptionListener(mSubscriptionEventListener);
		mLocalSubscriptionHandler.enableSubscription(mNetworkNode);
		
		super.setUp();
	}

	public void testUDPEventReceivedDataNull() {
		mLocalSubscriptionHandler.onUDPEventReceived(null, PURIFIER_IP);
		verify(mSubscriptionEventListener, never()).onSubscriptionEventReceived(anyString());
	}
	
	public void testUDPEventReceivedDataEmptyString() {
		mLocalSubscriptionHandler.onUDPEventReceived("", PURIFIER_IP);
		verify(mSubscriptionEventListener, never()).onSubscriptionEventReceived(anyString());
	}
	
	public void testUDPEventReceivedDataNonDecryptableString() {
		String expected = "dfjalsjdfl";
		mLocalSubscriptionHandler.onUDPEventReceived(expected, PURIFIER_IP);

		verify(mSubscriptionEventListener,never()).onSubscriptionEventReceived(expected);
	}
	
	public void testUDPEventReceivedIpNull() {
		String expected = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mLocalSubscriptionHandler.onUDPEventReceived(expected, null);

		verify(mSubscriptionEventListener, never()).onSubscriptionEventReceived(anyString());
	}
	
	public void testUDPEventReceivedIpEmptyString() {
		String expected = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mLocalSubscriptionHandler.onUDPEventReceived(expected, "");

		verify(mSubscriptionEventListener, never()).onSubscriptionEventReceived(anyString());
	}
	
	public void testUDPEventReceivedWrongIp() {
		String expected = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mLocalSubscriptionHandler.onUDPEventReceived(expected, "0.0.0.0");

		verify(mSubscriptionEventListener, never()).onSubscriptionEventReceived(anyString());
	}

	
	public void testUDPEncryptedAPEvent() {
		String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;		
		mLocalSubscriptionHandler.onUDPEventReceived(data, PURIFIER_IP);
		
		verify(mSubscriptionEventListener).onSubscriptionEventReceived(VALID_DECRYPTED_LOCALAIRPORTEVENT);
	}
	
	public void testUDPEncryptedFWEvent() {
		String data = VALID_ENCRYPTED_LOCALFWEVENT;		
		mLocalSubscriptionHandler.onUDPEventReceived(data, PURIFIER_IP);
		
		verify(mSubscriptionEventListener).onSubscriptionEventReceived(VALID_DECRYPTED_LOCALFWEVENT);
	}
	
	public void testUDPEncryptedAPEventWrongKey() {
		when(mNetworkNode.getEncryptionKey()).thenReturn("726783627");
		String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;		
		mLocalSubscriptionHandler.onUDPEventReceived(data, PURIFIER_IP);
		
		verify(mSubscriptionEventListener,never()).onSubscriptionEventReceived(VALID_DECRYPTED_LOCALAIRPORTEVENT);
	}
}
