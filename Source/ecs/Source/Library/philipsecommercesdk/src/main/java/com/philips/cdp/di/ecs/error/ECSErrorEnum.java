package com.philips.cdp.di.ecs.error;

import com.philips.cdp.di.ecs.R;

public enum ECSErrorEnum {

    invalid_grant( R.string.ail_cloud_consent_help, 3001),
    invalid_client( R.string.ail_cloud_consent_help, 3001),
    unsupported_grant_type( R.string.ail_cloud_consent_help, 3001),
    NoSuchElementError( R.string.ail_cloud_consent_help, 3001),
    hybristokenerror( R.string.ail_cloud_consent_help, 3001),
    CartError( R.string.ail_cloud_consent_help, 3001),
    InsufficientStockError( R.string.ail_cloud_consent_help, 3001),
    UnknownIdentifierError( R.string.ail_cloud_consent_help, 3001),
    CommerceCartModificationError( R.string.ail_cloud_consent_help, 3001),
    CartEntryError( R.string.ail_cloud_consent_help, 3001),
    InvalidTokenError( R.string.ail_cloud_consent_help, 3001),
    UnsupportedVoucherError( R.string.ail_cloud_consent_help, 3001),
    VoucherOperationError( R.string.ail_cloud_consent_help, 3001),
    ValidationError( R.string.ail_cloud_consent_help, 3001),
    UnsupportedDeliveryModeError( R.string.ail_cloud_consent_help, 3001),
    regionisocode( R.string.ail_cloud_consent_help, 3001),
    countryisocode( R.string.ail_cloud_consent_help, 3001),
    postalCode( R.string.ail_cloud_consent_help, 3001),
    firstName( R.string.ail_cloud_consent_help, 3001),
    lastName( R.string.ail_cloud_consent_help, 3001),
    phone1( R.string.ail_cloud_consent_help, 3001),
    phone2( R.string.ail_cloud_consent_help, 3001),
    addressId( R.string.ail_cloud_consent_help, 3001),
    sessionCart( R.string.ail_cloud_consent_help, 3001),
    postUrl( R.string.ail_cloud_consent_help, 3001),
    IllegalArgumentError( R.string.ail_cloud_consent_help, 3001),
    InvalidPaymentInfoError( R.string.ail_cloud_consent_help, 3001),



    //client error
    baseURL_notfound( R.string.ail_cloud_consent_help, 3002),
    appInfra_notfound( R.string.ail_cloud_consent_help, 3002),
    locale_notfound( R.string.ail_cloud_consent_help, 3002),
    propositionId_notFound( R.string.ail_cloud_consent_help, 3002),
    siteId_notfound( R.string.ail_cloud_consent_help, 3002),
    hybis_notAvailable( R.string.ail_cloud_consent_help, 3002),
    ctn_notProvided( R.string.ail_cloud_consent_help, 3002),
    oauth_notCalled( R.string.ail_cloud_consent_help, 3002),
    oauth_detail_error( R.string.ail_cloud_consent_help, 3002),
    countryCodeNotGiven( R.string.ail_cloud_consent_help, 3002),
    orderIdNil( R.string.ail_cloud_consent_help, 3002),
    something_went_wrong( R.string.ail_cloud_consent_help, 3002),
    unknown( R.string.ail_cloud_consent_help, 3002);



    String errorType;
    int resourceID;
    int errorCode;

    public String getErrorType() {
        return errorType;
    }

    public int getResourceID() {
        return resourceID;
    }


    public int getErrorCode() {
        return errorCode;
    }

    ECSErrorEnum( int resourceID, int errorCode) {
        this.resourceID = resourceID;
        this.errorCode = errorCode;
    }


    public static ECSErrorEnum getErrorEnumFromType(String errorType) {
        return ECSErrorEnum.valueOf(errorType);
    }
}
