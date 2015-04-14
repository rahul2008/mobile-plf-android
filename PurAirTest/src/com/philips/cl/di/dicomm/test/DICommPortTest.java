package com.philips.cl.di.dicomm.test;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.communication.ResponseHandler;
import com.philips.cl.di.dicomm.port.DICommPort;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;

import java.util.HashMap;
import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;


public class DICommPortTest extends MockitoTestCase{
	private final String PORT_NAME = "air";
	private final int PORT_PRODUCTID = 1;
	private final String FANSPEED_KEY = "fs";
	private final String FANSPEED_VALUE = "turbo";
	
	private NetworkNode mNetworkNode;
	private CommunicationStrategy mCommunicationStrategy;
	private DICommPort mDICommPort;
	
	@Captor
	private ArgumentCaptor<HashMap<String, String>> mMapCaptor;
	
	@Captor
	private ArgumentCaptor<ResponseHandler> mResponseHandlerCaptor;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();		
		mNetworkNode = mock(NetworkNode.class);
		mCommunicationStrategy = mock(CommunicationStrategy.class);
		mDICommPort = new DICommPortImpl(mNetworkNode, mCommunicationStrategy);
	}
	
	public void testPutProperties(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		
		verify(mCommunicationStrategy,times(1)).putProperties(mMapCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), any(ResponseHandler.class));
		HashMap<String, String> capturedMap = mMapCaptor.getValue();
		assertTrue(capturedMap.containsKey(FANSPEED_KEY));
		assertEquals(FANSPEED_VALUE, capturedMap.get(FANSPEED_KEY));
	}

	
	public void testGetProperties(){
		mDICommPort.getProperties();
		
		verify(mCommunicationStrategy,times(1)).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), any(ResponseHandler.class));
	}
	
	public void testSubscribe(){
		mDICommPort.subscribe();
		
		verify(mCommunicationStrategy,times(1)).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID),eq(DICommPort.SUBSCRIPTION_TTL) ,eq(mNetworkNode), any(ResponseHandler.class));
	}
	
	public void testUnsubscribe(){
		mDICommPort.unsubscribe();
		
		verify(mCommunicationStrategy,times(1)).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID),eq(mNetworkNode), any(ResponseHandler.class));
	}
	
	public void testPeformSubscribeAfterPutProperties(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verify(mCommunicationStrategy,times(1)).putProperties(mMapCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), mResponseHandlerCaptor.capture());
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		mDICommPort.subscribe();
		verify(mCommunicationStrategy,never()).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID),eq(DICommPort.SUBSCRIPTION_TTL) ,eq(mNetworkNode), any(ResponseHandler.class));
		responseHandler.onSuccess(null);
		verify(mCommunicationStrategy,times(1)).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID),eq(DICommPort.SUBSCRIPTION_TTL) ,eq(mNetworkNode), any(ResponseHandler.class));
	}
	
	
	
	
	public class DICommPortImpl extends DICommPort{

		public DICommPortImpl(NetworkNode networkNode,
				CommunicationStrategy communicationStrategy) {
			super(networkNode, communicationStrategy);
		}

		@Override
		public boolean isResponseForThisPort(String response) {
			return true;
		}

		@Override
		public void processResponse(String response) {
			// NOP
		}

		@Override
		public String getDICommPortName() {
			return PORT_NAME;
		}

		@Override
		public int getDICommProductId() {
			return PORT_PRODUCTID;
		}
		
	}

}
