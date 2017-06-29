package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.visit.VisitContext;

public interface PTHVisitContextCallBack<PTHVisitContext, PTHSDKError> {

    public void onResponse(PTHVisitContext pthVisitContext, PTHSDKError pthsdkError);

    public void onFailure(Throwable throwable);

}
