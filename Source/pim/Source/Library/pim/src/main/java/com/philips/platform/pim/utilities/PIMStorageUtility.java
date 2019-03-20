package com.philips.platform.pim.utilities;

import com.philips.platform.pim.listeners.PIMStorageListener;
import com.philips.platform.pim.models.PIMOIDCUserProfile;

public class PIMStorageUtility {
    public void storeUserProfileToSecureStorage(PIMOIDCUserProfile pimUserProfile, PIMStorageListener listener) {

    }

    /*public PIMUserProfile fetchUserProfileFromSecureStorage() {
        return new PIMUserProfile();
    }*/

    public void storeOidcConfigration(PIMStorageListener listener) {
//
    }


    public void storeAccessTokenToSecureStorage(String accessToken, PIMStorageListener listener) {

    }

    public String fetchAccessTokenFromSecureStorage() {
        return "accessToken";
    }

}
