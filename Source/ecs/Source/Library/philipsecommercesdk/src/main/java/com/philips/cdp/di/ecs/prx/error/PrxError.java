package com.philips.cdp.di.ecs.prx.error;

/**
 *  Prx Error Handle Class
 *
 */
public class PrxError {
    private final String description;
    private final int statusCode;

    public PrxError(String description, int statusCode) {
        this.description = description;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }

    public enum PrxErrorType {

        TIME_OUT(504, "Time out Exception"),
        UNKNOWN_EXCEPTION(-1, "Unknown exception"),
        NO_INTERNET_CONNECTION(9, "No internet connection"),
        NOT_FOUND(404, "The requested file was not found"),

        AUTHENTICATION_FAILURE(401, "Authentication failure when performing a Request"),
        NETWORK_ERROR(511, "Network error when performing a request"),
        PARSE_ERROR(1, "Indicates that the server's response could not be parsed"),
        INJECT_APPINFRA(3 , "You must inject AppInfra into PRX"),
        SERVER_ERROR(2, "Indicates that the error responded with an error response.");

        private final int id;
        private final String description;

        PrxErrorType(final int id, final String description) {
            this.description = description;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

    }
}