/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;

public class CancelOrderFragment extends BaseAnimationSupportFragment {

    public static final String TAG = EmptyPurchaseHistoryFragment.class.getName();
     private TextView mPhoneNumber;

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

        mPhoneNumber = (TextView) rootView.findViewById(R.id.tv_phone_number);
        Bundle bundle = getArguments();
        if (null != bundle) {
            if (bundle.containsKey(IAPConstant.CUSTOMER_CARE_NUMBER))
                mPhoneNumber.setText(bundle.getString(IAPConstant.CUSTOMER_CARE_NUMBER));
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
        setTitle(R.string.iap_cancel_order);
    }

    @Override
    public boolean onBackPressed() {
        if (getActivity() != null && getActivity() instanceof IAPActivity) {
            finishActivity();
        }
        return false;
    }
}
