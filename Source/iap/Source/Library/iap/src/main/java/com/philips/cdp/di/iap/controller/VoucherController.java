package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetApplyVoucherRequest;
import com.philips.cdp.di.iap.screens.VoucherFragment;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;

import java.util.HashMap;

import static com.philips.cdp.di.iap.session.RequestCode.APPLY_VOUCHER;

public class VoucherController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private VoucherListener mVoucherListener ;

    public interface VoucherListener {
        void onApplyVoucherResponse(Message msg);
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

    private void sendListener(Message msg) {
        int requestCode = msg.what;
        switch (requestCode) {
            case RequestCode.APPLY_VOUCHER:
                mVoucherListener.onApplyVoucherResponse(msg);
                break;
        }
    }
}
