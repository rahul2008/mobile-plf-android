/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.cloudcontroller.listener.AppUpdateListener;
import com.philips.cdp.cloudcontroller.listener.DcsEventListener;
import com.philips.cdp.cloudcontroller.listener.DcsResponseListener;
import com.philips.cdp.cloudcontroller.listener.PublishEventListener;
import com.philips.cdp.cloudcontroller.listener.SendNotificationRegistrationIdListener;
import com.philips.cdp.cloudcontroller.listener.SignonListener;
import com.philips.cdp.cloudcontroller.pairing.PairingController;

/**
 * The central entry point for the CloudController. The CloudController gives access to the Device Cloud,
 * allowing remote control of a device.
 */
public interface CloudController {

    /**
     * The constant NOTIFICATION_SERVICE_TAG.
     */
    String NOTIFICATION_SERVICE_TAG = "3pns";
    /**
     * The constant NOTIFICATION_PROTOCOL.
     */
    String NOTIFICATION_PROTOCOL = "push";

    /**
     * The enum Icp client dcs state.
     */
    enum ICPClientDCSState {
        /**
         * Started icp client dcs state.
         */
        STARTED, /**
         * Starting icp client dcs state.
         */
        STARTING, /**
         * Stopped icp client dcs state.
         */
        STOPPED, /**
         * Stopping icp client dcs state.
         */
        STOPPING
    }

    /**
     * ICP sign on state. Indicates if the {@link CloudController} has signed in to the Device Cloud.
     */
    enum SignOnState {
        /**
         * Not signed on state.
         */
        NOT_SIGNED_ON,
        /**
         * Signing on state.
         */
        SIGNING,
        /**
         * Signed on state.
         */
        SIGNED_ON
    }

    /**
     * The interface Dcs start listener.
     */
    interface DCSStartListener {
        /**
         * On response received.
         */
        void onResponseReceived();
    }

    /**
     * Provision a Key from the Device Cloud and sign on.
     * <p>
     * If the key is already provisioned, it will not be reprovisioned.
     * If the CloudController is already signed on this method does nothing.
     */
    void signOnWithProvisioning();

    /**
     * Check if the {@link CloudController} has signed on to the Device Cloud.
     *
     * @return true if signed on.
     */
    boolean isSignOn();

    /**
     * Add a {@link SignonListener}.
     * <p>
     * Multiple listeners can be added. If the listener was added previously this method does nothing.
     *
     * @param signOnListener listener to be added.
     */
    void addSignOnListener(@NonNull SignonListener signOnListener);

    /**
     * Remove a {@link SignonListener}.
     * <p>
     * If the listener was not addded or removed previously this method does nothing.
     *
     * @param signOnListener listener to be removed.
     */
    void removeSignOnListener(@NonNull SignonListener signOnListener);

    /**
     * Set the {@link SendNotificationRegistrationIdListener}.
     *
     * @param listener listener to be set. Can be null to remove the current listener.
     */
    void setNotificationListener(@Nullable SendNotificationRegistrationIdListener listener);

    /**
     * Set the {@link ICPDownloadListener}.
     *
     * @param downloadDataListener listener to be set. Can be null to remove the current listener.
     */
    void setDownloadDataListener(@Nullable ICPDownloadListener downloadDataListener);

    /**
     * Remove the current {@link ICPDownloadListener}.
     * <p>
     * In effect this is the same as calling {@link #setDownloadDataListener(ICPDownloadListener)}
     * with the {@link ICPDownloadListener} set to null.
     */
    void removeDownloadDataListener();

    /**
     * Add {@link DcsEventListener} for a cppId.
     * <p>
     * If a listener was already added for the cppId it will be replaced. To remove a listener use
     * {@link #removeDCSEventListener(String)}.
     *
     * @param cppId            the cppId for the listener.
     * @param dcsEventListener the listener.
     */
    void addDCSEventListener(@NonNull String cppId, @NonNull DcsEventListener dcsEventListener);

    /**
     * Remove {@link DcsEventListener} for a cppId.
     *
     * @param cppId the cppId to remove the listener for.
     */
//DI-Comm change. Added one more method to disable remote subscription
    void removeDCSEventListener(@NonNull String cppId);

    /**
     * Sets {@link DcsEventListener} for discovery.
     * <p>
     * If a listener was previously added it will be replaced. To remove the listener set it to null.
     *
     * @param mCppDiscoverEventListener the listener.
     */
    void setDCSDiscoverEventListener(DcsEventListener mCppDiscoverEventListener);

    /**
     * Add a {@link DcsResponseListener}.
     * <p>
     * If it was already added this method does nothing. To remove the listener use
     * {@link #removeDCSResponseListener(DcsResponseListener)}.
     *
     * @param dcsResponseListener the listener.
     */
    void addDCSResponseListener(@NonNull DcsResponseListener dcsResponseListener);

    /**
     * Remove a {@link DcsResponseListener}.
     * <p>
     * If it was not added or previously removed this method does nothing.
     *
     * @param dcsResponseListener the listener.
     */
    void removeDCSResponseListener(@NonNull DcsResponseListener dcsResponseListener);

    /**
     * Add a {@link PublishEventListener}.
     * <p>
     * If it was already added this method does nothing. To remove the listener use
     * {@link #removePublishEventListener(PublishEventListener)}.
     *
     * @param publishEventListener the listener.
     */
    void addPublishEventListener(@NonNull PublishEventListener publishEventListener);

    /**
     * Remove a {@link PublishEventListener}.
     * <p>
     * If it was not added or previously removed this method does nothing.
     *
     * @param publishEventListener the publish event listener
     */
    void removePublishEventListener(PublishEventListener publishEventListener);

    /**
     * Start DCS service.
     *
     * @param dcsStartListener the dcs start listener
     */
    void startDCSService(DCSStartListener dcsStartListener);

    /**
     * Stop DCS service.
     */
    void stopDCSService();

    /**
     * Notify DCS listener.
     *
     * @param data           the data.
     * @param fromEui64      the eui64 used.
     * @param action         the action that has been done.
     * @param conversationId the conversationId.
     */
    void notifyDCSListener(String data, String fromEui64, String action, String conversationId);

    /**
     * Publish an event to an appliance.
     * <p>
     * Requires the {@link CloudController} to be signed on, otherwise it will return -1.
     *
     * @param eventData      the event data.
     * @param eventType      the event type.
     * @param actionName     the action name.
     * @param conversationId the conversationID.
     * @param priority       the priority.
     * @param ttl            the ttl.
     * @param applianceEui64 the appliance Eui64 id.
     * @return ID of the message sent to the appliance.
     */
    int publishEvent(String eventData, String eventType,
                     String actionName, String conversationId, int priority,
                     int ttl, @Nullable String applianceEui64);

    /**
     * Send notification registration.
     * <p>
     * Requires the {@link CloudController} to be signed on, otherwise it will return false.
     *
     * @param gcmRegistrationId the gcm registration id
     * @param provider          the provider
     * @return true if successful.
     */
    boolean sendNotificationRegistrationId(String gcmRegistrationId, String provider);

    /**
     * This method will download data from the Cloud.
     *
     * @param query      the query.
     * @param bufferSize the buffer size.
     */
    void downloadDataFromCPP(String query, int bufferSize);

    /**
     * Sets the {@link AppUpdateListener}.
     *
     * @param listener the listener.
     */
    void setAppUpdateNotificationListener(@Nullable AppUpdateListener listener);

    /**
     * Start a new app update download.
     * <p>
     * Make sure to set a listener using {@link #setAppUpdateNotificationListener(AppUpdateListener)}
     * before calling this function.
     */
    void startNewAppUpdateDownload();

    /**
     * Gets icp client version.
     *
     * @return the icp client version
     */
    String getICPClientVersion();

    /**
     * Fetch icp components.
     *
     * @param appComponentId the app component id.
     */
    void fetchICPComponents(String appComponentId);

    /**
     * Gets app type.
     *
     * @return the app type.
     */
    String getAppType();

    /**
     * Gets app CppId.
     *
     * @return the app CppId.
     */
    String getAppCppId();

    /**
     * Gets sign on state.
     *
     * @return the current state.
     */
    SignOnState getSignOnState();

    /**
     * Get the {@link ICPClientDCSState}.
     *
     * @return the current state.
     */
    ICPClientDCSState getState();

    /**
     * Get the pairing controller.
     *
     * @return the pairing controller.
     */
    PairingController getPairingController();
}
