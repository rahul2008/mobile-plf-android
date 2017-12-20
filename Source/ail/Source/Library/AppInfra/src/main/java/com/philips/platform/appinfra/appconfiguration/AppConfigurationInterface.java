/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.appconfiguration;

import java.io.Serializable;

/**
 * The App Configuration Interface .
 */
public interface AppConfigurationInterface extends Serializable {

    /**
     * Gets property for key.
     *
     * @param key         the group name
     * @param group       the key
     * @param configError the config configError as OUT parameter
     * @return the value for key mapped by name, or null if no such mapping exists
     * if value in literal then 'String' Object is returned
     * if value in number then 'Integer' Object is returned
     * if value is array of literal then 'array of String' Object is returned
     * if value is array of number then 'array of Integer' Object is returned
     * if value is Map<String,String> then 'HashMap<String,String>' is returned
     * if value is Map<String,Integer> then 'HashMap<String,Integer>' is returned
     * @since 1.0.0
     */
    Object getPropertyForKey(String key, String group, AppConfigurationError configError) throws
            IllegalArgumentException;

    /**
     * Sets property for key.
     *
     * @param key         the group name
     * @param group       the key
     * @param object      the object (String/Integer/String[]/Integer[]/null/HashMap<String,String>
     *                    /HashMap<String,Integer></>)   null to be passed to delete key
     * @param configError the configError object as OUT parameter
     * @return the set operation status (success/failure)
     * @since 1.0.0
     */
    boolean setPropertyForKey(String key, String group, Object object, AppConfigurationError
            configError)
            throws IllegalArgumentException;


    /**
     * Gets property for key from static json file.
     *
     * @param key         the group name
     * @param group       the key
     * @param configError the config configError as OUT parameter
     * @return the value for key mapped by name, or null if no such mapping exists
     * if value in literal then 'String' Object is returned
     * if value in number then 'Integer' Object is returned
     * if value is array of literal then 'array of String' Object is returned
     * if value is array of number then 'array of Integer' Object is returned
     * @since 1.0.0
     */
    Object getDefaultPropertyForKey(String key, String group, AppConfigurationError configError)
            throws IllegalArgumentException;


    /**
     * download app config file from cloud
     * @since 2.1.0
     */
    void refreshCloudConfig(OnRefreshListener onRefreshListener);

    /**
     * This call back method of OnResultListener, if refersh failure it will calback to onError,if success it will callback to onSuccess
     * @since 2.1.0
     */
    interface OnRefreshListener {
        enum REFRESH_RESULT { REFRESHED_FROM_SERVER, NO_REFRESH_REQUIRED,REFRESHED_FAILED};
        void onError(AppConfigurationError.AppConfigErrorEnum error, String message);
        void onSuccess(REFRESH_RESULT result);
    }

    /**
     * This method clears dynamic config and downloaded cloud config
     * from both local cache and persistence memory.
     * @since 2.2.0
     */
    void resetConfig();

    /**
     * The type Config error.
     */
    class AppConfigurationError {
        /**
         * The enum Config error enum.
         */
        public enum AppConfigErrorEnum {
            NoError, FatalError, InvalidKey, NoDataFoundForKey, GroupNotExists, KeyNotExists,
            DeviceStoreError, ServerError
            ,DownloadInProgress  , SecureStorageError
        }

        private AppConfigErrorEnum errorCode = null;

        /**
         * Gets error code.
         *
         * @return the error code
         */
        public AppConfigErrorEnum getErrorCode() {
            return errorCode;
        }

        /**
         * Sets error code.
         *
         * @param errorCode the error code
         */
        void setErrorCode(AppConfigErrorEnum errorCode) {
            this.errorCode = errorCode;
        }

    }
}
