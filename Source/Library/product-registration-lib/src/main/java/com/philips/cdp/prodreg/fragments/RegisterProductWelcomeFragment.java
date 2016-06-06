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
public class RegisterProductWelcomeFragment extends ProdRegBaseFragment {
    Button extendWarranty, registerLater;

    @Override
    public String getActionbarTitle() {
        return null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_extend_warranty, container, false);
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
                showFragment(new InitialFragment());
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickExtendWarranty() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showFragment(new RegisterSingleProductFragment());
            }
        };
    }
}
