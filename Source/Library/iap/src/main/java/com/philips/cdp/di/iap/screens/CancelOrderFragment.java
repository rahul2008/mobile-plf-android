/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;

public class CancelOrderFragment extends InAppBaseFragment {

    public static final String TAG = CancelOrderFragment.class.getName();
    private String phoneNumber;

    public static CancelOrderFragment createInstance
            (Bundle args, InAppBaseFragment.AnimationType animType) {
        CancelOrderFragment fragment = new CancelOrderFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_cancel_order, container, false);

        Button phoneNumberText = (Button) rootView.findViewById(R.id.bt_phone_number);
        TextView cancelOrderId = (TextView) rootView.findViewById(R.id.tv_cancel_order_history_title);
        TextView keepOrderText = (TextView) rootView.findViewById(R.id.tv_from_other_channel);
        TextView openingTimingText = (TextView) rootView.findViewById(R.id.tv_opening_timings);

        Bundle bundle = getArguments();
        if (null != bundle) {
                phoneNumber = bundle.getString(IAPConstant.CUSTOMER_CARE_NUMBER);
                phoneNumberText.setText("Call " + PhoneNumberUtils.formatNumber(phoneNumber,
                        HybrisDelegate.getInstance().getStore().getCountry()));
                String weekdaysTiming = bundle.getString(IAPConstant.CUSTOMER_CARE_WEEKDAYS_TIMING);
                String saturdayTiming = bundle.getString(IAPConstant.CUSTOMER_CARE_SATURDAY_TIMING);
                openingTimingText.setText(getString(R.string.iap_opening_hours) + weekdaysTiming + "\n" + saturdayTiming);
                cancelOrderId.setText(getString(R.string.iap_cancel_your_order) + " #" + bundle.getString(IAPConstant.IAP_ORDER_ID));
                keepOrderText.setText(getString(R.string.iap_keep_order_number) + " #" + bundle.getString(IAPConstant.IAP_ORDER_ID));
        }
        phoneNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:" + PhoneNumberUtils.formatNumber(phoneNumber,
                        HybrisDelegate.getInstance().getStore().getCountry());
                i.setData(Uri.parse(p));
                startActivity(i);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility(R.string.iap_track_order, true);
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }
}
