/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.cpp;

import android.os.Handler;
import android.support.annotation.NonNull;
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

    private final ICPEventListener mListener;

    public ICPCallbackHandler(@NonNull ICPEventListener listener) {
        mListener = listener;
    }

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
            case Commands.ACCEPT_TERMSANDCONDITIONS:
            case Commands.QUERY_REGISTRATION_STATUS:
            case Commands.QUERY_TC_STATUS:
            case Commands.REGISTER_PRODUCT:
            case Commands.UNREGISTER_PRODUCT:
                mListener.onICPCallbackEventOccurred(Commands.REGISTER_PRODUCT, status, obj);
                break;
            case Commands.KEY_DEPROVISION:
            case Commands.KEY_PROVISION:
                mListener.onICPCallbackEventOccurred(Commands.KEY_PROVISION, status, obj);
                break;
            case Commands.FETCH_EVENTS:
                // TODO Check if this is correct (seems swapped with next case)
                mListener.onICPCallbackEventOccurred(Commands.EVENT_DISTRIBUTION_LIST, status, obj);
                break;
            case Commands.CANCEL_EVENT:
            case Commands.EVENT_DISTRIBUTION_LIST:
                mListener.onICPCallbackEventOccurred(Commands.FETCH_EVENTS, status, obj);
                break;
            case Commands.DATA_COLLECTION:
            case Commands.DOWNLOAD_DATA:
            case Commands.DOWNLOAD_FILE:
            case Commands.EVENT_NOTIFICATION:
            case Commands.GET_COMPONENT_DETAILS:
            case Commands.GET_DATETIME:
            case Commands.GET_SERVICE_URL:
            case Commands.GET_TIMEZONES:
            case Commands.PAIRING_ADD_PERMISSIONS:
            case Commands.PAIRING_ADD_RELATIONSHIP:
            case Commands.PAIRING_GET_PERMISSIONS:
            case Commands.PAIRING_GET_RELATIONSHIPS:
            case Commands.PAIRING_REMOVE_PERMISSIONS:
            case Commands.PAIRING_REMOVE_RELATIONSHIP:
            case Commands.PUBLISH_EVENT:
            case Commands.RESET:
            case Commands.SIGNON:
            case Commands.SUBSCRIBE_EVENTS:
            case Commands.THIRDPARTY_REGISTER_PROTOCOLADDRS:
                mListener.onICPCallbackEventOccurred(command, status, obj);
                break;
            default:
                break;
        }
    }

    @Override
    public void setHandler(Handler handler) {
        Log.i(LogConstants.CPPCONTROLLER, "setHandler: " + handler);
    }
}
