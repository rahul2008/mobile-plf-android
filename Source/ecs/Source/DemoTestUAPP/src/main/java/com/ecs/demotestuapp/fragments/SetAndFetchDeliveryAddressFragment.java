package com.ecs.demotestuapp.fragments;

import android.view.View;
import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.ECSAddress;

import java.util.List;

public class SetAndFetchDeliveryAddressFragment extends SetDeliveryAddressFragment {



    public void executeRequest() {


        ECSDataHolder.INSTANCE.getEcsServices().setAndFetchDeliveryAddress(getECSAddress(), new ECSCallback<List<ECSAddress>, Exception>() {
            @Override
            public void onResponse(List<ECSAddress> ecsAddresses) {

                gotoResultActivity(getJsonStringFromObject(ecsAddresses));
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
}
