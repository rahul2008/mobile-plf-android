package com.ecs.demouapp.ui.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.adapters.AppliedVoucherAdapter;
import com.ecs.demouapp.ui.analytics.ECSAnalytics;
import com.ecs.demouapp.ui.analytics.ECSAnalyticsConstant;
import com.ecs.demouapp.ui.controller.VoucherController;
import com.ecs.demouapp.ui.eventhelper.EventHelper;
import com.ecs.demouapp.ui.eventhelper.EventListener;
import com.ecs.demouapp.ui.session.IAPNetworkError;
import com.ecs.demouapp.ui.utils.AlertListener;
import com.ecs.demouapp.ui.utils.ECSConstant;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.ecs.demouapp.ui.utils.NetworkUtility;
import com.ecs.demouapp.ui.utils.Utility;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;
import com.philips.cdp.di.ecs.model.voucher.ECSVoucher;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.Label;

import java.util.List;


public class VoucherFragment extends InAppBaseFragment implements View.OnClickListener,VoucherController.VoucherListener , EventListener, AlertListener {


    VoucherController mVoucherController;
    Button mApplyVoucherButton;
    Context mContext;
    EditText voucherEditText;
    RelativeLayout voucherLayout,voucherBottomLayout;
    public AppliedVoucherAdapter mAppliedVoucherAdapter;
    private RecyclerView mRecyclerView;
    Label acceptedCodeLabel,totalCost;
    ScrollView headerParent;
    View line;
    List<ECSVoucher> getAppliedValueList;
    public static final String TAG = VoucherFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventHelper.getInstance().registerEventNotification(ECSConstant.IAP_DELETE_VOUCHER, this);
        EventHelper.getInstance().registerEventNotification(ECSConstant.IAP_DELETE_VOUCHER_CONFIRM, this);

        View rootView = inflater.inflate(R.layout.ecs_voucher_detail, container, false);
        voucherLayout = (RelativeLayout) rootView.findViewById(R.id.voucher_detail_layout);
        mApplyVoucherButton = (Button) rootView.findViewById(R.id.voucher_btn);
        voucherEditText = (EditText) rootView.findViewById(R.id.coupon_editText);
        voucherEditText.addTextChangedListener(new IAPTextWatcher(voucherEditText));
        Label voucherCodeLabel = (Label) rootView.findViewById(R.id.enter_voucher_label);
        mApplyVoucherButton.setEnabled(false);
        mApplyVoucherButton.setOnClickListener(this);
        mVoucherController = new VoucherController(mContext, VoucherFragment.this);
        mRecyclerView = rootView.findViewById(R.id.applied_voucher_cart_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        acceptedCodeLabel=(Label) rootView.findViewById(R.id.iap_accepted_code);
        headerParent=(ScrollView)rootView.findViewById(R.id.sv_header_parent);
        line=(View) rootView.findViewById(R.id.line);
        voucherBottomLayout=(RelativeLayout)rootView.findViewById(R.id.voucher_bottom_layout);
        totalCost=(Label) rootView.findViewById(R.id.total_cost_val);
        mVoucherController.getAppliedVoucherCode();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility(R.string.iap_apply_voucher, true);
        ECSAnalytics.trackPage(ECSAnalyticsConstant.APPLY_VOUCHER);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAppliedVoucherAdapter != null)
            mAppliedVoucherAdapter.onStop();
        hideProgressBar();
        NetworkUtility.getInstance().dismissErrorDialog();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.voucher_btn) {

            if (null != voucherEditText.getText().toString()) {
                createCustomProgressBar(voucherLayout, BIG);
                mVoucherController.applyCoupon(voucherEditText.getText().toString());
            }
        }
    }

    @Override
    public void onApplyVoucherResponse(Message msg) {
        hideProgressBar();

            onGetAppliedVoucherResponse(msg);

        if ((msg.obj instanceof IAPNetworkError)) {
            ECSUtility.showECSAlertDialog(mContext,"Error",((IAPNetworkError) msg.obj).getMessage());
        }
    }

    @Override
    public void onGetAppliedVoucherResponse(Message msg) {
        hideProgressBar();
        Double count = 0.00;
        if(msg.obj instanceof List){
            getAppliedValueList = (List<ECSVoucher>) msg.obj;
            mAppliedVoucherAdapter=new AppliedVoucherAdapter(mContext,getAppliedValueList);
            mRecyclerView.setAdapter(mAppliedVoucherAdapter);



          if(null!=getAppliedValueList && getAppliedValueList.size()>0){
              acceptedCodeLabel.setVisibility(View.VISIBLE);
              headerParent.setVisibility(View.VISIBLE);
              for(int i=0; i<getAppliedValueList.size();i++){
                  count=count+Double.parseDouble(getAppliedValueList.get(i).getAppliedValue().getValue());

              }
          }
          else {
              acceptedCodeLabel.setVisibility(View.GONE);
              headerParent.setVisibility(View.GONE);
          }


            String sValue = (String) String.format("%.2f", count);
            Double totalValue = Double.parseDouble(sValue);
            totalCost.setText("$ "+sValue);
            mAppliedVoucherAdapter.notifyDataSetChanged();
        }else {
            acceptedCodeLabel.setVisibility(View.GONE);
            headerParent.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDeleteAppliedVoucherResponse(Message msg) {
        hideProgressBar();
        if(mAppliedVoucherAdapter.getSelectedItemPosition()!=-1){
            getAppliedValueList.remove(mAppliedVoucherAdapter.getSelectedItemPosition());
            mAppliedVoucherAdapter.notifyItemRemoved(mAppliedVoucherAdapter.getSelectedItemPosition());
            mAppliedVoucherAdapter.notifyItemChanged(mAppliedVoucherAdapter.getSelectedItemPosition());
        }
        onGetAppliedVoucherResponse(msg);

    }

    @Override
    public void onEventReceived(String event) {
        hideProgressBar();
        if (event.equalsIgnoreCase(ECSConstant.IAP_DELETE_VOUCHER)) {
            Utility.showActionDialog(mContext, getString(R.string.iap_delete_voucher), getString(R.string.iap_cancel)
                    , getString(R.string.iap_delete_voucher), getString(R.string.iap_delete_voucher_sure), getFragmentManager(), this);
        }else if (event.equalsIgnoreCase(ECSConstant.IAP_DELETE_VOUCHER_CONFIRM)) {
            if(mAppliedVoucherAdapter.getSelectedItemPosition()!=-1){
               createCustomProgressBar(voucherLayout, BIG);
               mVoucherController.getDeleteVoucher( getAppliedValueList.get(mAppliedVoucherAdapter.getSelectedItemPosition()).getVoucherCode());
            }




        }
    }

    @Override
    public void onPositiveBtnClick() {
        EventHelper.getInstance().notifyEventOccurred(ECSConstant.IAP_DELETE_VOUCHER_CONFIRM);
    }

    @Override
    public void onNegativeBtnClick() {

    }


    private class IAPTextWatcher implements TextWatcher {
        private android.widget.EditText mEditText;

        IAPTextWatcher(android.widget.EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //  Do nothing
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (voucherEditText .getText().toString().trim().length() !=0) {
                    mApplyVoucherButton.setEnabled(true);
                }
                else {
                    mApplyVoucherButton.setEnabled(false);
                }
        }

        public synchronized void afterTextChanged(Editable text) {
            if (voucherEditText .getText().toString().trim().length() ==0) {
                mApplyVoucherButton.setEnabled(false);
            }
        }
    }

}