package com.philips.cdp.registration.configuration;

public class HSDPInfo {


    private String sharedId;
    private String secretId;
    private String baseURL;
    private String applicationName;

    /**
     * Get Shared Id
     *
     * @return String shared id
     */
    public String getSharedId() {
        return sharedId;
    }

    /**
     * Set shared id
     *
     * @param sharedId String
     */
    public void setSharedId(String sharedId) {
        this.sharedId = sharedId;
    }

    /**
     * Get secret id
     *
     * @return String
     */
    public String getSecretId() {
        return secretId;
    }

    /**
     * Set secrete id
     *
     * @param secretId String
     */
    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    /**
     * Get base URL
     *
     * @return String
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * Set base URL
     *
     * @param baseURL
     */
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Get Application name
     *
     * @return String Application
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Set application name
     *
     * @param applicationName
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}



