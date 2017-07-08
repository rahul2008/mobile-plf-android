package com.philips.platform.ths.intake;

public interface THSVisitContextCallBack<PTHVisitContext, PTHSDKError> {

    public void onResponse(PTHVisitContext pthVisitContext, PTHSDKError pthsdkError);

    public void onFailure(Throwable throwable);

}
