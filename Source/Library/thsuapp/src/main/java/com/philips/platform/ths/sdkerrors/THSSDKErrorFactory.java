package com.philips.platform.ths.sdkerrors;

import com.americanwell.sdk.entity.SDKErrorReason;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class THSSDKErrorFactory {

    static WeakReference<List> weakReference;


    public static String getErrorType(SDKErrorReason sdkErrorReason) {
        List<THSErrorHandlerInterface> errorList = null;
        if (weakReference != null) {
            errorList = weakReference.get();
        }
        if (errorList == null) {
            errorList = new ArrayList<>();
            addErrorTypes(errorList);
            weakReference = new WeakReference<List>(errorList);
        }

        for (THSErrorHandlerInterface thssdkUserError : errorList) {
            if (thssdkUserError.validate(sdkErrorReason)) {
                return thssdkUserError.getErrorMessage();
            }
        }

        return null;
    }

    static void addErrorTypes(List<THSErrorHandlerInterface> errorList) {
        errorList.add(new THSSDKServerError());
        errorList.add(new THSSDKUserError());
        errorList.add(new THSSDKSpecificError());
    }

}
