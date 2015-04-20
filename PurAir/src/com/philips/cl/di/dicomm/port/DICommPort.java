package com.philips.cl.di.dicomm.port;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

public abstract class DICommPort {

	public static final int SUBSCRIPTION_TTL = 300;

	protected final NetworkNode mNetworkNode;
	private CommunicationStrategy mCommunicationStrategy;

	private boolean mHasOutstandingRequest;
	private boolean mIsApplyingChanges;
	private boolean mGetPropertiesRequested;
	private boolean mSubscribeRequested;
	private boolean mUnsubscribeRequested;
	private Map<String,Object> mPutPropertiesMap;

	private ArrayList<DIPropertyUpdateHandler> mPropertyUpdateHandlers;
	private ArrayList<DIPropertyErrorHandler> mPropertyErrorHandlers;


	public DICommPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy){
		mNetworkNode = networkNode;
		mCommunicationStrategy = communicationStrategy;
		mPutPropertiesMap = new HashMap<String, Object>();
		mPropertyUpdateHandlers = new ArrayList<DIPropertyUpdateHandler>();
		mPropertyErrorHandlers = new ArrayList<DIPropertyErrorHandler>();
	}

	public abstract boolean isResponseForThisPort(String response);

	public abstract void processResponse(String response);

	public abstract String getDICommPortName();

	public abstract int getDICommProductId();

	public boolean isApplyingChanges(){
	   	return mIsApplyingChanges;
	}

	public boolean isPutPropertiesRequested(){
		synchronized (mPutPropertiesMap) {
			return !mPutPropertiesMap.isEmpty();
		}
	}

	public boolean isGetPropertiesRequested(){
	   	return mGetPropertiesRequested;
	}

	public boolean isSubscribeRequested(){
	   	return mSubscribeRequested;
	}

	public boolean isUnsubcribeRequested(){
	   	return mUnsubscribeRequested;
	}

    public void putProperties(String key, String value){
    	synchronized (mPutPropertiesMap) {
    		mPutPropertiesMap.put(key, value);
		}
    	tryToPerformNextRequest();
    }

    public void getProperties(){
    	mGetPropertiesRequested = true;
    	tryToPerformNextRequest();
    }

    public void subscribe(){
    	mSubscribeRequested = true;
    	tryToPerformNextRequest();
    }

    public void unsubscribe(){
    	mUnsubscribeRequested = true;
    	tryToPerformNextRequest();
    }

    public void registerPropertyUpdateHandler(DIPropertyUpdateHandler handler) {
    	synchronized(mPropertyUpdateHandlers) {
    		mPropertyUpdateHandlers.add(handler);
    	}
    }

    public void unregisterPropertyUpdateHandler(DIPropertyUpdateHandler handler) {
    	synchronized(mPropertyUpdateHandlers) {
    		mPropertyUpdateHandlers.remove(handler);
    	}
    }

    public void registerPropertyErrorHandler(DIPropertyErrorHandler handler) {
    	synchronized(mPropertyErrorHandlers) {
    		mPropertyErrorHandlers.add(handler);
    	}
    }

    public void unregisterPropertyErrorHandler(DIPropertyErrorHandler handler) {
    	synchronized(mPropertyErrorHandlers) {
    		mPropertyErrorHandlers.remove(handler);
    	}
    }

    private void notifyPropertyUpdateHandlers(boolean isSubscription) {
    	synchronized (mPropertyUpdateHandlers) {
    		for (DIPropertyUpdateHandler handler : mPropertyUpdateHandlers) {
    			handler.handlePropertyUpdateForPort(this, isSubscription);
    		}
		}
    }

    private void notifyPropertyErrorHandlers(Error error) {
    	synchronized (mPropertyErrorHandlers) {
    		for (DIPropertyErrorHandler handler : mPropertyErrorHandlers) {
    			handler.handleErrorForPortIfEnabled(this, error);
    		}
    	}
    }

    private void tryToPerformNextRequest(){
    	if(mHasOutstandingRequest){
    		return;
    	}
    	mHasOutstandingRequest = true;

    	if (isPutPropertiesRequested()){
    		performPutProperties();
    	}else if(isSubscribeRequested()){
    		performSubscribe();
    	}else if(isUnsubcribeRequested()){
    		performUnsubscribe();
    	}else if(isGetPropertiesRequested()){
    		performGetProperties();
    	}else{
    		mHasOutstandingRequest = false;
    	}

    }

    private void requestCompleted() {
    	mHasOutstandingRequest = false;
    	tryToPerformNextRequest();
    }

    private void handleResponse(String data) {
		mGetPropertiesRequested = false;
		processResponse(data);
		notifyPropertyUpdateHandlers(false);
	}

    public void handleSubscription(String data) {
		mGetPropertiesRequested = false;
		processResponse(data);
		notifyPropertyUpdateHandlers(true);
	}

    private void performPutProperties() {
    	final HashMap<String, Object> propertiesToSend;

    	synchronized (mPutPropertiesMap) {
    		propertiesToSend = new HashMap<String, Object>(mPutPropertiesMap);
        	mPutPropertiesMap.clear();
		}

    	mIsApplyingChanges = true;
    	mCommunicationStrategy.putProperties(propertiesToSend, getDICommPortName(), getDICommProductId(),mNetworkNode,new ResponseHandler() {

			@Override
			public void onSuccess(String data) {
				handleResponse(data);
				requestCompleted();
				mIsApplyingChanges = false;
			}

			public void onError(Error error) {
				notifyPropertyErrorHandlers(error);
				requestCompleted();
				mIsApplyingChanges = false;
			}
		});
    }

    private void performGetProperties() {
    	mCommunicationStrategy.getProperties(getDICommPortName(), getDICommProductId(), mNetworkNode, new ResponseHandler() {

			@Override
			public void onSuccess(String data) {
				handleResponse(data);
				requestCompleted();
			}

			@Override
			public void onError(Error error) {
				mGetPropertiesRequested = false;
				notifyPropertyErrorHandlers(error);
				requestCompleted();
			}
		});
    }

    private void performSubscribe() {
    	mCommunicationStrategy.subscribe(getDICommPortName(), getDICommProductId(),SUBSCRIPTION_TTL, mNetworkNode, new ResponseHandler() {

			@Override
			public void onSuccess(String data) {
				mSubscribeRequested = false;
				handleResponse(data);
				requestCompleted();
			}

			@Override
			public void onError(Error error) {
				mSubscribeRequested = false;
				notifyPropertyErrorHandlers(error);
				requestCompleted();
			}
		});
    }

    private void performUnsubscribe() {
    	mCommunicationStrategy.unsubscribe(getDICommPortName(), getDICommProductId(), mNetworkNode, new ResponseHandler() {

			@Override
			public void onSuccess(String data) {
				mUnsubscribeRequested = false;
				handleResponse(data);
				requestCompleted();
			}

			@Override
			public void onError(Error error) {
				mUnsubscribeRequested = false;
				notifyPropertyErrorHandlers(error);
				requestCompleted();
			}
		});
    }

}
