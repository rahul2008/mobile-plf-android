package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.NetworkConstants;

/**
 * Created by philips on 8/31/17.
 */

public class DLSAddressFragment extends InAppBaseFragment {

    public static final String TAG = DLSAddressFragment.class.getSimpleName();
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.iap_address, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View rootView) {

        TextView tv_checkOutSteps = (TextView) rootView.findViewById(R.id.tv_checkOutSteps);
        tv_checkOutSteps.setText(String.format(mContext.getString(R.string.iap_checkout_steps), "2"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    public static DLSAddressFragment createInstance(Bundle args, AnimationType animType) {
        DLSAddressFragment fragment = new DLSAddressFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }
}
