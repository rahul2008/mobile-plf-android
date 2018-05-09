package com.philips.platform.ths.sdkerrors;


import android.content.Context;

import com.americanwell.sdk.entity.SDKErrorReason;
import com.philips.platform.ths.R;

public class THSSDKSpecificError implements THSErrorHandlerInterface {

    String errorMessage = "";
    protected Context context;
    @Override
    public String getErrorMessage(Context context) {
        this.context = context;
        return errorMessage;
    }

    @Override
    public boolean validate(String sdkErrorReason, Context context) {
        if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.ONDEMAND_SPECIALTY_NOT_FOUND)){
            errorMessage = context.getString(R.string.ths_on_demand_provider_unavailable);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.PRIVACY_DISCLAIMER_MISSING)){
            errorMessage = context.getString(R.string.ths_privacy_disclaimer_missing);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_CONSUMER_UNDERAGE)){
            errorMessage = context.getString(R.string.ths_customer_underage);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_EMAIL_IN_USE)){
            errorMessage = context.getString(R.string.ths_email_already_in_use);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_REQ_PARAM_TOO_SHORT)){
            errorMessage = context.getString(R.string.ths_search_string_less_than_three_chars);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_COORDINATE_FORMAT)){
            errorMessage = context.getString(R.string.ths_improper_format_latitude);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_INTEGER_FORMAT)){
            errorMessage = context.getString(R.string.ths_improper_radius_format);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.CONSUMER_PRIMARY_PHARMACY_NOT_FOUND)){
            errorMessage = context.getString(R.string.ths_primary_pharmacy_not_found);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_MISSING)){
            errorMessage = context.getString(R.string.ths_credit_card_missing);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_INVALID_COUPON)){
            errorMessage = context.getString(R.string.ths_invalid_coupon_code);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.PROVIDER_NOT_AVAILABLE)){
            errorMessage = context.getString(R.string.ths_provider_not_available);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.WAITING_ROOM_ACCESS_DENIED)){
            errorMessage = context.getString(R.string.ths_provider_not_accepting_waiting_consumer);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.PROVIDER_NOT_LICENSED_FOR_CONSUMER_STATE)){
            errorMessage = context.getString(R.string.ths_provider_not_licensed);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_VALIDATION_ERROR)){
            errorMessage = context.getString(R.string.ths_invalid_credit_card);
            return true;
        }
        else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_INCORRECT_CVV)){
            errorMessage = context.getString(R.string.ths_invalid_cvv_number);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_DECLINED_ERROR)){
            errorMessage = context.getString(R.string.ths_credit_card_declined);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_INVALID_ZIP)){
            errorMessage = context.getString(R.string.ths_invalid_zip_number);
            return true;
        }else if(sdkErrorReason.equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_RESIDENCY_CHECK_FAILED)){
            errorMessage = context.getString(R.string.ths_residency_check_failed);
            return true;
        } else if (sdkErrorReason.equalsIgnoreCase(SDKErrorReason.ENG_SCHEDULING_FAILURE)) {
            errorMessage = context.getString(R.string.ths_slot_no_longer_available);
            return true;
        } else if (sdkErrorReason.equalsIgnoreCase(SDKErrorReason.ENG_USER_ALREADY_ACTIVE)) {
            errorMessage = context.getString(R.string.ths_consumer_already_active);
            return true;
        } else if (sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_REQ_PARAM_INVALID)) {
            errorMessage = context.getString(R.string.ths_invalid_parameter);
            return true;
        }else if (sdkErrorReason.equalsIgnoreCase(SDKErrorReason.VALIDATION_ELIG_EXCEPTION)) {
            errorMessage = context.getString(R.string.ths_insurance_invalid_subscription_info);
            return true;
        } else {
            return false;
        }

    }
}
