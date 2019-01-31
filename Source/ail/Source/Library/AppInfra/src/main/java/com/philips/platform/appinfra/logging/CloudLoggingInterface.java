package com.philips.platform.appinfra.logging;

/**
 * The CloudLogging Interface
 */
public interface CloudLoggingInterface extends LoggingInterface {
    /**
     * To identify log originated from which user set/reset when user login/logout can be empty (will not be able to track based on user)
     *
     * @param userUUID
     * @since 1901.0
     */
    void setHSDPUserUUID(String userUUID);

    /**
     * @return This method returns the Cloud Logging consent identifier
     * @since 1901.0
     */
    String getCloudLoggingConsentIdentifier();
}
