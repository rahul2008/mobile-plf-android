package com.philips.platform.catk;

public class CatkConstants {
    /**
     * It is added to get coverge of constants usage
     */
    public CatkConstants() {
    }

    public static final int DEFAULT_TIMEOUT_MS = 30000;
    public static final String EMPTY_RESPONSE = "";
    public static final int CONSENT_SUCCESS = 0;
    public static final int CONSENT_ERROR = -1;
    public static final int CONSENT_ERROR_NO_CONNECTION = 2;
    public static final int CONSENT_ERROR_CONNECTION_TIME_OUT = 3;
    public static final int CONSENT_ERROR_AUTHENTICATION_FAILURE = 4;
    public static final int CONSENT_ERROR_SERVER_ERROR = 5;
    public static final int CONSENT_ERROR_INSUFFICIENT_STOCK_ERROR = 6;
    public static final int CONSENT_ERROR_UNKNOWN = 7;

    public static final String APPLICATION_NAME = "OneBackend";
    public static final String PROPOSITION_NAME = "OneBackendProp";

    public static final String BUNDLE_KEY_APPLICATION_NAME = "appName";
    public static final String BUNDLE_KEY_PROPOSITION_NAME = "propName";
}
