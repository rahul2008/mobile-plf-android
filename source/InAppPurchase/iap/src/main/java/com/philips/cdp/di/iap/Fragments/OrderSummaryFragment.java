package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPLog;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrderSummaryFragment extends BaseAnimationSupportFragment {

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
    }

    @Override
    protected void updateTitle() {
        IAPLog.d(IAPLog.ORDER_SUMMARY_FRAGMENT, "OrderSummaryFragment = updateTitle");
        setTitle(R.string.iap_order_summary);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_order_summary_fragment, container, false);
        IAPLog.d(IAPLog.ORDER_SUMMARY_FRAGMENT, "OrderSummaryFragment ");
        return rootView;
    }

    public static OrderSummaryFragment createInstance(final AnimationType animType) {
        OrderSummaryFragment fragment = new OrderSummaryFragment();
        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
    }
}
