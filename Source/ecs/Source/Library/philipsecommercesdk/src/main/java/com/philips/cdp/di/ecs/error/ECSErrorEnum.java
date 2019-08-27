package com.philips.cdp.di.ecs.error;

import android.util.Log;

import com.philips.cdp.di.ecs.R;
import com.philips.cdp.di.ecs.util.ECSConfig;

public enum ECSErrorEnum {

    invalid_grant( R.string.invalid_grant, 5000),
    invalid_client(R.string.invalid_client, 5001),
    unsupported_grant_type( R.string.unsupported_grant_type, 5002),
    NoSuchElementError( R.string.NoSuchElementError, 5003),
   // hybristokenerror( R.string.hybristokenerror, 3001),
    CartError( R.string.CartError, 5004),
    InsufficientStockError( R.string.InsufficientStockError, 5005),
    UnknownIdentifierError( R.string.UnknownIdentifierError, 5006),
    CommerceCartModificationError( R.string.CommerceCartModificationError, 5007),
    CartEntryError( R.string.CartEntryError, 5008),
    InvalidTokenError( R.string.InvalidTokenError, 5009),
    UnsupportedVoucherError( R.string.UnsupportedVoucherError, 5010),
    VoucherOperationError( R.string.VoucherOperationError, 5011),
    ValidationError( R.string.ValidationError, 5012),
    UnsupportedDeliveryModeError( R.string.UnsupportedDeliveryModeError, 5013),
    regionisocode( R.string.regionisocode, 5014),
    countryisocode( R.string.countryisocode, 5015),
    postalCode( R.string.postalCode, 5016),
    firstName( R.string.firstName, 5017),
    lastName( R.string.lastName, 5018),
    phone1( R.string.phone1, 5019),
    phone2( R.string.phone2, 5020),
    addressId( R.string.addressId, 5021),
    sessionCart( R.string.sessionCart, 5022),
    postUrl( R.string.postUrl, 5023),
    IllegalArgumentError( R.string.IllegalArgumentError, 5024),
    InvalidPaymentInfoError( R.string.InvalidPaymentInfoError, 5025),



    //client error
    baseURL_notfound( R.string.baseURL_notfound, 5050),
    appInfra_notfound( R.string.appInfra_notfound, 5051),
    locale_notfound( R.string.locale_notfound, 5052),
    propositionId_notFound( R.string.propositionId_notFound, 5053),
    siteId_notfound( R.string.siteId_notfound, 5054),
    hybis_notAvailable( R.string.hybis_notAvailable, 5055),
    ctn_notProvided( R.string.ctn_notProvided, 5056),
    oauth_notCalled( R.string.oauth_notCalled, 5057),
    oauth_detail_error( R.string.oauth_detail_error, 5058),
    countryCodeNotGiven( R.string.countryCodeNotGiven, 5059),
    orderIdNil( R.string.orderIdNil, 5060),
    something_went_wrong( R.string.something_went_wrong, 5999);





    int resourceID;
    int errorCode;



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

    public  String getLocalizedErrorString(){
        String localizedError= ECSConfig.INSTANCE.getAppInfra().getAppInfraContext().getString(R.string.something_went_wrong);
        try{
            localizedError =   ECSConfig.INSTANCE.getAppInfra().getAppInfraContext().getResources().getString(getResourceID());
        } catch(Exception e){
            Log.e("RES_NOT_FOUND", e.getMessage());
        }
        return localizedError;
    }
}
