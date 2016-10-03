/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

import android.os.Handler;
import android.util.Log;

import com.philips.cdp.dicommclient.util.LogConstants;
import com.philips.icpinterface.CallbackHandler;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;

/**
 * This interface defines the callback method of ICP Client.
 * These functions must be implemented by the application.
 */

public class ICPCallbackHandler implements CallbackHandler {

    ICPEventListener listener;

    /**
     * Callback function for executeCommand.
     * <p>
     *
     * @param command command number. Refer to {@link Commands} for details.
     * @param status  status of the call. Refer to {@link Errors} for details.
     *                on the error values.
     * @param obj     object to reference.
     */
    @Override
    public void callback(int command, int status, ICPClient obj) {
        Log.d(LogConstants.CPPCONTROLLER, "callback command " + command + " status " + status);
        switch (command) {
            case Commands.SIGNON:
                performSignOnCB(status, obj);
                break;

            case Commands.GET_DATETIME:
                getDateTimeCB(status, obj);
                break;

            case Commands.GET_TIMEZONES:
                getTimeZonesCB(status, obj);
                break;

            case Commands.GET_SERVICE_URL:
                getServicePortalCB(status, obj);
                break;

            case Commands.RESET:
                clientResetCB(status, obj);
                break;

            case Commands.FETCH_EVENTS:
                eventsCB(status, obj);
                break;

            case Commands.GET_COMPONENT_DETAILS:
                icpClientGetComponentDetailsCB(status, obj);
                break;

            case Commands.DOWNLOAD_FILE:
                icpClientDownloadFile_CB(status, obj);
                break;

            case Commands.EVENT_NOTIFICATION:
                eventNotificationCB(status, obj);
                break;

            case Commands.DATA_COLLECTION:
                dataCollectionCB(status, obj);
                break;

            case Commands.REGISTER_PRODUCT:
            case Commands.UNREGISTER_PRODUCT:
            case Commands.QUERY_REGISTRATION_STATUS:
            case Commands.QUERY_TC_STATUS:
            case Commands.ACCEPT_TERMSANDCONDITIONS:
                registerationCB(status, obj);
                break;

            case Commands.KEY_PROVISION:
            case Commands.KEY_DEPROVISION:
                provisioningCB(status, obj);
                break;

            case Commands.SUBSCRIBE_EVENTS:
                eventSubscriptionCB(status, obj);
                break;

            case Commands.THIRDPARTY_REGISTER_PROTOCOLADDRS:
                thirdParyNotificationCB(status, obj);
                break;

            case Commands.DOWNLOAD_DATA:
                downloadDataCB(status, obj);
                break;

            case Commands.PUBLISH_EVENT:
                publishEventCB(status, obj);
                break;
            case Commands.CANCEL_EVENT:
                cancelEventCB(status, obj);
                break;
            case Commands.EVENT_DISTRIBUTION_LIST:
                eventDistributionListCB(status, obj);
                break;
            case Commands.PAIRING_ADD_RELATIONSHIP:
                pairingAddRelatioshipCB(status, obj);
                break;
            case Commands.PAIRING_GET_RELATIONSHIPS:
                pairingGetRelationsCB(status, obj);
                break;
            case Commands.PAIRING_REMOVE_RELATIONSHIP:
                pairingRemoveRelationsCB(status, obj);
                break;
            case Commands.PAIRING_ADD_PERMISSIONS:
                pairingAddPermissionsCB(status, obj);
                break;
            case Commands.PAIRING_REMOVE_PERMISSIONS:
                pairingRemovePermissionsCB(status, obj);
                break;
            case Commands.PAIRING_GET_PERMISSIONS:
                pairingGetPermissionsCB(status, obj);
                break;
            default:
                break;
        }
    }


    public void setHandler(ICPEventListener listener) {
        this.listener = listener;
    }

    public void performSignOnCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.SIGNON, status, obj);
    }

    public void getTimeZonesCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.GET_TIMEZONES, status, obj);
    }

    public void getDateTimeCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.GET_DATETIME, status, obj);
    }

    public void getServicePortalCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.GET_SERVICE_URL, status, obj);
    }

    public void clientResetCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.RESET, status, obj);
    }

    public void eventsCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.EVENT_DISTRIBUTION_LIST, status, obj);
    }

    public void icpClientGetComponentDetailsCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.GET_COMPONENT_DETAILS, status, obj);
    }

    public void icpClientDownloadFile_CB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.DOWNLOAD_FILE, status, obj);
    }

    public void eventNotificationCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.EVENT_NOTIFICATION, status, obj);
    }

    public void dataCollectionCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.DATA_COLLECTION, status, obj);
    }

    public void registerationCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.REGISTER_PRODUCT, status, obj);
    }

    public void provisioningCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.KEY_PROVISION, status, obj);
    }

    public void eventSubscriptionCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.SUBSCRIBE_EVENTS, status, obj);
    }

    public void thirdParyNotificationCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.THIRDPARTY_REGISTER_PROTOCOLADDRS, status, obj);
    }

    public void downloadDataCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.DOWNLOAD_DATA, status, obj);
    }

    public void publishEventCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.PUBLISH_EVENT, status, obj);
    }

    public void cancelEventCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(7, status, obj);
    }

    public void eventDistributionListCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(7, status, obj);
    }

    public void pairingAddRelatioshipCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_RELATIONSHIP, status, obj);
    }

    public void pairingGetRelationsCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.PAIRING_GET_RELATIONSHIPS, status, obj);
    }

    public void pairingRemoveRelationsCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.PAIRING_REMOVE_RELATIONSHIP, status, obj);
    }

    public void pairingAddPermissionsCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.PAIRING_ADD_PERMISSIONS, status, obj);
    }

    public void pairingRemovePermissionsCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.PAIRING_REMOVE_PERMISSIONS, status, obj);
    }

    public void pairingGetPermissionsCB(int status, ICPClient obj) {
        listener.onICPCallbackEventOccurred(Commands.PAIRING_GET_PERMISSIONS, status, obj);
    }

    @Override
    public void setHandler(Handler handler) {
        Log.i(LogConstants.CPPCONTROLLER, "setHandler: " + handler);
        //TODO implement
    }
}
