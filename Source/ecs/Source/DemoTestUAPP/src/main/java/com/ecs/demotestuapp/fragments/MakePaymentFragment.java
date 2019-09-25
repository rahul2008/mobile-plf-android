package com.ecs.demotestuapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.jsonmodel.SubgroupItem;
import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.ECSAddress;
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail;
import com.philips.cdp.di.ecs.model.payment.ECSPaymentProvider;

import java.util.ArrayList;
import java.util.List;

public class MakePaymentFragment extends BaseFragment {

    private LinearLayout linearLayout;
    private SubgroupItem subgroupItem;

    private Button btn_execute;
    private ProgressBar progressBar;
    private Spinner spinner2;

    private EditText  etOrderDetailID;

    String orderDetailID = null;
    String billingAddressID = "unknown";

    ECSOrderDetail ecsOrderDetail =null;

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

        etOrderDetailID = linearLayout.findViewWithTag("et_one");

        if(ECSDataHolder.INSTANCE.getEcsOrderDetail()!=null){
            ecsOrderDetail = ECSDataHolder.INSTANCE.getEcsOrderDetail();
            etOrderDetailID.setText(ECSDataHolder.INSTANCE.getEcsOrderDetail().getCode());
        }

        spinner2 = linearLayout.findViewWithTag("spinner_one");

        fillSpinnerData(spinner2);

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

        orderDetailID = getTextFromEditText(etOrderDetailID);

        if(spinner2.getSelectedItem()!=null){
            billingAddressID = (String)spinner2.getSelectedItem();
        }

        ECSAddress ecsAddress = getECSAddress(billingAddressID);

        ECSDataHolder.INSTANCE.getEcsServices().makePayment(ECSDataHolder.INSTANCE.getEcsOrderDetail(), ecsAddress, new ECSCallback<ECSPaymentProvider, Exception>() {
            @Override
            public void onResponse(ECSPaymentProvider ecsPaymentProvider) {

                String jsonString = getJsonStringFromObject(ecsPaymentProvider);
                gotoResultActivity(jsonString);
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

    private void fillSpinnerData(Spinner spinner) {

        List<ECSAddress> ecsAddressList = ECSDataHolder.INSTANCE.getEcsAddressList();

        List<String> list = new ArrayList<>();

        if(ecsAddressList!=null) {
            for (ECSAddress ecsAddress : ecsAddressList) {
                list.add(ecsAddress.getId());
            }
        }

        fillSpinner(spinner,list);
    }

    private ECSAddress getECSAddress(String addressID){

        ECSAddress ecsAddress = new ECSAddress() ;

        List<ECSAddress> ecsAddressList = ECSDataHolder.INSTANCE.getEcsAddressList();
        for(ECSAddress ecsAddress1:ecsAddressList){
            if(ecsAddress1.getId().equalsIgnoreCase(addressID)){
                return ecsAddress1;
            }
        }

        return ecsAddress;
    }
}
