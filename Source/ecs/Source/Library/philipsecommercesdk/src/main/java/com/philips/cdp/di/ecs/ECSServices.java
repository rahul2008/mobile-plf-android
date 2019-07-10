package com.philips.cdp.di.ecs;

import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.AuthInput;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.cdp.di.ecs.integration.ECSListener;
import com.philips.cdp.di.ecs.integration.ECSServiceProvider;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.test.FetchConfiguration;
import com.philips.cdp.di.ecs.util.ECSUtil;
import com.philips.platform.appinfra.AppInfra;

import static com.philips.cdp.di.ecs.integration.ECSErrorReason.INITIALIZATION_FAILURE;

public class ECSServices implements ECSServiceProvider {

    private static ECSServices mECSServices;

    private ECSServices(ECSInput ecsInput, AppInfra appInfra) {
        ECSUtil.INSTANCE.setAppInfra(appInfra);
        ECSUtil.INSTANCE.setEcsInput(ecsInput);
    }

    /**
     * Initialize IAPSDKService. Once initialized IAPSDKService object is created and returned in iapsdkCallback.
     * All IAP service methods can be called only by this IAPSDKService object.
     *
     * @param ecsInput     the init params componentId, propositionId and locale
     * @param iapsdkCallback the iapsdk callback
     */
    public static void init(ECSInput ecsInput, @NonNull AppInfra appInfra, ECSCallback<ECSServices, Exception> iapsdkCallback) {
        ECSServices iapSdkService =null;
        if(isValidInput(ecsInput,appInfra)){  // if locale, propositionID are verified
            mECSServices=new ECSServices(ecsInput,appInfra);
            iapsdkCallback.onResponse(mECSServices);
        }else{
            iapsdkCallback.onFailure(new Exception(INITIALIZATION_FAILURE),9999);
        }

    }

    private static boolean isValidInput(ECSInput ecsInput, AppInfra appInfra) {
        return ecsInput.getLocale()!=null && appInfra!=null;
    }

    public void hybrisOathAuthentication(AuthInput authInput,ECSListener ecsListener){
        ECSUtil.INSTANCE.setEcsListener(ecsListener);
    }




    @Override
    public void getIAPConfig(ECSCallback<HybrisConfigResponse, Exception> eCSCallback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                new FetchConfiguration().fetchConfiguration(eCSCallback);
            }
        }).start();
    }


    @Override
    public void InvalidateECS(ECSCallback<Boolean,Exception> eCSCallback) {
        mECSServices=null;
        eCSCallback.onResponse(true);

    }
}
