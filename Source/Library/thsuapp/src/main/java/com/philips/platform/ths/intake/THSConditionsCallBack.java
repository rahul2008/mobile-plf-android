package com.philips.platform.ths.intake;

public interface THSConditionsCallBack<THSCondition,PTHSDKError> {
    void onResponse(THSCondition var1, PTHSDKError var2);
    void onFailure(Throwable throwable);
}
