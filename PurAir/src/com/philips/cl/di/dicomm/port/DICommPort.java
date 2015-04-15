package com.philips.cl.di.dicomm.port;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

public abstract class DICommPort {
	
	protected final NetworkNode mNetworkNode;
	private CommunicationStrategy mCommunicationStrategy;
	
	private boolean mHasOutstandingRequest;
	private boolean mIsApplyingChanges;
	private boolean mGetPropertiesRequested;
	private boolean mSubscribeRequested;
	private boolean mUnsubscribeRequested;
	private HashMap<String,String> mPutPropertiesMap;
	
	public static final int SUBSCRIPTION_TTL = 300;
	
	public DICommPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy){
		mNetworkNode = networkNode;
		mCommunicationStrategy = communicationStrategy;
		mPutPropertiesMap = new HashMap<String, String>();
	}
	
	public abstract boolean isResponseForThisPort(String response);
	
	public abstract void processResponse(String response);
	
	public abstract String getDICommPortName();
	
	public abstract int getDICommProductId();

	public boolean isApplyingChanges(){
	   	return mIsApplyingChanges;
	}	    
	
	public boolean isPutPropertiesRequested(){
	   	return !mPutPropertiesMap.isEmpty();
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
    	mPutPropertiesMap.put(key, value);
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
	}
    
    
    //TODO:DICOMM Refactor, define an interface to notify appliance.
    private void performPutProperties(){
    	final HashMap<String, String> propertiesToSend = new HashMap<String, String>(mPutPropertiesMap);
    	mPutPropertiesMap.clear();
    	mCommunicationStrategy.putProperties(propertiesToSend, getDICommPortName(), getDICommProductId(),mNetworkNode,new ResponseHandler() {
			
			@Override
			public void onSuccess(String data) {
				handleResponse(data);
				requestCompleted();
			}

			public void onError(Error error) {
				// TODO: DICOMM Refactor, Retry request.
				requestCompleted();
			}
		});
    }
    
    private void performGetProperties(){
    	mCommunicationStrategy.getProperties(getDICommPortName(), getDICommProductId(), mNetworkNode, new ResponseHandler() {
			
			@Override
			public void onSuccess(String data) {
				handleResponse(data);
				requestCompleted();
			}
			
			@Override
			public void onError(Error error) {
				// TODO: DICOMM Refactor, Retry request.
				mGetPropertiesRequested = false;
				requestCompleted();
			}
		});
    }
    
    private void performSubscribe(){
    	mCommunicationStrategy.subscribe(getDICommPortName(), getDICommProductId(),SUBSCRIPTION_TTL, mNetworkNode, new ResponseHandler() {
			
			@Override
			public void onSuccess(String data) {
				mSubscribeRequested = false;
				handleResponse(data);
				requestCompleted();
			}
			
			@Override
			public void onError(Error error) {
				// TODO: DICOMM Refactor, Retry request.
				mSubscribeRequested = false;
				requestCompleted();
			}
		});
    }
    
    private void performUnsubscribe(){
    	mCommunicationStrategy.unsubscribe(getDICommPortName(), getDICommProductId(), mNetworkNode, new ResponseHandler() {
			
			@Override
			public void onSuccess(String data) {
				mUnsubscribeRequested = false;
				handleResponse(data);
				requestCompleted();
			}
			
			@Override
			public void onError(Error error) {
				// TODO: DICOMM Refactor, Retry request.
				mUnsubscribeRequested = false;
				requestCompleted();
			}
		});
    }
    
}
