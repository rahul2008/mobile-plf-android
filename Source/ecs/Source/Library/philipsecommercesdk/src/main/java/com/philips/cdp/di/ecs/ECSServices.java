package com.philips.cdp.di.ecs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.di.ecs.integration.AuthInput;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSInput;
import com.philips.cdp.di.ecs.integration.ECSListener;
import com.philips.cdp.di.ecs.integration.ECSServiceProvider;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.AppInfra;

import static com.philips.cdp.di.ecs.integration.ECSErrorReason.INITIALIZATION_FAILURE;

public class ECSServices implements ECSServiceProvider {

    private static ECSServices mECSServices;

    private ECSManager mECSManager;

    private ECSServices(ECSInput ecsInput, AppInfra appInfra) {
        ECSConfig.INSTANCE.setAppInfra(appInfra);
        ECSConfig.INSTANCE.setEcsInput(ecsInput);
        mECSManager = new ECSManager();
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
        ECSConfig.INSTANCE.setEcsListener(ecsListener);
    }




    @Override
    public void getIAPConfig(ECSCallback<HybrisConfigResponse, Exception> ecsCallback) {

        mECSManager.getHybrisConfigResponse(ecsCallback);

    }

    @Override
    public void getProductDetail(Context context,int currentPage, int pageSize, ECSCallback<Products, Exception> eCSCallback) {
        mECSManager.getProductDetail(context,currentPage,pageSize,eCSCallback);
    }


    @Override
    public void InvalidateECS(ECSCallback<Boolean,Exception> ecsCallback) {
        mECSServices=null;
        ecsCallback.onResponse(true);

    }
}
