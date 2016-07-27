/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;

import java.util.Map;

public class NullStrategy extends CommunicationStrategy {

    @Override
    public void getProperties(String portName, int productId,
                              NetworkNode networkNode, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOTCONNECTED, null);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName,
                              int productId, NetworkNode networkNode,
                              ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOTCONNECTED, null);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName,
                              int productId, NetworkNode networkNode,
                              ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOTCONNECTED, null);
    }

    @Override
    public void deleteProperties(String portName, int productId, NetworkNode networkNode, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOTCONNECTED, null);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl,
                          NetworkNode networkNode, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOTCONNECTED, null);
    }

    @Override
    public void unsubscribe(String portName, int productId,
                            NetworkNode networkNode, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOTCONNECTED, null);
    }

    @Override
    public boolean isAvailable(NetworkNode networkNode) {
        return true;
    }

    @Override
    public void enableSubscription(
            SubscriptionEventListener subscriptionEventListener, NetworkNode networkNode) {
    }

    @Override
    public void disableCommunication() {
    }
}
