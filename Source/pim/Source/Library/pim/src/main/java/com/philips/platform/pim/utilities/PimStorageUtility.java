package com.philips.platform.pim.utilities;

import com.philips.platform.pim.models.OIDCConfig;
import com.philips.platform.pim.models.PimUserProfile;
import com.philips.platform.pim.rest.PimListener;

public class PimStorageUtility {
    public void storeUserProfileToSecureStorage(PimUserProfile pimUserProfile, PimListener listener) {

    }

    public PimUserProfile fetchUserProfileFromSecureStorage() {
        return new PimUserProfile();
    }

    public void storeOidcConfigration(OIDCConfig docJson, PimListener listener) {
//
    }

    public OIDCConfig fetchOIDCConfiguration() {
        return new OIDCConfig();
    }

    public void storeAccessTokenToSecureStorage(String accessToken, PimListener listener) {

    }

    public String fetchAccessTokenFromSecureStorage() {
        return "accessToken";
    }

}
