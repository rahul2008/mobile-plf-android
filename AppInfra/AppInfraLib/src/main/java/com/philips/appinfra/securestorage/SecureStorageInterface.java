/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.appinfra.securestorage;

/**
 * Created by 310238114 on 4/11/2016.
 */
public  interface SecureStorageInterface {



    public boolean storeValueForKey(String userKey, String valueToBeEncrypted);
    public String fetchValueForKey(String userKey);
    public boolean removeValueForKey(String userKey);
}
