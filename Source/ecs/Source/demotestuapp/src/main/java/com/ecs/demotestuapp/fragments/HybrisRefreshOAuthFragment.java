package com.ecs.demotestuapp.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;

import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;

public class HybrisRefreshOAuthFragment extends HybrisOAthAuthenticationFragment {



    public void executeRequest() {

        ECSDataHolder.INSTANCE.getEcsServices().hybrisRefreshOAuth(getECSOAuthProvider(), new ECSCallback<ECSOAuthData, Exception>() {
            @Override
            public void onResponse(ECSOAuthData ecsoAuthData) {

                ECSDataHolder.INSTANCE.setEcsoAuthData(ecsoAuthData);
                gotoResultActivity(getJsonStringFromObject(ecsoAuthData));
                getProgressBar().setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e, ECSError ecsError) {

                String errorString = getFailureString(e, ecsError);
                gotoResultActivity(errorString);
                getProgressBar().setVisibility(View.GONE);
            }
        });
    }

    @Override
    public  String getAuthID(){

        if(ECSDataHolder.INSTANCE.getEcsoAuthData()!=null){
            refreshToken = ECSDataHolder.INSTANCE.getEcsoAuthData().getRefreshToken();
        }
        return refreshToken;
    }

}

