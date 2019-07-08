package com.ecs.demouapp.ui.controller;

import android.content.Context;
import android.os.Message;


import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.model.DeleteVoucherRequest;
import com.ecs.demouapp.ui.model.GetAppliedVoucherRequest;
import com.ecs.demouapp.ui.model.GetApplyVoucherRequest;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.session.RequestCode;
import com.ecs.demouapp.ui.utils.ModelConstants;

import java.util.HashMap;

import static com.ecs.demouapp.ui.session.RequestCode.APPLY_VOUCHER;
import static com.ecs.demouapp.ui.session.RequestCode.DELETE_VOUCHER;
import static com.ecs.demouapp.ui.session.RequestCode.GET_APPLIED_VOUCHER;


public class VoucherController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private VoucherListener mVoucherListener ;

    public interface VoucherListener {
        void onApplyVoucherResponse(Message msg);
        void onGetAppliedVoucherResponse(Message msg);
        void onDeleteAppliedVoucherResponse(Message msg);
    }

    public VoucherController(Context mContext, VoucherListener voucherListener) {
        this.mContext = mContext;
        this.mVoucherListener=voucherListener;
    }

    @Override
    public void onModelDataLoadFinished(Message msg) {
        sendListener(msg);
    }

    @Override
    public void onModelDataError(Message msg) {
        sendListener(msg);
    }

    public void applyCoupon(String voucherId) {
        final HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.VOUCHER_ID, voucherId);
        GetApplyVoucherRequest request = new GetApplyVoucherRequest(delegate.getStore(), query, this);
        delegate.sendRequest(APPLY_VOUCHER, request, request);
    }

    public void getAppliedVoucherCode() {

        final HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        GetAppliedVoucherRequest request = new GetAppliedVoucherRequest(delegate.getStore(), null,this);
        delegate.sendRequest(GET_APPLIED_VOUCHER, request, request);
    }

    public void getDeleteVoucher(String voucherCode){
        final HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);

        DeleteVoucherRequest deleteVoucherRequest = new DeleteVoucherRequest(delegate.getStore(), null, this, voucherCode);
        delegate.sendRequest(DELETE_VOUCHER, deleteVoucherRequest, deleteVoucherRequest);
    }

    private void sendListener(Message msg) {
        int requestCode = msg.what;
        switch (requestCode) {
            case RequestCode.APPLY_VOUCHER:
                mVoucherListener.onApplyVoucherResponse(msg);
                break;
            case RequestCode.GET_APPLIED_VOUCHER:
                mVoucherListener.onGetAppliedVoucherResponse(msg);
                break;
            case RequestCode. DELETE_VOUCHER:
                mVoucherListener.onDeleteAppliedVoucherResponse(msg);
                break;

        }
    }
}
