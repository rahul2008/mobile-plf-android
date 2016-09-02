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
    private int resId;

    @Override
    public int getActionbarTitleResId() {
        return R.string.PPR_NavBar_Title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return regProdList;
    }

    @Override
    public void setImageBackground() {
        if (getView() != null) {
            //TODO getView().setBackgroundResource(resId);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_register_success, container, false);
        ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegSuccessScreen", "specialEvents", "successProductRegistration");
        Button button = (Button) view.findViewById(R.id.continueButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clearFragmentStack();
                handleCallBack(false);
            }
        });
        return view;
    }

    @SuppressWarnings("noinspection unchecked")
    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            RegisteredProduct registeredProduct = (RegisteredProduct) arguments.getSerializable(ProdRegConstants.PROD_REG_PRODUCT);
            regProdList = (ArrayList<RegisteredProduct>) arguments.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            resId = arguments.getInt(ProdRegConstants.PROD_REG_FIRST_IMAGE_ID);
            if (registeredProduct != null) {
                ProdRegTagging.getInstance().trackPageWithCommonGoals("ProdRegSuccessScreen", "productModel", registeredProduct.getCtn());
            }
        }
    }

    @Override
    public boolean handleBackEvent() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            final boolean fragmentStack = clearFragmentStack();
            handleCallBack(true);
            return fragmentStack;
        }
        return true;
    }
}
