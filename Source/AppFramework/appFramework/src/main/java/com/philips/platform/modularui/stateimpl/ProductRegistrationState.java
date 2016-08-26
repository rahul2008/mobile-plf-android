/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.launcher.PRInterface;
import com.philips.cdp.prodreg.launcher.ProdRegLaunchInput;
import com.philips.cdp.prodreg.launcher.ProdRegUiHelper;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.ArrayList;
import java.util.List;

public class ProductRegistrationState extends UIState implements ProdRegUiListener {

    Context mContext;
    int containerID;
    private FragmentActivity fa;
    ActionbarUpdateListener actionbarUpdateListener;

    public ProductRegistrationState(@UIStateDef int stateID){
        super(stateID);
    }
    @Override
    protected void navigate(Context context) {
        mContext = context;
        runProductRegistration();
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }

    private Product loadProduct() {
        Product product = new Product("HX6064/33", Sector.B2C, Catalog.CONSUMER);
        product.setSerialNumber("");
        product.setPurchaseDate("");
        product.setFriendlyName("");
        product.sendEmail(false);
        return product;
    }
/*
    @Override
    public void updateActionbar(String s) {

    }*/

    @Override
    public void onProdRegContinue(List<RegisteredProduct> list, UserWithProducts userWithProducts) {

    }

    @Override
    public void onProdRegBack(List<RegisteredProduct> list, UserWithProducts userWithProducts) {

    }

    @Override
    public void onProdRegFailed(ProdRegError prodRegError) {

    }

    public void runProductRegistration(){
        ArrayList<Product> products = new ArrayList<>();
        products.add(loadProduct());
        ProdRegLaunchInput prodRegLaunchInput;
        if(mContext instanceof HomeActivity){
            containerID = R.id.frame_container;
            fa = (HomeActivity)mContext;
        }
        FragmentLauncher fragLauncher = new FragmentLauncher(fa, containerID, new ActionBarListener() {
            @Override
            public void updateActionBar(@StringRes int i, boolean b) {

            }

            @Override
            public void updateActionBar(String s, boolean b) {

            }
        });
        fragLauncher.setCustomAnimation(0, 0);
        prodRegLaunchInput = new ProdRegLaunchInput(products, false);
        prodRegLaunchInput.setProdRegUiListener(this);
        new PRInterface().launch(fragLauncher,prodRegLaunchInput);
    }
}
