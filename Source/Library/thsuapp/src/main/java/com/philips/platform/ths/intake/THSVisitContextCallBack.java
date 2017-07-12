package com.philips.platform.ths.intake;

public interface THSVisitContextCallBack<THSVisitContext, THSSDKError> {

    public void onResponse(THSVisitContext pthVisitContext, THSSDKError pthsdkError);

    public void onFailure(Throwable throwable);

}
