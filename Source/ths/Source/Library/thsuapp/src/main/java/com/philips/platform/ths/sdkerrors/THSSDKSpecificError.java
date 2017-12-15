package com.philips.platform.ths.sdkerrors;


import com.americanwell.sdk.entity.SDKErrorReason;

public class THSSDKSpecificError implements THSErrorHandlerInterface {

    String errorMessage = "";
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean validate(SDKErrorReason sdkErrorReason) {
        if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.ONDEMAND_SPECIALTY_NOT_FOUND.name())){
            errorMessage = "onDemand provider unavailable";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.PRIVACY_DISCLAIMER_MISSING.name())){
            errorMessage = "privacy disclaimer missing";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_MEMBER_UNDERAGE.name())){
            errorMessage = "Customer underage";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_EMAIL_IN_USE.name())){
            errorMessage = "Email is already in use";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_REQ_PARAM_TOO_SHORT.name())){
            errorMessage = "Search string less than 3 chars";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_COORDINATE_FORMAT.name())){
            errorMessage = "Improperly formatted longitude and/or latitude";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_INTEGER_FORMAT.name())){
            errorMessage = "Improperly formatted value for radius";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.MEMBER_PRIMARY_PHARMACY_NOT_FOUND.name())){
            errorMessage = "Primary pharmacy not found";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_MISSING.name())){
            errorMessage = "Credit card missing";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_INVALID_COUPON.name())){
            errorMessage = "Invalid coupon code";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.PROVIDER_NOT_AVAILABLE.name())){
            errorMessage = "Provider not available";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.WAITING_ROOM_ACCESS_DENIED.name())){
            errorMessage = "Provider does not accept waiting room requests from consumer";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.PROVIDER_NOT_LICENSED_FOR_MEMBER_STATE.name())){
            errorMessage = "Provider is not licensed for consumer’s state";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_VALIDATION_ERROR.name())){
            errorMessage = "Invalid credit card";
            return true;
        }
        else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_INCORRECT_CVV.name())){
            errorMessage = "Invalid CVV number";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_DECLINED_ERROR.name())){
            errorMessage = "Credit card declined";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_INVALID_ZIP.name())){
            errorMessage = "Invalid zip number";
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_RESIDENCY_CHECK_FAILED.name())){
            errorMessage = "Credit card residency check failed";
            return true;
        } else if (sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.ENG_SCHEDULING_FAILURE.name())) {
            errorMessage = "This appointment slot is no longer available.  Please select a new time";
            return true;
        } else if (sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.ENG_USER_ALREADY_ACTIVE.name())) {
            errorMessage = "The consumer is already active in a visit. End the active visit and try again";
            return true;
        } else {
            return false;
        }

    }
}
