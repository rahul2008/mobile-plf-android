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
		
		verifyPutPropertiesCalled(true);
		HashMap<String, String> capturedMap = mMapCaptor.getValue();
		assertTrue(capturedMap.containsKey(FANSPEED_KEY));
		assertEquals(FANSPEED_VALUE, capturedMap.get(FANSPEED_KEY));
	}

	
	public void testGetProperties(){
		mDICommPort.getProperties();
		verifyGetPropertiesCalled(true);
	}
	
	public void testSubscribe(){
		mDICommPort.subscribe();
		verifySubscribeCalled(true);
	}
	
	public void testUnsubscribe(){
		mDICommPort.unsubscribe();
		verifyUnsubscribeCalled(true);
	}
	
	public void testPeformSubscribeAfterPutPropertiesSuccess(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		
		mDICommPort.subscribe();		
		verifySubscribeCalled(false);
		
		mResponseHandlerCaptor.getValue().onSuccess(null);
		verifySubscribeCalled(true);		
	}
	
	
	public void testPerformUnsubscribeAfterPutPropertiesSuccess(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		
		mDICommPort.unsubscribe();		
		verifyUnsubscribeCalled(false);
		
		mResponseHandlerCaptor.getValue().onSuccess(null);
		verifyUnsubscribeCalled(true);
	}
	
	public void testDoNotPerformGetAfterPutPropertiesSuccess(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		
		mDICommPort.getProperties();		
		verifyGetPropertiesCalled(false);
		
		mResponseHandlerCaptor.getValue().onSuccess(null);
		verifyGetPropertiesCalled(false);
	}
	
	public void testDoNotPerformPutAfterPutPropertiesSuccess(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);
		
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(false);
		
		responseHandler.onSuccess(null);
		verifyPutPropertiesCalled(true);
	}
	
	public void testPeformSubscribeAfterPutPropertiesError(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		
		mDICommPort.subscribe();		
		verifySubscribeCalled(false);
		
		mResponseHandlerCaptor.getValue().onError(null);
		verifySubscribeCalled(true);		
	}
	
	
	public void testPerformUnsubscribeAfterPutPropertiesError(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		
		mDICommPort.unsubscribe();		
		verifyUnsubscribeCalled(false);
		
		mResponseHandlerCaptor.getValue().onError(null);
		verifyUnsubscribeCalled(true);
	}
	
	public void testDoNotPerformGetAfterPutPropertiesError(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		
		mDICommPort.getProperties();		
		verifyGetPropertiesCalled(false);
		
		mResponseHandlerCaptor.getValue().onError(null);
		verifyGetPropertiesCalled(true);
	}
	
	public void testDoNotPerformPutAfterPutPropertiesError(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);
		
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(false);
		
		responseHandler.onError(null);
		verifyPutPropertiesCalled(true);
	}
	
	// subsbcribe: test subscribe after subscribe and get after subscribe (also error)
	// unsubsbcribe: test unsubscribe after unsubscribe and get after unsubscribe (also error)
	// get: test get after get (also error)
	
	private void verifyPutPropertiesCalled(boolean invoked) {
		if (invoked) {
			verify(mCommunicationStrategy,times(1)).putProperties(mMapCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), mResponseHandlerCaptor.capture());
		} else {
			verify(mCommunicationStrategy,never()).putProperties(mMapCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), mResponseHandlerCaptor.capture());
		}
	}
	
	private void verifyGetPropertiesCalled(boolean invoked) {
		if (invoked) {
			verify(mCommunicationStrategy,times(1)).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), any(ResponseHandler.class));
		} else {
			verify(mCommunicationStrategy,never()).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), any(ResponseHandler.class));
		}
	}

	private void verifySubscribeCalled(boolean invoked) {
		if (invoked) {
			verify(mCommunicationStrategy,times(1)).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID),eq(DICommPort.SUBSCRIPTION_TTL) ,eq(mNetworkNode), any(ResponseHandler.class));
		} else {
			verify(mCommunicationStrategy,never()).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID),eq(DICommPort.SUBSCRIPTION_TTL) ,eq(mNetworkNode), any(ResponseHandler.class));
		}
	}
	
	private void verifyUnsubscribeCalled(boolean invoked) {
		if (invoked) {
			verify(mCommunicationStrategy,times(1)).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID) ,eq(mNetworkNode), any(ResponseHandler.class));
		} else {
			verify(mCommunicationStrategy,never()).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID) ,eq(mNetworkNode), any(ResponseHandler.class));
		}
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
