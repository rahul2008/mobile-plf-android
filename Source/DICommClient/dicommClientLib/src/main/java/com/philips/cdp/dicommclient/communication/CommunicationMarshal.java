/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;

import java.util.Map;

public class CommunicationMarshal extends CommunicationStrategy {

    private final LocalStrategy mLocalStrategy;
    private final RemoteStrategy mRemoteStrategy;
    private final NullStrategy mNullStrategy;
    private final NetworkNode networkNode;

    public CommunicationMarshal(DISecurity diSecurity, final NetworkNode networkNode) {
        this.networkNode = networkNode;
        mLocalStrategy = new LocalStrategy(diSecurity, networkNode);
        mRemoteStrategy = new RemoteStrategy(networkNode);
        mNullStrategy = new NullStrategy();
    }

    @Override
    public void getProperties(String portName, int productId, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).getProperties(portName, productId, responseHandler);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName,
                              int productId, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).putProperties(dataMap, portName, productId, responseHandler);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName,
                              int productId, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).addProperties(dataMap, portName, productId, responseHandler);
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).deleteProperties(portName, productId, responseHandler);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).subscribe(portName, productId, subscriptionTtl, responseHandler);
    }

    @Override
    public void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).unsubscribe(portName, productId, responseHandler);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    private CommunicationStrategy findAvailableStrategy(NetworkNode networkNode) {
        if (mLocalStrategy.isAvailable()) {
            return mLocalStrategy;
        } else if (mRemoteStrategy.isAvailable()) {
            return mRemoteStrategy;
        }
        return mNullStrategy;
    }

    @Override
    public void enableSubscription(
            SubscriptionEventListener subscriptionEventListener) {
        findAvailableStrategy(networkNode).enableSubscription(subscriptionEventListener);
    }

    @Override
    public void disableCommunication() {
        mLocalStrategy.disableCommunication();
        mRemoteStrategy.disableCommunication();
        mNullStrategy.disableCommunication();
    }
}
