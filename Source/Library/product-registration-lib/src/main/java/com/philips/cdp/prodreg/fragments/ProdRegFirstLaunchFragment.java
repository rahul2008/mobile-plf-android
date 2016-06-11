package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RegConstants;

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
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickExtendWarranty() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                User user = new User(getActivity());
                if (user.isUserSignIn()) {
                    final ProdRegProcessFragment processFragment = new ProdRegProcessFragment();
                    processFragment.setArguments(getArguments());
                    showFragment(processFragment);
                } else {
                    RegistrationFragment registrationFragment = new RegistrationFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, false);
                    registrationFragment.setArguments(bundle);
                    registrationFragment.setOnUpdateTitleListener(new RegistrationTitleBarListener() {
                        @Override
                        public void updateRegistrationTitle(final int i) {

                        }

                        @Override
                        public void updateRegistrationTitleWithBack(final int i) {

                        }

                        @Override
                        public void updateRegistrationTitleWithOutBack(final int i) {

                        }
                    });
                    showFragment(registrationFragment);
                }
            }
        };
    }
}
