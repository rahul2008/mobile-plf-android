package com.philips.platform.appinfra.logging;

public interface CloudLoggingInterface extends LoggingInterface {
    /**
     * To identify log originated from which user set/reset when user login/logout can be empty (will not be able to track based on user)
     *
     * @param userUUID
     * @since 1802.0.0
     */
    void setHSDPUserUUID(String userUUID);

    /**
     * @return This method returns the Cloud Logging consent identifier
     * @since 1802.0
     */
    String getCloudLoggingConsentIdentifier();
}
