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

    public CommunicationMarshal(DISecurity diSecurity) {
        mLocalStrategy = new LocalStrategy(diSecurity);
        mRemoteStrategy = new RemoteStrategy();
        mNullStrategy = new NullStrategy();
    }

    @Override
    public void getProperties(String portName, int productId,
                              NetworkNode networkNode, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).getProperties(portName, productId, networkNode, responseHandler);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName,
                              int productId, NetworkNode networkNode,
                              ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).putProperties(dataMap, portName, productId, networkNode, responseHandler);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName,
                              int productId, NetworkNode networkNode,
                              ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).addProperties(dataMap, portName, productId, networkNode, responseHandler);
    }

    @Override
    public void deleteProperties(String portName, int productId, NetworkNode networkNode, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).deleteProperties(portName, productId, networkNode, responseHandler);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl,
                          NetworkNode networkNode, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).subscribe(portName, productId, subscriptionTtl, networkNode, responseHandler);
    }

    @Override
    public void unsubscribe(String portName, int productId,
                            NetworkNode networkNode, ResponseHandler responseHandler) {
        findAvailableStrategy(networkNode).unsubscribe(portName, productId, networkNode, responseHandler);
    }

    @Override
    public boolean isAvailable(NetworkNode networkNode) {
        return true;
    }

    private CommunicationStrategy findAvailableStrategy(NetworkNode networkNode) {
        if (mLocalStrategy.isAvailable(networkNode)) {
            return mLocalStrategy;
        } else if (mRemoteStrategy.isAvailable(networkNode)) {
            return mRemoteStrategy;
        }
        return mNullStrategy;
    }

    @Override
    public void enableSubscription(
            SubscriptionEventListener subscriptionEventListener, NetworkNode networkNode) {
        findAvailableStrategy(networkNode).enableSubscription(subscriptionEventListener, networkNode);
    }

    @Override
    public void disableCommunication() {
        mLocalStrategy.disableCommunication();
        mRemoteStrategy.disableCommunication();
        mNullStrategy.disableCommunication();
    }
}
