package com.philips.plataform.mya.model.utils;

/**
 * Created by Maqsood on 10/13/17.
 */

public class ConsentUtil {
    public static final int maxRetries = 0;
    public static final int requestTimeOut = 5000;
    public final static int DEFAULT_TIMEOUT_MS = 30000;
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

}
