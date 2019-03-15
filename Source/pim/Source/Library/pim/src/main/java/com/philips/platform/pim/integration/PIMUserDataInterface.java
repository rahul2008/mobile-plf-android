package com.philips.platform.pim.integration;

import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;

import java.util.ArrayList;
import java.util.HashMap;

public interface PIMUserDataInterface {

    //Listener name should be LogoutSessionListener
    void logoutSession(LogoutSessionListener logoutSessionListener);

    void refreshSession(RefreshSessionListener refreshSessionListener);

    void refetchUserDetails(RefetchUserDetailsListener userDetailsListener);

    HashMap<String, Object> getUserDetails(ServiceIdentifiers serviceIdentifiers, ArrayList<String> detailKeys) throws Exception;

    //ToDo: Could be generic for Service ID to be conclud
    boolean authorizeLoginToHSDP(HSDPAuthenticationListener hsdpListener);

    UserLoggedInState getUserLoggedInState();

    //We need to maintain list of listeners
    void addUserDataInterfaceListener(UserDataListener listener);

    void removeUserDataInterfaceListener(UserDataListener listener);

    interface UserDataListener extends LogoutSessionListener, RefreshSessionListener, RefetchUserDetailsListener {
    }

    interface LogoutSessionListener {
        void logoutSessionSuccess();

        void logoutSessionFailed(PIMError Error);
    }


    //inProgress implementation would be internal to us and not to pass to proposition, once success or failure then only will return
    interface RefreshSessionListener {
        void refreshSessionSuccess();

        void refreshSessionFailed(PIMError error);

        void forcedLogout();
    }

    interface RefetchUserDetailsListener {

        void onRefetchSuccess();

        void onRefetchFailure(PIMError error);

    }

    public interface HSDPAuthenticationListener {

        void onHSDPLoginSuccess();

        void onHSDPLoginFailure(int errorCode, String msg);
    }

    enum ServiceIdentifiers {
        JANRAIN,
        HSDP_PH
    }

    enum PIMError {
        // Define Error Constatnt error code and message
    }
}
