package com.philips.platform.appinfra.rest.hpkp;

import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.HashMap;

class HPKPLoggingHelper {

    static final String LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE = "Mismatch of stored pinned Public-key with certificate signature";
    static final String LOG_MESSAGE_PUBLIC_KEY_PIN_EXPIRED = "Stored pinned Public-key matching header pinned Public-key is expired";
    static final String LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_HEADER = "Mismatch of stored pinned Public-key with response header pinned Public-key";
    static final String LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK = "Could not find Public-Key-Pins in network response";
    static final String LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE = "Could not find Public-Key-Pins in storage";
    static final String LOG_MESSAGE_STORAGE_ERROR = "Could not update Public-Key-Pins in Secure Storage";

    private static final String LOG_MESSAGE_BASE = "Public-key pins Mismatch";
    private static final int PIN_MISMATCH_LOG_MAX_COUNT = 3;
    private HashMap<String, Integer> pinMismatchLogCount;
    private LoggingInterface loggingInterface;

    HPKPLoggingHelper(LoggingInterface loggingInterface) {
        this.loggingInterface = loggingInterface;
        pinMismatchLogCount = new HashMap<>();
    }

    void logError(String hostname, String message) {
        if (pinMismatchLogCount.containsKey(hostname)) {
            int count = pinMismatchLogCount.get(hostname);
            if (count <= PIN_MISMATCH_LOG_MAX_COUNT) {
                log(hostname, message, LoggingInterface.LogLevel.ERROR);
                pinMismatchLogCount.put(hostname, count + 1);
            }
        } else {
            log(hostname, message, LoggingInterface.LogLevel.ERROR);
            pinMismatchLogCount.put(hostname, 1);
        }
    }

    private void log(String hostname, String message, LoggingInterface.LogLevel logLevel) {
        loggingInterface.log(logLevel, HPKPManager.class.getSimpleName(), LOG_MESSAGE_BASE + ":" + hostname + ":" + message);
    }

    void logDebug(String hostname, String message) {
        log(hostname, message, LoggingInterface.LogLevel.DEBUG);
    }
}
