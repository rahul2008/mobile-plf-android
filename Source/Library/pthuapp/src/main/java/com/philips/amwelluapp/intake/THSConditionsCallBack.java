package com.philips.amwelluapp.intake;

import java.util.List;

public interface THSConditionsCallBack<THSCondition,PTHSDKError> {
    void onResponse(THSCondition var1, PTHSDKError var2);
    void onFailure(Throwable throwable);
}
