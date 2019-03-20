package com.philips.platform.pim.manager;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pim.models.PIMHsdpUserProfile;
import com.philips.platform.pim.models.PIMOIDCUserProfile;

public class PIMUserManager {
    private PIMOIDCUserProfile pimoidcUserProfile;
    private PIMHsdpUserProfile pimHsdpUserProfile;

    public PIMUserManager() {

    }

    public void init(SecureStorageInterface secureStorage) {
        //get Secure Storage user profile
        pimoidcUserProfile = getUserProfileFromSecureStorage(secureStorage);
    }


    public PIMOIDCUserProfile fetchOIDCUserProfile() {
        return pimoidcUserProfile;
    }

    protected PIMHsdpUserProfile fetchHSDPUserProfile() {
        return pimHsdpUserProfile;
    }

    private PIMOIDCUserProfile getUserProfileFromSecureStorage(SecureStorageInterface secureStorage) {
        secureStorage.fetchValueForKey("ProfileKey", new SecureStorageInterface.SecureStorageError());
        return null;
    }

}
