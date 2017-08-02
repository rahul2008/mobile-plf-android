/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.core.communication;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;

public class CombinedCommunicationStrategy extends CommunicationStrategy {

    @NonNull
    private final LinkedHashSet<CommunicationStrategy> communicationStrategies;

    public CombinedCommunicationStrategy(@NonNull CommunicationStrategy... communicationStrategies) {
        this.communicationStrategies = new LinkedHashSet<>(Arrays.asList(communicationStrategies));
        if (this.communicationStrategies.isEmpty()) {
            throw new IllegalArgumentException("CombinedCommunicationStrategy needs to be constructed with at least 1 communication strategy.");
        }
        this.communicationStrategies.add(new NullCommunicationStrategy());
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

    @Override
    public void enableCommunication(SubscriptionEventListener subscriptionEventListener) {
        findAvailableStrategy().enableCommunication(subscriptionEventListener);
    }

    @Override
    public void disableCommunication() {
        for (CommunicationStrategy strategy : communicationStrategies) {
            strategy.disableCommunication();
        }
    }

    private CommunicationStrategy findAvailableStrategy() {
        for (CommunicationStrategy strategy : communicationStrategies) {
            if (strategy.isAvailable()) {
                return strategy;
            }
        }
        throw new IllegalStateException("No strategies are available.");
    }
}
