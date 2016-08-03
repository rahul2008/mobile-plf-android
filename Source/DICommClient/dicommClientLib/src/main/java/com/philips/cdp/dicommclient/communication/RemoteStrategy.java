/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import java.util.Map;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.RemoteRequest;
import com.philips.cdp.dicommclient.request.RemoteRequestType;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.request.StartDcsRequest;
import com.philips.cdp.dicommclient.subscription.RemoteSubscriptionHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;

public class RemoteStrategy extends CommunicationStrategy {
    private final RequestQueue mRequestQueue;
    private final RemoteSubscriptionHandler mRemoteSuscriptionHandler;
    private final NetworkNode networkNode;
    private final CppController mCppController;

    public RemoteStrategy(final NetworkNode networkNode, final CppController cppController) {
        this.networkNode = networkNode;
        mRequestQueue = new RequestQueue();
        mCppController = cppController;
        mRemoteSuscriptionHandler = new RemoteSubscriptionHandler(cppController);
    }

    @Override
    public void getProperties(final String portName, final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary(new ResponseHandler() {
            @Override
            public void onSuccess(final String data) {
                RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.GET_PROPS, null, responseHandler, mCppController);
                mRequestQueue.addRequest(request);
            }

            @Override
            public void onError(final Error error, final String errorData) {
                responseHandler.onError(error, errorData);
            }
        });
    }

    @Override
    public void putProperties(final Map<String, Object> dataMap, final String portName,
                              final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary(new ResponseHandler() {
            @Override
            public void onSuccess(final String data) {
                RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.PUT_PROPS, dataMap, responseHandler, mCppController);
                mRequestQueue.addRequest(request);
            }

            @Override
            public void onError(final Error error, final String errorData) {
                responseHandler.onError(error, errorData);
            }
        });
    }

    @Override
    public void addProperties(final Map<String, Object> dataMap, final String portName,
                              final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary(new ResponseHandler() {
            @Override
            public void onSuccess(final String data) {
                RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.ADD_PROPS, dataMap, responseHandler, mCppController);
                mRequestQueue.addRequest(request);
            }

            @Override
            public void onError(final Error error, final String errorData) {
                responseHandler.onError(error, errorData);
            }
        });
    }

    @Override
    public void deleteProperties(final String portName, final int productId, final ResponseHandler responseHandler) {
        startDcsIfNecessary(new ResponseHandler() {
            @Override
            public void onSuccess(final String data) {
                RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.DEL_PROPS, null, responseHandler, mCppController);
                mRequestQueue.addRequest(request);
            }

            @Override
            public void onError(final Error error, final String errorData) {
                responseHandler.onError(error, errorData);
            }
        });
    }

    @Override
    public void subscribe(final String portName, final int productId, final int subscriptionTtl,
                          final ResponseHandler responseHandler) {
        startDcsIfNecessary(new ResponseHandler() {
            @Override
            public void onSuccess(final String data) {
                RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.SUBSCRIBE, getSubscriptionData(subscriptionTtl), responseHandler, mCppController);
                mRequestQueue.addRequest(request);
            }

            @Override
            public void onError(final Error error, final String errorData) {
                responseHandler.onError(error, errorData);
            }
        });
    }

    @Override
    public void unsubscribe(final String portName, final int productId,
                            final ResponseHandler responseHandler) {
        startDcsIfNecessary(new ResponseHandler() {
            @Override
            public void onSuccess(final String data) {
                RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.UNSUBSCRIBE, getUnsubscriptionData(), responseHandler, mCppController);
                mRequestQueue.addRequest(request);
            }

            @Override
            public void onError(final Error error, final String errorData) {
                responseHandler.onError(error, errorData);
            }
        });
    }

    @Override
    public boolean isAvailable() {
        return networkNode.getConnectionState().equals(ConnectionState.CONNECTED_REMOTELY);
    }

    @Override
    public void enableSubscription(final SubscriptionEventListener subscriptionEventListener) {
        startDcsIfNecessary(new ResponseHandler() {
            @Override
            public void onSuccess(final String data) {
                mRemoteSuscriptionHandler.enableSubscription(networkNode, subscriptionEventListener);
            }

            @Override
            public void onError(final Error error, final String errorData) {
                //nobody to report this error to
            }
        });

    }

    @Override
    public void disableCommunication() {
        mRemoteSuscriptionHandler.disableSubscription();
        mCppController.stopDCSService();
    }

    private void startDcsIfNecessary(ResponseHandler responseHandler) {
        if (mCppController.getState() != CppController.ICP_CLIENT_DCS_STATE.STARTED) {
            mRequestQueue.addRequestInFrontOfQueue(new StartDcsRequest(mCppController, responseHandler));
        } else {
            responseHandler.onSuccess(null);
        }
    }
}
