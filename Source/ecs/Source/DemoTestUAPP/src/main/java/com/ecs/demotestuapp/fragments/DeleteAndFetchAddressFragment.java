package com.ecs.demotestuapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.jsonmodel.SubgroupItem;
import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.ECSAddress;

import java.util.ArrayList;
import java.util.List;

public class DeleteAndFetchAddressFragment extends DeleteAddressFragment {



    public void executeRequest() {

        ECSDataHolder.INSTANCE.getEcsServices().deleteAndFetchAddress(getECSAddress(), new ECSCallback<List<ECSAddress>, Exception>() {
            @Override
            public void onResponse(List<ECSAddress> ecsAddresses) {

                ECSDataHolder.INSTANCE.setEcsAddressList(ecsAddresses);
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


    @Override
    public void clearData() {
        ECSDataHolder.INSTANCE.setEcsAddressList(null);
    }
}
