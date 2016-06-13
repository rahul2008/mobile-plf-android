package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegFirstLaunchFragment extends ProdRegBaseFragment {
    public static final String TAG = ProdRegFirstLaunchFragment.class.getName();
    private Button extendWarranty, registerLater;

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.prodreg_actionbar_title);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_first_launch, container, false);
        extendWarranty = (Button) view.findViewById(R.id.yes_register_button);
        registerLater = (Button) view.findViewById(R.id.no_thanks_button);
        extendWarranty.setOnClickListener(onClickExtendWarranty());
        registerLater.setOnClickListener(onClickNoThanks());
        return view;
    }

    @NonNull
    private View.OnClickListener onClickNoThanks() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getActivity().onBackPressed();
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickExtendWarranty() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ProdRegProcessFragment processFragment = new ProdRegProcessFragment();
                processFragment.setArguments(getArguments());
                showFragment(processFragment);
            }
        };
    }
}
