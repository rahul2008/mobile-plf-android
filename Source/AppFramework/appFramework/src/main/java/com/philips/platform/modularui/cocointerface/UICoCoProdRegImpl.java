/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.cocointerface;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.launcher.ProdRegConfig;
import com.philips.cdp.prodreg.launcher.ProdRegUiHelper;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;

import java.util.ArrayList;
import java.util.List;

public class UICoCoProdRegImpl implements UICoCoInterface, com.philips.cdp.prodreg.listener.ActionbarUpdateListener,ProdRegUiListener {
    private FragmentActivity fa;
    com.philips.cdp.prodreg.listener.ActionbarUpdateListener actionbarUpdateListener;
    int containerID;

    @Override
    public void loadPlugIn(Context context) {

    }

    private Product loadProduct() {
        Product product = new Product("HX6064/33", Sector.B2C, Catalog.CONSUMER);
        product.setSerialNumber("");
        product.setPurchaseDate("");
        product.setFriendlyName("");
        product.sendEmail(false);
        return product;
    }

    @Override
    public void runCoCo(Context context) {
        ArrayList<Product> products = new ArrayList<>();
        products.add(loadProduct());
        ProdRegConfig prodRegConfig;
            FragmentLauncher fragLauncher = new FragmentLauncher(fa, containerID, this);
            fragLauncher.setAnimation(0, 0);
            prodRegConfig = new ProdRegConfig(products, false);
            ProdRegUiHelper.getInstance().invokeProductRegistration(fragLauncher, prodRegConfig,this);
    }

    @Override
    public void unloadCoCo() {

    }

    @Override
    public void setActionbar(ActionbarUpdateListener actionBarClickListener) {

    }

    public void setActionBar(com.philips.cdp.prodreg.listener.ActionbarUpdateListener actionbarUpdateListener){
        this.actionbarUpdateListener = actionbarUpdateListener;
    }
    @Override
    public void setFragActivity(FragmentActivity fa) {
            this.fa = fa;
    }

    @Override
    public void updateActionbar(String s) {
        /*if (getActivity() != null && getActivity().getActionBar() != null)
            getActivity().getActionBar().setTitle(var1);*/
    }

    @Override
    public void onProdRegContinue(List<RegisteredProduct> list, UserWithProducts userWithProducts) {

    }

    @Override
    public void onProdRegBack(List<RegisteredProduct> list, UserWithProducts userWithProducts) {

    }

    public void setFragmentContainer(int containerID){
        this.containerID = containerID;
    }
}
