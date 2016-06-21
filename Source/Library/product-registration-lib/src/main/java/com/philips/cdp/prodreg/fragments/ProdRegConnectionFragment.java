package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.prodreg.listener.ProdRegBackListener;
import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegConnectionFragment extends ProdRegBaseFragment implements ProdRegBackListener {

    public static final String TAG = ProdRegConnectionFragment.class.getName();

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_connection, container, false);
        Button backButton = (Button) view.findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });
        return view;
    }

    @Override
    public boolean onBackPressed() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            return clearFragmentStack();

        }
        return true;
    }
}
