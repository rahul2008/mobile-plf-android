package com.philips.platform.ths.sdkerrors;

import com.americanwell.sdk.entity.SDKErrorReason;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SERVER_ERROR;
import static com.philips.platform.ths.utility.THSConstants.THS_USER_ERROR;

public class THSSDKErrorFactory {

    static WeakReference<List> weakReference;


    public static String getErrorType(SDKErrorReason sdkErrorReason) {
        List<THSErrorHandlerInterface> errorList = null;
        String errorMessage=null;
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
                errorMessage=thssdkUserError.getErrorMessage();
                final String errorTag=THSTagUtils.createErrorTag("",errorMessage);
                if(thssdkUserError instanceof THSSDKServerError){ // server or technical error
                    THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA ,THS_SERVER_ERROR,errorTag);
                }else{// user or specific error
                    THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA ,THS_USER_ERROR,errorTag);
                }
                break;
            }
        }

        return errorMessage;
    }

    static void addErrorTypes(List<THSErrorHandlerInterface> errorList) {
        errorList.add(new THSSDKServerError());
        errorList.add(new THSSDKUserError());
        errorList.add(new THSSDKSpecificError());
    }

}
