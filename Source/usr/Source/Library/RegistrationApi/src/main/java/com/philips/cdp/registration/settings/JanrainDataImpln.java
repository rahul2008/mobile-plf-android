package com.philips.cdp.registration.settings;

import com.janrain.android.StorageInterface;
import com.janrain.android.ServerTimeInterface;
import com.philips.cdp.registration.ui.utils.ServerTime;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

public class JanrainDataImpln implements StorageInterface, ServerTimeInterface {

    private SecureStorageInterface mSecureStorageInterface;

    public JanrainDataImpln(SecureStorageInterface secureStorageInterface){
        mSecureStorageInterface = secureStorageInterface;
    }

    @Override
    public String getCurrentUTCTimeWithFormat(String DATE_FORMAT_FOR_JUMP) {
        return ServerTime.getCurrentUTCTimeWithFormat(DATE_FORMAT_FOR_JUMP);
    }

    @Override
    public String fetchValueForKey(String userKey) {
        return mSecureStorageInterface.fetchValueForKey(userKey, new SecureStorageInterface.SecureStorageError());
    }

    @Override
    public void storeValueForKey(String userKey, String valueToBeEncrypted) {
        mSecureStorageInterface.storeValueForKey(userKey, valueToBeEncrypted, new SecureStorageInterface.SecureStorageError());
    }

    @Override
    public void removeValueForKey(String userKey) {
        mSecureStorageInterface.removeValueForKey(userKey);
    }
}
