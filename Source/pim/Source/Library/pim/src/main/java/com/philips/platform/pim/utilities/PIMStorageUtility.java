package com.philips.platform.pim.utilities;

import com.philips.platform.pim.models.OIDCConfig;
import com.philips.platform.pim.models.PIMUserProfile;
import com.philips.platform.pim.rest.PIMListener;

public class PIMStorageUtility {
    public void storeUserProfileToSecureStorage(PIMUserProfile pimUserProfile, PIMListener listener) {

    }

    /*public PIMUserProfile fetchUserProfileFromSecureStorage() {
        return new PIMUserProfile();
    }*/

    public void storeOidcConfigration(OIDCConfig docJson, PIMListener listener) {
//
    }

    public OIDCConfig fetchOIDCConfiguration() {
        return new OIDCConfig();
    }

    public void storeAccessTokenToSecureStorage(String accessToken, PIMListener listener) {

    }

    public String fetchAccessTokenFromSecureStorage() {
        return "accessToken";
    }

}
