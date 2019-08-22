package com.philips.cdp.di.ecs.error;

public class ECSErrorConstant {

    //Get Delivery Mode Errors
    public enum GetDeliveryModeError {
        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",17001),
        NO_DELIVERY_MODES_FOUND("InvalidHybrisToken",17002),
        UNKNOWN_ERROR("UnknownError",17999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        GetDeliveryModeError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

    //Set Delivery Mode Errors
    public enum SetDeliveryModeError {
        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",18001),
        DELIVERY_MODES_NOT_MATCHED("DeliveryModesNotMatched",18002),
        UNKNOWN_ERROR("UnknownError",18999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        SetDeliveryModeError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }


}
