/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.localcache;

import android.content.Context;

import com.philips.cdp.prodreg.launcher.ProdRegUiHelper;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

public class ProdRegCache {

    private Context context;
    private String TAG = ProdRegCache.class.getSimpleName();

    public ProdRegCache(Context context) {
        this.context = context;
    }

    public void storeStringData(String key, String value) {
        SecureStorageInterface ssInterface = getAppInfraSecureStorageInterface();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        boolean result = ssInterface.storeValueForKey(key, value, ssError);
        if (null == ssError.getErrorCode() && result) {
            ProdRegLogger.v(TAG, "Data stored successfully");
        } else {
            ProdRegLogger.e(TAG, "Failed storing data due to" + ssError.getErrorCode().toString());
        }
    }

    public String getStringData(String key) {
        SecureStorageInterface ssInterface = getAppInfraSecureStorageInterface();
        SecureStorageInterface.SecureStorageError ssError = new SecureStorageInterface.SecureStorageError();
        String decryptedData = ssInterface.fetchValueForKey(key, ssError);
        if (null == ssError.getErrorCode() && null != decryptedData) {
            ProdRegLogger.v(TAG, "Data requested for key " + key + " is " + decryptedData);
        } else {
            ProdRegLogger.e(TAG, "Failed fetching data for key " + key + " is " + ssError.getErrorCode().toString());
        }
        return decryptedData;
    }

    public int getIntData(String key) {
        String decryptedData = getStringData(key);
        if (decryptedData != null)
            return Integer.parseInt(decryptedData);
        else return 0;
    }

    private SecureStorageInterface getAppInfraSecureStorageInterface() {
        return ProdRegUiHelper.getInstance().getAppInfraSecureStorageInterface();
    }
}
