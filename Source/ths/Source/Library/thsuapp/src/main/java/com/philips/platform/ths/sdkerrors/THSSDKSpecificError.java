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
    public boolean validate(SDKErrorReason sdkErrorReason, Context context) {
        if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.ONDEMAND_SPECIALTY_NOT_FOUND.name())){
            errorMessage = context.getString(R.string.ths_on_demand_provider_unavailable);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.PRIVACY_DISCLAIMER_MISSING.name())){
            errorMessage = context.getString(R.string.ths_privacy_disclaimer_missing);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_MEMBER_UNDERAGE.name())){
            errorMessage = context.getString(R.string.ths_customer_underage);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_EMAIL_IN_USE.name())){
            errorMessage = context.getString(R.string.ths_email_already_in_use);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_REQ_PARAM_TOO_SHORT.name())){
            errorMessage = context.getString(R.string.ths_search_string_less_than_three_chars);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_COORDINATE_FORMAT.name())){
            errorMessage = context.getString(R.string.ths_improper_format_latitude);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_BAD_INTEGER_FORMAT.name())){
            errorMessage = context.getString(R.string.ths_improper_radius_format);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.MEMBER_PRIMARY_PHARMACY_NOT_FOUND.name())){
            errorMessage = context.getString(R.string.ths_primary_pharmacy_not_found);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_MISSING.name())){
            errorMessage = context.getString(R.string.ths_credit_card_missing);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.VALIDATION_INVALID_COUPON.name())){
            errorMessage = context.getString(R.string.ths_invalid_coupon_code);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.PROVIDER_NOT_AVAILABLE.name())){
            errorMessage = context.getString(R.string.ths_provider_not_available);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.WAITING_ROOM_ACCESS_DENIED.name())){
            errorMessage = context.getString(R.string.ths_provider_not_accepting_waiting_consumer);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.PROVIDER_NOT_LICENSED_FOR_MEMBER_STATE.name())){
            errorMessage = context.getString(R.string.ths_provider_not_licensed);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_VALIDATION_ERROR.name())){
            errorMessage = context.getString(R.string.ths_invalid_credit_card);
            return true;
        }
        else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_INCORRECT_CVV.name())){
            errorMessage = context.getString(R.string.ths_invalid_cvv_number);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_DECLINED_ERROR.name())){
            errorMessage = context.getString(R.string.ths_credit_card_declined);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_INVALID_ZIP.name())){
            errorMessage = context.getString(R.string.ths_invalid_zip_number);
            return true;
        }else if(sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_RESIDENCY_CHECK_FAILED.name())){
            errorMessage = context.getString(R.string.ths_residency_check_failed);
            return true;
        } else if (sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.ENG_SCHEDULING_FAILURE.name())) {
            errorMessage = context.getString(R.string.ths_slot_no_longer_available);
            return true;
        } else if (sdkErrorReason.name().equalsIgnoreCase(SDKErrorReason.ENG_USER_ALREADY_ACTIVE.name())) {
            errorMessage = context.getString(R.string.ths_consumer_already_active);
            return true;
        } else {
            return false;
        }

    }
}
