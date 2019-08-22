package com.philips.cdp.di.ecs.error;

public class ECSErrorConstant {

    public enum AuthenticationError{
        UNKNOWN_ERROR("UnknownError",2999),
        INVALID_TOKEN("INVALID_TOKEN",2001);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        AuthenticationError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

    public enum ConfigError{
        UN_SUPPORTED_PROPOSITION("UnsupportedProposition",3001),
        UNSUPPORTED_LOCALE("UnsupportedLocale",3002),
        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",3003),
        UNKNOWN_ERROR("UnknownError",3999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        ConfigError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

    public enum FetchProductError{

        NO_PRODUCT_FOUND("NoProductFound",4001),
        UNKNOWN_ERROR("UnknownError",4999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        FetchProductError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }

    }

    public enum FetchProductDetailError{

        NO_PRODUCT_DETAIL_FOUND("NoProductDetailFound",5001),
        UNKNOWN_ERROR("UnknownError",5999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        FetchProductDetailError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }

    }

    public enum CreateCartError{

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",7001),
        UNKNOWN_ERROR("UnknownError",7999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        CreateCartError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }

    }

    public enum GetCartError{

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",8001),
        UNKNOWN_ERROR("UnknownError",7999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        GetCartError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }

    }

    public enum AddProductToCartError{

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",9001),
        SHOPPING_CART_NOT_CREATED("ShoppingCartNotCreated",9002),
        UNKNOWN_ERROR("UnknownError",9999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        AddProductToCartError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

    public enum UpdateQuantityError{

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",10001),
        SHOPPING_CART_NOT_CREATED("ShoppingCartNotCreated",10002),
        PRODUCT_OUT_OF_STOCK("ProductOutOfStock",10003),
        UNKNOWN_ERROR("UnknownError",10099);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        UpdateQuantityError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

    public enum CreateNewAddressError{

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",12001),
        ADDRESS_VALIDATION_ERROR("AddressValidationError",12002),
        UNKNOWN_ERROR("UnknownError",12999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        CreateNewAddressError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

    public enum UpdateAddressError{

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",13001),
        ADDRESS_VALIDATION_ERROR("AddressValidationError",13002),
        UNKNOWN_ERROR("UnknownError",13999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        UpdateAddressError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

    public enum GetListSavedAddressError{

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",14001),
        NO_SAVED_ADDRESS_FOUND("AddressValidationError",14002),
        UNKNOWN_ERROR("UnknownError",14999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        GetListSavedAddressError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

    public enum DeleteAddressError{

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",15001),
        ADDRESS_NOT_MATCHED("AddressNotMatched",15002),
        UNKNOWN_ERROR("UnknownError",15999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        DeleteAddressError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

    public enum SetDeliveryAddressError{

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",16001),
        ADDRESS_NOT_MATCHED("AddressNotMatched",16002),
        UNKNOWN_ERROR("UnknownError",16999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        SetDeliveryAddressError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }

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

    public enum GetPaymentError {

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",21001),
        NO_PAYMENT_METHOD_FOUND("NoPaymentMethodFound",21002),
        UNKNOWN_ERROR("UnknownError",21999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        GetPaymentError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }

    }

    public enum SetPaymentError {

        INVALID_HYBRIS_TOKEN("InvalidHybrisToken",22001),
        INVALID_PAYMENT_ID("InvalidPaymentID",22002),
        UNKNOWN_ERROR("UnknownError",22999);

        public String getErrorMessage() {
            return errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        private final String errorMessage;
        private final int errorCode;

        SetPaymentError(String errorMessage, int errorCode) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
        }
    }


}
