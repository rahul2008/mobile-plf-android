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
import com.philips.cdp.di.ecs.model.order.ECSOrderHistory;
import com.philips.cdp.di.ecs.model.order.ECSOrders;

import java.util.ArrayList;
import java.util.List;

public class FetchOrderDetailForOdersFragment extends BaseFragment {

    private LinearLayout linearLayout;
    private SubgroupItem subgroupItem;

    private Button btn_execute;
    private ProgressBar progressBar;

    private Spinner spinnerOrders;

    String orderID = "unknown";

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


        spinnerOrders  = linearLayout.findViewWithTag("spinner_one");


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

        ECSOrderHistory ecsOrderHistory = ECSDataHolder.INSTANCE.getEcsOrderHistory();

        if(ecsOrderHistory!=null){

            List<String> list = new ArrayList<>();
            List<ECSOrders> orders = ecsOrderHistory.getOrders();

            if(orders !=null){

                for(ECSOrders ecsOrders:orders){
                    list.add(ecsOrders.getCode());
                }
            }
            fillSpinner(spinner,list);
        }

    }

    private ECSOrders getOrderFromOrderID(String orderID){

        ECSOrderHistory ecsOrderHistory = ECSDataHolder.INSTANCE.getEcsOrderHistory();

        if(ecsOrderHistory!=null){

            List<ECSOrders> orders = ecsOrderHistory.getOrders();

            if(orders !=null){

                for(ECSOrders ecsOrders:orders){

                    if(orderID.equalsIgnoreCase(ecsOrders.getCode())){
                        return ecsOrders;
                    }
                }
            }

        }
        return null;
    }



    private void executeRequest() {

        if(spinnerOrders.getSelectedItem()!=null) {
            orderID = (String) spinnerOrders.getSelectedItem();
        }

        ECSOrders ecsOrders = getOrderFromOrderID(orderID);

        ECSDataHolder.INSTANCE.getEcsServices().fetchOrderDetail(ecsOrders, new ECSCallback<ECSOrders, Exception>() {
            @Override
            public void onResponse(ECSOrders ecsOrders) {

                String jsonString = getJsonStringFromObject(ecsOrders);
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

}
