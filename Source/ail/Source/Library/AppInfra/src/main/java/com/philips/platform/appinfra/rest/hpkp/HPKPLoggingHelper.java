package com.philips.platform.appinfra.rest.hpkp;

import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.HashMap;

class HPKPLoggingHelper {

    static final String LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE_EXPIRED = "Mismatch of certificate signature with stored pinned Public-key due to expiry";
    static final String LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE = "Mismatch of certificate signature with stored pinned Public-key";
    static final String LOG_MESSAGE_PUBLIC_KEY_PIN_CERTIFICATE_EXPIRED = "Certificate signature matching the Stored pinned Public-key is expired";
    static final String LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_HEADER = "Pinned Public-key received in response header does not match with stored value of pinned Public-key";
    static final String LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK = "Could not find Public-Key-Pins in network response";
    static final String LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE = "Could not find Public-Key-Pins in storage";
    static final String LOG_MESSAGE_STORAGE_ERROR = "Could not update Public-Key-Pins in Secure Storage";

    private static final String LOG_MESSAGE_BASE = "Public-key pins Mismatch";
    private static final String LOG_MAP_KEY = "hostname";
    private static final int PIN_MISMATCH_LOG_MAX_COUNT = 3;
    private HashMap<String, Integer> pinMismatchLogCount;
    private LoggingInterface loggingInterface;

    HPKPLoggingHelper(LoggingInterface loggingInterface) {
        this.loggingInterface = loggingInterface;
        pinMismatchLogCount = new HashMap<>();
    }

    void logError(String hostname, String message) {
        boolean entryExistsForHost = pinMismatchLogCount.containsKey(hostname);
        int count = entryExistsForHost ? pinMismatchLogCount.get(hostname) : 0;
        if (count <= PIN_MISMATCH_LOG_MAX_COUNT) {
            log(hostname, message, LoggingInterface.LogLevel.ERROR);
            pinMismatchLogCount.put(hostname, count + 1);
        }
    }

    void logDebug(String hostname, String message) {
        log(hostname, message, LoggingInterface.LogLevel.DEBUG);
    }

    private void log(String hostname, String message, LoggingInterface.LogLevel logLevel) {
        HashMap<String, String> map = new HashMap<>();
        map.put(LOG_MAP_KEY, hostname);
        loggingInterface.log(logLevel, LOG_MESSAGE_BASE, message, map);
    }
}
