package com.philips.platform.ths.intake;


public interface THSVitalSDKCallback<THSVitals, PTHSDKError> {
        void onResponse(THSVitals var1, PTHSDKError var2);

        void onFailure(Throwable var1);
}
