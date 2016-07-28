/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.product_registration_lib.R;
import java.util.ArrayList;
import java.util.List;

public class ProdRegSuccessFragment extends ProdRegBaseFragment {

    public static final String TAG = ProdRegSuccessFragment.class.getName();
    private ArrayList<RegisteredProduct> regProdList;

    @Override
    public String getActionbarTitle() {
        return getActivity().getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return regProdList;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_register_success, container, false);
        ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegSuccessScreen", "specialEvents", "successProductRegistration");
        Button button = (Button) view.findViewById(R.id.continueButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clearFragmentStack(false);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            RegisteredProduct registeredProduct = arguments.getParcelable(ProdRegConstants.PROD_REG_PRODUCT);
            regProdList =  arguments.getParcelableArrayList(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            if (registeredProduct != null) {
                ProdRegTagging.getInstance().trackPageWithCommonGoals("ProdRegSuccessScreen", "productModel", registeredProduct.getCtn());
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            return clearFragmentStack(true);
        }
        return true;
    }
}
