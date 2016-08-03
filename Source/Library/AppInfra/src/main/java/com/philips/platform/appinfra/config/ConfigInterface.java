package com.philips.platform.appinfra.config;

/**
 * Created by 310238114 on 7/25/2016.
 */
public interface ConfigInterface {

    /**
     * Gets property for key.
     *
     * @param groupName   the group name
     * @param key         the key
     * @param configError the config configError as OUT parameter
     * @return the value for key mapped by name, or null if no such mapping exists
     * if value in literal then 'String' Object is returned
     * if value in number then 'Integer' Object is returned
     * if value is array of literal then 'array of String' Object is returned
     * if value is array of number then 'array of Integer' Object is returned
     */
    Object getPropertyForKey(String groupName, String key, ConfigError configError);

    /**
     * Sets property for key.
     *
     * @param groupName   the group name
     * @param key         the key
     * @param object      the object (String/Integer/String[]/Integer[])
     * @param configError the configError object as OUT parameter
     * @return the set operation status (success/failure)
     */
    boolean setPropertyForKey(String groupName, String key, Object object, ConfigError configError);

    /**
     * The type Config error.
     */
    public class ConfigError {
        /**
         * The enum Config error enum.
         */
        public  enum  ConfigErrorEnum { InvalidKey, GroupNotExists ,KeyNotExists, ErrorKeyExists, FatalError, DeviceStoreError   , NoDataFoundForKey };


        private ConfigErrorEnum errorCode = null;

        /**
         * Gets error code.
         *
         * @return the error code
         */
        public ConfigErrorEnum getErrorCode() {
            return errorCode;
        }

        /**
         * Sets error code.
         *
         * @param errorCode the error code
         */
        void setErrorCode(ConfigErrorEnum errorCode) {
            this.errorCode = errorCode;
        }



    }
}
