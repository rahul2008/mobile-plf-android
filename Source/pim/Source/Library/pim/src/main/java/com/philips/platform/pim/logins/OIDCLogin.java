package com.philips.platform.pim.logins;

import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMListener;
import com.philips.platform.pim.listeners.PIMOIDCLoginListener;
import com.philips.platform.pim.manager.PIMUserManager;
import com.philips.platform.pim.models.PIMOIDCUserProfile;

public class OIDCLogin {
    private PIMOIDCConfigration pimOidcConfiuration;
    private PIMListener pimListener;
    private PIMUserManager pimUserManager;


    public OIDCLogin(PIMOIDCConfigration pimConfiuration, PIMListener pimListener) {
        this.pimOidcConfiuration = pimConfiuration;
        pimListener = pimListener;
    }

    public void login(PIMOIDCLoginListener pimLoginListener) {
        //TODO: Check whether user is logged in or not. Inject User manager and fetch user profile. User manager will populate user profile and get to know whether logged in or not.
        // If logged in get user profile from User manager.
        // Not logged in- inject AppAuth manager to do login by passing OIDCConfiguration
        //if not then connect to AppAuth Manager
        //UserManager: Injecting UserManage for now but someone should hold this Object
        PIMOIDCUserProfile pimoidcUserProfile = pimUserManager.fetchOIDCUserProfile();
        if (pimoidcUserProfile != null) {

        }else{

        }
    }
}
