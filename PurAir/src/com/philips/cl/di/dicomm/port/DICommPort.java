package com.philips.cl.di.dicomm.port;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Looper;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.ResponseHandler;
import com.philips.cl.di.dicomm.util.WrappedHandler;

public abstract class DICommPort<T> {

    public static final int SUBSCRIPTION_TTL = 300;
    public static final int SUBSCRIPTION_TTL_MS = SUBSCRIPTION_TTL * 1000;

	protected final NetworkNode mNetworkNode;
	private CommunicationStrategy mCommunicationStrategy;
    private WrappedHandler mResubscriptionHandler;

	private boolean mHasOutstandingRequest;
	private boolean mIsApplyingChanges;
	private boolean mGetPropertiesRequested;
	private boolean mSubscribeRequested;
	private boolean mUnsubscribeRequested;
    private boolean mStopResubscribe;
    private Object mResubscribeLock = new Object();

	private Map<String,Object> mPutPropertiesMap;
	private T mPortProperties;

	private ArrayList<DIPortListener> mPortListeners;

	public DICommPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy){
		mNetworkNode = networkNode;
		mCommunicationStrategy = communicationStrategy;
		mPutPropertiesMap = new HashMap<String, Object>();
		mPortListeners = new ArrayList<DIPortListener>();
	}

    public abstract boolean isResponseForThisPort(String response);

	protected abstract void processResponse(String response);

	protected abstract String getDICommPortName();

	protected abstract int getDICommProductId();

	public abstract boolean supportsSubscription();

	public T getPortProperties() {
		if (mPortProperties == null) {
			getProperties();
		}
		return mPortProperties;
	}

	protected void setPortProperties(T portProperties) {
		mGetPropertiesRequested = false;
		mPortProperties = portProperties;
	}

    public void putProperties(String key, String value){
    	synchronized (mPutPropertiesMap) {
    		mPutPropertiesMap.put(key, value);
		}
    	tryToPerformNextRequest();
    }

    public void putProperties(Map<String, Object> dataMap){
        synchronized (mPutPropertiesMap) {
            mPutPropertiesMap.putAll(dataMap);
        }
        tryToPerformNextRequest();
    }

    public void getProperties(){
    	mGetPropertiesRequested = true;
    	tryToPerformNextRequest();
    }

    public void subscribe(){
        if(mSubscribeRequested) return;

    	mSubscribeRequested = true;
    	mStopResubscribe = false;

        getResubscriptionHandler().removeCallbacks(mResubscribtionRunnable);
        getResubscriptionHandler().postDelayed(mResubscribtionRunnable, SUBSCRIPTION_TTL_MS);

    	tryToPerformNextRequest();
    }

    protected WrappedHandler getResubscriptionHandler() {
        if(mResubscriptionHandler==null){
            mResubscriptionHandler = new WrappedHandler(new Handler(Looper.getMainLooper()));
        }
        return mResubscriptionHandler;
    }

    private final Runnable mResubscribtionRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (mResubscribeLock) {
                if(!mStopResubscribe){
                    subscribe();
                }
            }
        }
    };

    public void unsubscribe(){
        mUnsubscribeRequested = true;
        stopResubscribe();
        tryToPerformNextRequest();
    }

    public void stopResubscribe(){
        synchronized (mResubscribeLock) {
            mStopResubscribe = true;
        }
        getResubscriptionHandler().removeCallbacks(mResubscribtionRunnable);
    }

    public void registerPortListener(DIPortListener listener) {
		mPortListeners.add(listener);
    }

    public void unregisterPortListener(DIPortListener listener) {
		mPortListeners.remove(listener);
    }

    private void notifyPortListenersOnUpdate(boolean isSubscription) {
        ArrayList<DIPortListener> copyListeners = new ArrayList<DIPortListener>(mPortListeners);
		for (DIPortListener listener : copyListeners) {
			DIRegistration registration = listener.onPortUpdate(this);
			if(registration == DIRegistration.UNREGISTER){
			    mPortListeners.remove(listener);
			}
		}
    }

    private void notifyPortListenersOnError(Error error, String errorData) {
        ArrayList<DIPortListener> copyListeners = new ArrayList<DIPortListener>(mPortListeners);
		for (DIPortListener listener : copyListeners) {
		    DIRegistration registration = listener.onPortError(this, error, errorData);
			if(registration == DIRegistration.UNREGISTER){
                mPortListeners.remove(listener);
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

    public boolean isApplyingChanges(){
        return mIsApplyingChanges;
    }

    private boolean isPutPropertiesRequested(){
        synchronized (mPutPropertiesMap) {
            return !mPutPropertiesMap.isEmpty();
        }
    }

    private boolean isGetPropertiesRequested(){
        return mGetPropertiesRequested;
    }

    private boolean isSubscribeRequested(){
        return mSubscribeRequested;
    }

    private boolean isUnsubcribeRequested(){
        return mUnsubscribeRequested;
    }

    private void requestCompleted() {
    	mHasOutstandingRequest = false;
    	tryToPerformNextRequest();
    }

    private void handleResponse(String data) {
		mGetPropertiesRequested = false;
		processResponse(data);
		notifyPortListenersOnUpdate(false);
	}

    public void handleSubscription(String data) {
		mGetPropertiesRequested = false;
		processResponse(data);
		notifyPortListenersOnUpdate(true);
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

			public void onError(Error error, String errorData) {
				notifyPortListenersOnError(error, errorData);
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
			public void onError(Error error, String errorData) {
				mGetPropertiesRequested = false;
				notifyPortListenersOnError(error, errorData);
				requestCompleted();
			}
		});
    }

    private void performSubscribe() {
    	mCommunicationStrategy.subscribe(getDICommPortName(), getDICommProductId(), SUBSCRIPTION_TTL, mNetworkNode, new ResponseHandler() {

			@Override
			public void onSuccess(String data) {
				mSubscribeRequested = false;
				handleResponse(data);
				requestCompleted();
			}

			@Override
			public void onError(Error error, String errorData) {
				mSubscribeRequested = false;
				notifyPortListenersOnError(error, errorData);
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
			public void onError(Error error, String errorData) {
				mUnsubscribeRequested = false;
				notifyPortListenersOnError(error, errorData);
				requestCompleted();
			}
		});
    }
}
