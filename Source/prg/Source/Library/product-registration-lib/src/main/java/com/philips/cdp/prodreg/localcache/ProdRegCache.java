/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.localcache;

import androidx.annotation.NonNull;

import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

public class ProdRegCache {

    public boolean storeStringData(String key, String value) {
        SecureStorageInterface ssInterface = getAppInfraSecureStorageInterface();
        SecureStorageInterface.SecureStorageError ssError = getSecureStorageError();
        return ssInterface.storeValueForKey(key, value, ssError);
    }

    public String getStringData(String key) {
        SecureStorageInterface ssInterface = getAppInfraSecureStorageInterface();
        SecureStorageInterface.SecureStorageError ssError = getSecureStorageError();
        if (ssInterface != null)
            return ssInterface.fetchValueForKey(key, ssError);

        return null;
    }

    @NonNull
    public SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

    public int getIntData(String key) {
        String decryptedData = getStringData(key);
        if (decryptedData != null)
            return Integer.parseInt(decryptedData);
        else return 0;
    }

    public SecureStorageInterface getAppInfraSecureStorageInterface() {
        return PRUiHelper.getInstance().getAppInfraSecureStorageInterface();
    }
}
