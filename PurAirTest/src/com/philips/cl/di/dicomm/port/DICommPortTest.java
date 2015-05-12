package com.philips.cl.di.dicomm.port;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import com.philips.cdp.dicomm.util.WrappedHandler;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.communication.ResponseHandler;
import com.philips.cl.di.dicomm.util.MockitoTestCase;


public class DICommPortTest extends MockitoTestCase{

	private final String PORT_NAME = "air";
	private final int PORT_PRODUCTID = 1;
	private final String FANSPEED_KEY = "fs";
	private final String FANSPEED_VALUE = "turbo";
	private final String POWER_KEY = "pwr";
	private final String POWER_VALUE = "1";
	private final String CHILDLOCK_KEY = "childlock";
	private final String CHILDLOCK_VALUE = "0";

	private NetworkNode mNetworkNode;
	private CommunicationStrategy mCommunicationStrategy;

	private DICommPort<?> mDICommPort;

	@Captor
	private ArgumentCaptor<Map<String, Object>> mMapCaptor;

	@Captor
	private ArgumentCaptor<ResponseHandler> mResponseHandlerCaptor;
    private WrappedHandler mHandler;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mNetworkNode = mock(NetworkNode.class);
        mCommunicationStrategy = mock(CommunicationStrategy.class);
        mHandler = mock(WrappedHandler.class);
		mDICommPort = new DICommPortImpl(mNetworkNode, mCommunicationStrategy, mHandler);
	}

	public void testPutProperties(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

		verifyPutPropertiesCalled(true);
		Map<String, Object> capturedMap = mMapCaptor.getValue();
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

	public void testPerformPutAfterPutPropertiesSuccess(){
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

		mResponseHandlerCaptor.getValue().onError(null, null);
		verifySubscribeCalled(true);
	}


	public void testPerformUnsubscribeAfterPutPropertiesError(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);

		mDICommPort.unsubscribe();
		verifyUnsubscribeCalled(false);

		mResponseHandlerCaptor.getValue().onError(null, null);
		verifyUnsubscribeCalled(true);
	}

	public void testPerformGetAfterPutPropertiesError(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);

		mDICommPort.getProperties();
		verifyGetPropertiesCalled(false);

		mResponseHandlerCaptor.getValue().onError(null, null);
		verifyGetPropertiesCalled(true);
	}

	public void testPerformPutAfterPutPropertiesError(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(false);

		responseHandler.onError(null, null);
		verifyPutPropertiesCalled(true);
	}

	public void testDoNotRetryPutAfterPutPropertiesError(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		reset(mCommunicationStrategy);

		mResponseHandlerCaptor.getValue().onError(null, null);
		verifyPutPropertiesCalled(false);
	}

	public void testDoNotPerformSuscribeAfterSubscribeSuccess(){
		mDICommPort.subscribe();
		verifySubscribeCalled(true);
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		mDICommPort.subscribe();
		verifySubscribeCalled(false);

		responseHandler.onSuccess(null);
		verifySubscribeCalled(false);
	}

	public void testDoNotPerformGetAfterSubscribeSuccess(){
		mDICommPort.subscribe();
		verifySubscribeCalled(true);

		mDICommPort.getProperties();
		verifyGetPropertiesCalled(false);

		mResponseHandlerCaptor.getValue().onSuccess(null);
		verifyGetPropertiesCalled(false);
	}

	public void testPerformGetAfterSubscribeError(){
		mDICommPort.subscribe();
		verifySubscribeCalled(true);

		mDICommPort.getProperties();
		verifyGetPropertiesCalled(false);

		mResponseHandlerCaptor.getValue().onError(null, null);
		verifyGetPropertiesCalled(true);
	}

	public void testDoNotRetrySuscribeAfterSubscribeError(){
		mDICommPort.subscribe();
		verifySubscribeCalled(true);
		reset(mCommunicationStrategy);

		mResponseHandlerCaptor.getValue().onError(null, null);
		verifySubscribeCalled(false);
	}


	public void testDoNotPerformUnsubscribeAfterUnsubscribeSuccess(){
		mDICommPort.unsubscribe();
		verifyUnsubscribeCalled(true);
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		mDICommPort.unsubscribe();
		verifyUnsubscribeCalled(false);

		responseHandler.onSuccess(null);
		verifyUnsubscribeCalled(false);
	}

	public void testDoNotPerformGetAfterUnsubscribeSuccess(){
		mDICommPort.unsubscribe();
		verifyUnsubscribeCalled(true);

		mDICommPort.getProperties();
		verifyGetPropertiesCalled(false);

		mResponseHandlerCaptor.getValue().onSuccess(null);
		verifyGetPropertiesCalled(false);
	}

	public void testDonotRetryUnSubscribeAfterUnsubscribeError(){
		mDICommPort.unsubscribe();
		verifyUnsubscribeCalled(true);
		reset(mCommunicationStrategy);

		mResponseHandlerCaptor.getValue().onError(null, null);
		verifyUnsubscribeCalled(false);
	}

	public void testPerformGetAfterUnsubscribeError(){
		mDICommPort.unsubscribe();
		verifyUnsubscribeCalled(true);

		mDICommPort.getProperties();
		verifyGetPropertiesCalled(false);

		mResponseHandlerCaptor.getValue().onError(null, null);
		verifyGetPropertiesCalled(true);
	}

	public void testDoNotPerformGetAfterGetSuccess(){
		mDICommPort.getProperties();
		verifyGetPropertiesCalled(true);
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		mDICommPort.getProperties();
		verifyGetPropertiesCalled(false);

		responseHandler.onSuccess(null);
		verifyGetPropertiesCalled(false);
	}

	public void testDoNotPerformGetAfterGetError(){
		mDICommPort.getProperties();
		verifyGetPropertiesCalled(true);
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		mDICommPort.getProperties();
		verifyGetPropertiesCalled(false);

		responseHandler.onError(null, null);
		verifyGetPropertiesCalled(false);
	}

	public void testPerformRequestsOnPriority(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		mDICommPort.getProperties();
		mDICommPort.unsubscribe();
		mDICommPort.subscribe();
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);

		responseHandler.onSuccess(null);
		verifyPutPropertiesCalled(true);
		responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		responseHandler.onSuccess(null);
		verifySubscribeCalled(true);
		responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		responseHandler.onSuccess(null);
		verifyUnsubscribeCalled(true);
		responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		responseHandler.onSuccess(null);
		verifyGetPropertiesCalled(false);
	}

    public void test_ShouldPostRunnable_WhenSubscribeIsCalled() throws Exception {
        mDICommPort.subscribe();

        verify(mHandler).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    public void test_ShouldNotPostRunnableTwice_WhenSubscribeIsCalledTwice() throws Exception {
        mDICommPort.subscribe();
        mDICommPort.subscribe();

        verify(mHandler).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    public void test_ShouldPostRunnableAgain_WhenSubscribeIsCalled_AfterSubscribeResponseIsReceived() throws Exception {
        mDICommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        mDICommPort.subscribe();

        verify(mHandler, Mockito.times(2)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    public void test_ShouldSubscribeToCommunicationStrategy_WhenSubscribeIsCalled() throws Exception {
        mDICommPort.subscribe();

        verifySubscribeCalled(true);
    }

    public void test_ShouldUnsubscribeFromCommunicationStrategy_WhenUnsubscribeIsCalled() throws Exception {
        mDICommPort.unsubscribe();

        verifyUnsubscribeCalled(true);
    }
    
    public void test_ShouldRemoveAndAddRunnable_WhenSubscribeIsCalled() throws Exception {
        mDICommPort.subscribe();

        Runnable runnable = captureResubscribeHandler();
        verify(mHandler).removeCallbacks(runnable);
    }

    public void test_ShouldRemoveSubscribeRunnable_WhenStopResubscribeIsCalled() throws Exception {
        mDICommPort.subscribe();
        mDICommPort.stopResubscribe();

        Runnable runnable = captureResubscribeHandler();

        verify(mHandler, Mockito.times(2)).removeCallbacks(runnable);
    }

    public void test_ShouldRemoveSubscribeRunnable_WhenUnsubscribeIsCalled() throws Exception {
        mDICommPort.subscribe();
        mDICommPort.unsubscribe();

        Runnable runnable = captureResubscribeHandler();

        verify(mHandler, Mockito.times(2)).removeCallbacks(runnable);
    }

    public void test_ShouldRepostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterSubscribeResponseIsReceived() throws Exception {
        mDICommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(mHandler, Mockito.times(2)).postDelayed(Mockito.eq(runnable), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    public void test_ShouldNotRepostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterStopResubscribeIsCalled() throws Exception {
        mDICommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        mDICommPort.stopResubscribe();

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    public void test_ShouldRePostSubscribeRunnable_WhenSubscribeRunnableIsExecuted_AfterStopResubscribeAndSubscribeIsCalled() throws Exception {
        mDICommPort.stopResubscribe();

        mDICommPort.subscribe();
        verify(mHandler, Mockito.times(1)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));

        verifySubscribeCalled(true);
        ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
        responseHandler.onSuccess(null);

        Runnable runnable = captureResubscribeHandler();
        runnable.run();

        verify(mHandler, Mockito.times(2)).postDelayed(Mockito.any(Runnable.class), Mockito.eq(DICommPort.SUBSCRIPTION_TTL_MS));
    }

    private Runnable captureResubscribeHandler() {
        ArgumentCaptor<Runnable> runnableCaptor =  ArgumentCaptor.forClass(Runnable.class);
        verify(mHandler).postDelayed(runnableCaptor.capture(), Mockito.anyInt());
        Runnable runnable = runnableCaptor.getValue();
        return runnable;
    }

	public void testMultiplePutRequests(){
		mDICommPort.putProperties(FANSPEED_KEY, FANSPEED_VALUE);
		verifyPutPropertiesCalled(true);
		ResponseHandler responseHandler = mResponseHandlerCaptor.getValue();
		reset(mCommunicationStrategy);

		mDICommPort.putProperties(POWER_KEY, POWER_VALUE);
		mDICommPort.putProperties(CHILDLOCK_KEY, CHILDLOCK_VALUE);

		responseHandler.onSuccess(null);

		verifyPutPropertiesCalled(true);

		Map<String, Object> capturedMap = mMapCaptor.getValue();
		assertTrue(capturedMap.containsKey(POWER_KEY));
		assertTrue(capturedMap.containsKey(CHILDLOCK_KEY));
		assertEquals(POWER_VALUE, capturedMap.get(POWER_KEY));
		assertEquals(CHILDLOCK_VALUE, capturedMap.get(CHILDLOCK_KEY));
		assertEquals(2, capturedMap.size());
	}

	public void testGetPropertiesWhenPortInfoNull() {
		mDICommPort.getPortProperties();
		verifyGetPropertiesCalled(true);
	}

	private void verifyPutPropertiesCalled(boolean invoked) {
		if (invoked) {
			verify(mCommunicationStrategy,times(1)).putProperties(mMapCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), mResponseHandlerCaptor.capture());
		} else {
			verify(mCommunicationStrategy,never()).putProperties(mMapCaptor.capture(), eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), mResponseHandlerCaptor.capture());
		}
	}

	private void verifyGetPropertiesCalled(boolean invoked) {
		if (invoked) {
			verify(mCommunicationStrategy,times(1)).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), mResponseHandlerCaptor.capture());
		} else {
			verify(mCommunicationStrategy,never()).getProperties(eq(PORT_NAME), eq(PORT_PRODUCTID), eq(mNetworkNode), mResponseHandlerCaptor.capture());
		}
	}

	private void verifySubscribeCalled(boolean invoked) {
		if (invoked) {
			verify(mCommunicationStrategy,times(1)).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID),eq(DICommPort.SUBSCRIPTION_TTL) ,eq(mNetworkNode), mResponseHandlerCaptor.capture());
		} else {
			verify(mCommunicationStrategy,never()).subscribe(eq(PORT_NAME), eq(PORT_PRODUCTID),eq(DICommPort.SUBSCRIPTION_TTL) ,eq(mNetworkNode), mResponseHandlerCaptor.capture());
		}
	}

	private void verifyUnsubscribeCalled(boolean invoked) {
		if (invoked) {
			verify(mCommunicationStrategy,times(1)).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID) ,eq(mNetworkNode), mResponseHandlerCaptor.capture());
		} else {
			verify(mCommunicationStrategy,never()).unsubscribe(eq(PORT_NAME), eq(PORT_PRODUCTID) ,eq(mNetworkNode), mResponseHandlerCaptor.capture());
		}
	}


	public class DICommPortImpl extends DICommPort<Object>{

		private WrappedHandler hander;

        public DICommPortImpl(NetworkNode networkNode,
				CommunicationStrategy communicationStrategy, WrappedHandler hander) {
			super(networkNode, communicationStrategy);
            this.hander = hander;
		}

		@Override
		protected WrappedHandler getResubscriptionHandler() {
		    return hander;
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

        @Override
        public boolean supportsSubscription() {
            return false;
        }
	}
}
