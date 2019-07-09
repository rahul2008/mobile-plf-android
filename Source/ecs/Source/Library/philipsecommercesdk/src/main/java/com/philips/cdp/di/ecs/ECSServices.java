package com.philips.cdp.di.ecs;

import com.philips.cdp.di.ecs.integration.AuthInput;
import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.cdp.di.ecs.integration.ECSListener;
import com.philips.cdp.di.ecs.integration.ECSSDKCallback;
import com.philips.cdp.di.ecs.integration.ECSServiceProvider;
import com.philips.cdp.di.ecs.test.FetchConfiguration;
import com.philips.cdp.di.ecs.util.ECSUtil;
import com.philips.platform.appinfra.AppInfra;

import java.util.Map;

public class ECSServices implements ECSServiceProvider {


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
    public static void init(ECSInput ecsInput, AppInfra appInfra, ECSSDKCallback<ECSServices, Exception> iapsdkCallback) {
        ECSServices iapSdkService =null;
        if(isValidInput(ecsInput,appInfra)){  // if locale, propositionID are verified
            iapSdkService=new ECSServices(ecsInput,appInfra);
            iapsdkCallback.onResponse(iapSdkService);
        }else{
            iapsdkCallback.onFailure(new Exception("IAPSDKService could not be Initialized"),9999);
        }

    }

    private static boolean isValidInput(ECSInput ecsInput, AppInfra appInfra) {
        return ecsInput.getLocale()!=null && appInfra!=null;
    }

    public void hybrisOathAuthentication(AuthInput authInput,ECSListener ecsListener){
        ECSUtil.INSTANCE.setEcsListener(ecsListener);
    }

    public void getIAPConfig(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                new FetchConfiguration().fetchConfiguration();
            }
        }).start();

    }

    public void getProductList(){

    }

    @Override
    public void getIAPConfig(String propositionId, String locale, ECSSDKCallback ECSSDKCallback) {

    }
}
