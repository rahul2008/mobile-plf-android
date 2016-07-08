package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.launcher.ProdRegUiHelper;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegSuccessFragment extends ProdRegBaseFragment {

    public static final String TAG = ProdRegSuccessFragment.class.getName();

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_registere_success, container, false);
        ProdRegTagging.getInstance(getActivity()).trackActionWithCommonGoals("ProdRegSuccessScreen", "specialEvents", "successProductRegistration");
        Button button = (Button) view.findViewById(R.id.continueButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            RegisteredProduct registeredProduct = (RegisteredProduct) arguments.getSerializable(ProdRegConstants.PROD_REG_PRODUCT);
            if (registeredProduct != null) {
                ProdRegTagging.getInstance(getActivity()).trackPageWithCommonGoals("ProdRegSuccessScreen", "productModel", registeredProduct.getCtn());
                final ProdRegUiListener prodRegUiListener = ProdRegUiHelper.getInstance().getProdRegUiListener();
                if (prodRegUiListener != null)
                    prodRegUiListener.onProdRegExit(registeredProduct, new ProdRegHelper().getSignedInUserWithProducts());
            }
        }
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
