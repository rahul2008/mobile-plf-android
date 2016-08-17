/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;

public class CancelOrderFragment extends BaseAnimationSupportFragment {

    public static final String TAG = EmptyPurchaseHistoryFragment.class.getName();

    public static CancelOrderFragment createInstance
            (Bundle args, BaseAnimationSupportFragment.AnimationType animType) {
        CancelOrderFragment fragment = new CancelOrderFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_cancel_order, container, false);

        TextView phoneNumberText = (TextView) rootView.findViewById(R.id.tv_phone_number);
        TextView cancelOrderId = (TextView) rootView.findViewById(R.id.tv_cancel_order_history_title);
        Bundle bundle = getArguments();
        if (null != bundle) {
            if (bundle.containsKey(IAPConstant.CUSTOMER_CARE_NUMBER)) {
                String phoneNumber = bundle.getString(IAPConstant.CUSTOMER_CARE_NUMBER);
                phoneNumberText.setText(PhoneNumberUtils.formatNumber(phoneNumber,
                        HybrisDelegate.getInstance().getStore().getCountry()));
            }
            if(bundle.containsKey(IAPConstant.IAP_ORDER_ID)) {
                cancelOrderId.setText(getString(R.string.iap_cancel_your_order) + " #" + bundle.getString(IAPConstant.IAP_ORDER_ID));
            }
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility(R.string.iap_cancel_order_title,true);
        //setBackButtonVisibility(true);
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }
}
