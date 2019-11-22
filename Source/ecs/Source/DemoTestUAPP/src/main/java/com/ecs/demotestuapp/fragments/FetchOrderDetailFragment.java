package com.ecs.demotestuapp.fragments;

import android.view.View;
import android.widget.EditText;

import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.orders.ECSOrderDetail;

public class FetchOrderDetailFragment extends BaseAPIFragment {


    EditText etOrderDetailID;

    @Override
    public void onResume() {
        super.onResume();
        etOrderDetailID = getLinearLayout().findViewWithTag("et_one");
        etOrderDetailID.setText("UNKNNOWN");
    }

    public void executeRequest() {

        String orderDetailID = etOrderDetailID.getText().toString().trim();

        ECSDataHolder.INSTANCE.getEcsServices().fetchOrderDetail(orderDetailID, new ECSCallback<ECSOrderDetail, Exception>() {
            @Override
            public void onResponse(ECSOrderDetail ecsOrderDetail) {
                ECSDataHolder.INSTANCE.setEcsOrderDetailOrderHistory(ecsOrderDetail);
                gotoResultActivity(getJsonStringFromObject(ecsOrderDetail));
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

    }
}

