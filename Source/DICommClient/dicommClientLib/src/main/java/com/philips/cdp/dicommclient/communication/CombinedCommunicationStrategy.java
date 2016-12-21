/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;

import java.util.Map;

public class CombinedCommunicationStrategy extends CommunicationStrategy {

    private final LanCommunicationStrategy mLanCommunicationStrategy;
    private final RemoteCommunicationStrategy mRemoteCommunicationStrategy;
    private final NullCommunicationStrategy mNullCommunicationStrategy;

    public CombinedCommunicationStrategy(final NetworkNode networkNode) {
        mLanCommunicationStrategy = new LanCommunicationStrategy(networkNode);
        mRemoteCommunicationStrategy = new RemoteCommunicationStrategy(networkNode, DICommClientWrapper.getCloudController());
        mNullCommunicationStrategy = new NullCommunicationStrategy();
    }

    @Override
    public void getProperties(String portName, int productId, ResponseHandler responseHandler) {
        findAvailableStrategy().getProperties(portName, productId, responseHandler);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName,
                              int productId, ResponseHandler responseHandler) {
        findAvailableStrategy().putProperties(dataMap, portName, productId, responseHandler);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName,
                              int productId, ResponseHandler responseHandler) {
        findAvailableStrategy().addProperties(dataMap, portName, productId, responseHandler);
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
        findAvailableStrategy().deleteProperties(portName, productId, responseHandler);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
        findAvailableStrategy().subscribe(portName, productId, subscriptionTtl, responseHandler);
    }

    @Override
    public void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        findAvailableStrategy().unsubscribe(portName, productId, responseHandler);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    private CommunicationStrategy findAvailableStrategy() {
        if (mLanCommunicationStrategy.isAvailable()) {
            return mLanCommunicationStrategy;
        } else if (mRemoteCommunicationStrategy.isAvailable()) {
            return mRemoteCommunicationStrategy;
        }
        return mNullCommunicationStrategy;
    }

    @Override
    public void enableCommunication(
            SubscriptionEventListener subscriptionEventListener) {
        findAvailableStrategy().enableCommunication(subscriptionEventListener);
    }

    @Override
    public void disableCommunication() {
        mLanCommunicationStrategy.disableCommunication();
        mRemoteCommunicationStrategy.disableCommunication();
        mNullCommunicationStrategy.disableCommunication();
    }
}
