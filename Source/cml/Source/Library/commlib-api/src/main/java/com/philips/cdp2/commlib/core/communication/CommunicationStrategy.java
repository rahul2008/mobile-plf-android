/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.communication;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.core.util.Availability;

import java.util.Map;

/**
 * The CommunicationStrategy is responsible for communicating to an actual appliance.
 * @publicApi
 */
public interface CommunicationStrategy extends Availability<CommunicationStrategy> {

    /**
     * Retrieves the properties of a {@link com.philips.cdp.dicommclient.port.DICommPort} in an appliance.
     * @param portName String The name of the port for which to get properties
     * @param productId int The ID of the product where the port lives
     * @param responseHandler ResponseHandler The object to notify when the request is completed.
     */
    void getProperties(String portName, int productId, ResponseHandler responseHandler);

    /**
     * Sends properties in <code>dataMap</code> to an appliance
     * @param dataMap Map<String, Object> The properties to send
     * @param portName String The name of the port to send the properties to
     * @param productId int The ID of the product where the port lives
     * @param responseHandler ResponseHandler The object to notify when the request is completed.
     */
    void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler);

    /**
     * Adds properties to a {@link com.philips.cdp.dicommclient.port.DICommPort} in an appliance.
     * @param dataMap Map<String, Object> The properties to send
     * @param portName String the name of the port to add the properties to
     * @param productId int The ID of the product where the port lives
     * @param responseHandler ResponseHandler The object to notify when the request is completed.
     */
    void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler);

    /**
     * Clears the values of a {@link com.philips.cdp.dicommclient.port.DICommPort}.
     * @param portName String the name of the port to add the properties to
     * @param productId int The ID of the product where the port lives
     * @param responseHandler ResponseHandler The object to notify when the request is completed.
     */
    void deleteProperties(String portName, int productId, ResponseHandler responseHandler);

    /**
     * Subscribes to all changes of a {@link com.philips.cdp.dicommclient.port.DICommPort}.
     * @param portName String the name of the port to add the properties to
     * @param productId int The ID of the product where the port lives
     * @param subscriptionTtl int The time (in seconds) a subscription stays valid.
     * @param responseHandler ResponseHandler The object to notify when the request is completed.
     */
    void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler);

    /**
     * Cancels the subscription to the changes of a {@link com.philips.cdp.dicommclient.port.DICommPort}
     * @param portName String the name of the port to add the properties to
     * @param productId int The ID of the product where the port lives
     * @param responseHandler ResponseHandler The object to notify when the request is completed.
     */
    void unsubscribe(String portName, int productId, ResponseHandler responseHandler);

    /**
     * Adds an listener for subscription events
     * @param listener SubscriptionEventListener
     */
    void addSubscriptionEventListener(@NonNull SubscriptionEventListener listener);

    /**
     * Removes an listener for subscription events
     * @param listener SubscriptionEventListener
     */
    void removeSubscriptionEventListener(@NonNull SubscriptionEventListener listener);

    @Deprecated
    void enableCommunication();

    @Deprecated
    void disableCommunication();
}
