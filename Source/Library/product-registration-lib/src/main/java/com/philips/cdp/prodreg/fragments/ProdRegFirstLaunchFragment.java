package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegFirstLaunchFragment extends ProdRegBaseFragment {
    public static final String TAG = ProdRegFirstLaunchFragment.class.getName();

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_first_launch, container, false);
        final Button registerButton = (Button) view.findViewById(R.id.yes_register_button);
        final Button registerLater = (Button) view.findViewById(R.id.no_thanks_button);
        registerButton.setOnClickListener(onClickRegister());
        registerLater.setOnClickListener(onClickNoThanks());
        return view;
    }

    @NonNull
    private View.OnClickListener onClickNoThanks() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clearFragmentStack();
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickRegister() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ProdRegProcessFragment processFragment = new ProdRegProcessFragment();
                Bundle bundle = getArguments();
                if (bundle != null) {
                    bundle.putBoolean(ProdRegConstants.PROD_REG_IS_FIRST_LAUNCH, true);
                    processFragment.setArguments(bundle);
                }
                showFragment(processFragment);
            }
        };
    }
}
