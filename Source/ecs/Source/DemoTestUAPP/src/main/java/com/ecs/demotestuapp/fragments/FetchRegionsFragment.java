package com.ecs.demotestuapp.fragments;

import android.view.View;
import android.widget.EditText;

import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.region.ECSRegion;

import java.util.List;

public class FetchRegionsFragment extends BaseAPIFragment {


    String countryISO = null;
    EditText etCountryISO ;

    @Override
    public void onResume() {
        super.onResume();
        etCountryISO = getLinearLayout().findViewWithTag("et_one");
    }

    public void executeRequest() {

        if(etCountryISO.getText() != null){
            countryISO =  etCountryISO.getText().toString();
        }

        ECSDataHolder.INSTANCE.getEcsServices().fetchRegions(countryISO,new ECSCallback<List<ECSRegion>, Exception>() {
            @Override
            public void onResponse(List<ECSRegion> ecsRegions) {

                ECSDataHolder.INSTANCE.setEcsRegions(ecsRegions);
                gotoResultActivity(getJsonStringFromObject(ecsRegions));
                getProgressBar().setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e, ECSError ecsError) {

                String errorString = getFailureString(e,ecsError);

                gotoResultActivity(errorString);
                getProgressBar().setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void clearData() {
        ECSDataHolder.INSTANCE.setEcsRegions(null);
    }
}

