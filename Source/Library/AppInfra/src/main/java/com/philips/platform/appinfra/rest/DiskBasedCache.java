/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.rest;


import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.File;

/**
 * Cache implementation that caches files directly onto the hard disk in the specified
 * directory. The default disk usage size is 5MB, but is configurable.
 */
public class DiskBasedCache extends com.android.volley.toolbox.DiskBasedCache {

    private AppInfra mAppInfra;

    /**
     * Constructs an instance of the DiskBasedCache at the specified directory.
     *
     * @param rootDirectory       The root directory of the cache.
     * @param maxCacheSizeInBytes The maximum size of the cache in bytes.
     */
    public DiskBasedCache(File rootDirectory, int maxCacheSizeInBytes, AppInfra appInfra) {
        super(rootDirectory, maxCacheSizeInBytes);
        mAppInfra = appInfra;
    }

    /**
     * Returns the cache entry with the specified key if it exists, null otherwise.
     */
    @Override
    public synchronized Entry get(String key) {
        Entry e = super.get(key);
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI Rest Cache read ", key + " before decryption");
        SecureStorageInterface secureStorage = mAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        if (e != null)
            e.data = secureStorage.decryptData(e.data, sse);
        if (sse.getErrorCode() != null) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI Rest ", key + " response Decryption Error");
            return null;
        } else {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI Rest Cache read ", key + " after decryption");
        }
        return e;
    }

    /**
     * Puts the entry with the specified key into the cache.
     */
    @Override
    public synchronized void put(String key, Entry entry) {
        mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI Rest Cache write ", key + " before encryption");
        SecureStorageInterface secureStorage = mAppInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        entry.data = secureStorage.encryptData(entry.data, sse);

        if (sse.getErrorCode() != null) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI Rest ", key + " response Encryption Error");
        } else {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO, "AI Rest Cache write ", key + " after encryption");
            super.put(key, entry);
        }
    }
}
