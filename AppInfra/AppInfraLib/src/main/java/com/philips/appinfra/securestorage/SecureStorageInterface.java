package com.philips.appinfra.securestorage;

/**
 * Created by 310238114 on 4/11/2016.
 */
public  interface SecureStorageInterface {



    public void storeValueForKey(String userKey, String valueToBeEncrypted);
    public String fetchValueForKey(String userKey);
    public boolean RemoveValueForKey(String userKey);
}
