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
import com.philips.cdp.di.ecs.model.address.ECSDeliveryMode;
import com.philips.cdp.di.ecs.model.cart.ECSEntries;
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher;

import java.util.ArrayList;
import java.util.List;

public class SetDeliveryModeFragment extends BaseFragment {

    private LinearLayout linearLayout;
    private SubgroupItem subgroupItem;

    private Button btn_execute;
    private ProgressBar progressBar;
    private Spinner spinner;

    String deliveryMode = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_fragment, container, false);

        linearLayout = rootView.findViewById(R.id.ll_container);

        Bundle bundle = getActivity().getIntent().getExtras();
        subgroupItem = (SubgroupItem) bundle.getSerializable("sub_group");
        inflateLayout(linearLayout,subgroupItem);


        btn_execute = rootView.findViewById(R.id.btn_execute);
        progressBar = rootView.findViewById(R.id.progressBar);

        spinner = linearLayout.findViewWithTag("spinner_one");

        fillSpinnerData(spinner);

        btn_execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                executeRequest();
            }
        });

        return rootView;
    }

    private void fillSpinnerData(Spinner spinner) {

        ArrayList<String> deliveryModeCodes = new ArrayList<>();

        if (ECSDataHolder.INSTANCE.getEcsProducts() != null) {

            List<ECSDeliveryMode> ecsDeliveryModes = ECSDataHolder.INSTANCE.getEcsDeliveryModes();
            if (ecsDeliveryModes.size() != 0) {

                for (ECSDeliveryMode ecsDeliveryMode : ecsDeliveryModes) {
                    deliveryModeCodes.add(ecsDeliveryMode.getCode() + "");
                }

                fillSpinner(spinner, deliveryModeCodes);
            }
        }
    }

    private ECSDeliveryMode getDeliveryMode(String deliveryModeID){

        if (ECSDataHolder.INSTANCE.getEcsProducts() != null) {

            List<ECSDeliveryMode> ecsDeliveryModes = ECSDataHolder.INSTANCE.getEcsDeliveryModes();
            if (ecsDeliveryModes.size() != 0) {

                for (ECSDeliveryMode ecsDeliveryMode : ecsDeliveryModes) {

                    if(ecsDeliveryMode.getCode().equalsIgnoreCase(deliveryModeID)){
                        return ecsDeliveryMode;
                    }
                }


            }
        }
        return null;
    }

    private void executeRequest() {

        String selectedDeliveryID = (String)spinner.getSelectedItem();

        ECSDeliveryMode deliveryMode = getDeliveryMode(selectedDeliveryID);


        ECSDataHolder.INSTANCE.getEcsServices().setDeliveryMode(deliveryMode, new ECSCallback<Boolean, Exception>() {
            @Override
            public void onResponse(Boolean aBoolean) {

                gotoResultActivity(aBoolean+"");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e, ECSError ecsError) {

                String errorString = getFailureString(e, ecsError);
                gotoResultActivity(errorString);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
