package com.philips.platform.pim.manager;

import android.util.Log;

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
        SecureStorageInterface.SecureStorageError secureStorageError = new SecureStorageInterface.SecureStorageError();
        String profile = secureStorage.fetchValueForKey("ProfileKey", secureStorageError);
        SecureStorageInterface.SecureStorageError.secureStorageError errorCode = secureStorageError.getErrorCode();
        if (profile != null && secureStorageError.getErrorMessage() != null) {
            Log.e("PimUserManager", "Error Occured" + secureStorageError.getErrorMessage());
            return null;
        } else
            return null;
    }

}
