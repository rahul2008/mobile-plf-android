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
import com.philips.cdp.di.ecs.model.region.ECSRegion;

import java.util.ArrayList;
import java.util.List;

public class CreateAddressFragment extends BaseFragment {

    private LinearLayout linearLayout;
    private SubgroupItem subgroupItem;

    private Button btn_execute;
    private ProgressBar progressBar;
    private Spinner spinnerSalutation,spinnerState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_fragment, container, false);

        linearLayout = rootView.findViewById(R.id.ll_container);

        Bundle bundle = getActivity().getIntent().getExtras();
        subgroupItem = (SubgroupItem) bundle.getSerializable("sub_group");
        inflateLayout(linearLayout,subgroupItem);

        spinnerSalutation = linearLayout.findViewWithTag("spinner_salutation");
        spinnerState = linearLayout.findViewWithTag("spinner_state");

        fillSpinnerDataForSalutation(spinnerSalutation);
        fillSpinnerDataForState(spinnerState);

        btn_execute = rootView.findViewById(R.id.btn_execute);
        progressBar = rootView.findViewById(R.id.progressBar);


        btn_execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                executeRequest();
            }
        });

        return rootView;
    }

    private void executeRequest() {

        ECSDataHolder.INSTANCE.getEcsServices().createAddress(getECSAddress(linearLayout), new ECSCallback<ECSAddress, Exception>() {
            @Override
            public void onResponse(ECSAddress ecsAddress) {
                gotoResultActivity(getJsonStringFromObject(ecsAddress));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e, ECSError ecsError) {

                String errorString = getFailureString(e,ecsError);
                gotoResultActivity(errorString);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void fillSpinnerDataForSalutation(Spinner spinner) {
        List<String> list = new ArrayList<>();
        list.add("Mr.");
        list.add("Ms.");

        fillSpinner(spinner,list);
    }

    private void fillSpinnerDataForState(Spinner spinner) {

        List<ECSRegion> ecsRegions = ECSDataHolder.INSTANCE.getEcsRegions();
        List<String> list = new ArrayList<String>();

        for(ECSRegion ecsRegion:ecsRegions){
            list.add(ecsRegion.getName());
        }

        fillSpinner(spinner,list);
    }


}
