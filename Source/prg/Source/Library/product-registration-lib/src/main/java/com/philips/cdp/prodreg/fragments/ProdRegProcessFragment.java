/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.prodreg.register.ProdRegProcessController;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.product_registration_lib.R;

import java.util.List;

public class ProdRegProcessFragment extends ProdRegBaseFragment implements ProdRegProcessController.ProcessControllerCallBacks {

    public static final String TAG = ProdRegProcessFragment.class.getName();
    private static final long serialVersionUID = -6635233525340545670L;

    private ProdRegProcessController prodRegProcessController;
    private boolean isFailedOnError = false;
    private int responseCode;
    private boolean isFirstLaunch;
    private int resId;

    @Override
    public int getActionbarTitleResId() {
        return R.string.PRG_NavBar_Title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.PRG_NavBar_Title);
    }

    @Override
    public boolean getBackButtonState() {
        return isFirstLaunch;
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return null;
    }

    private void setImageBackground() {
        if (getView() != null && resId != 0) {
            getView().setBackgroundResource(resId);
        }
    }


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prodreg_activity, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setImageBackground();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {

    }

    public void exitProductRegistration() {
        clearFragmentStack();
        unRegisterProdRegListener();
    }

    @Override
    public void showAlertOnError(int responseCode) {
        super.showAlertOnError(responseCode);
        this.responseCode = responseCode;
        isFailedOnError = true;
    }


    public void buttonEnable() {

    }

    @Override
    public void showFragment(Fragment fragment) {
        super.showFragment(fragment);
    }


    public void showLoadingDialog() {
        showProdRegLoadingDialog(getString(R.string.PRG_Looking_For_Products_Lbltxt), "prg_dialog");
    }

    public void dismissLoadingDialog() {
        dismissProdRegLoadingDialog();
    }

}
