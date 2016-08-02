package com.philips.platform.appinfra.config;

/**
 * Created by 310238114 on 7/25/2016.
 */
public interface ConfigInterface {

    Object getConfigurationPropertyForKey(String cocoName, String key,ConfigError configError);
    boolean setConfigurationPropertyForKey(String cocoName, String key, Object object,ConfigError configError);

    public class ConfigError {
        public  enum  ConfigErrorEnum { InvalidKey, CocoNotExists ,KeyNotExists, ErrorKeyExists, FatalError, DeviceStoreError   , NoDataFoundForKey };


        private ConfigErrorEnum errorCode = null;
        public ConfigErrorEnum getErrorCode() {
            return errorCode;
        }

        void setErrorCode(ConfigErrorEnum errorCode) {
            this.errorCode = errorCode;
        }



    }
}
