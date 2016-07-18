/**
 * DigitalCareConfigManager is the Singleton class helps to manage,customize the features through
 * the supported API's.
 * <b> Note: </b>
 * <p> Few Methods may not relevant your requirement. As it playing the Horizontal Component
 * - API's are added by considering the commmon requirement  for the integrating applciations.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 5 Dec 2014
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.prodreg.launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.fragments.ProdRegFirstLaunchFragment;
import com.philips.cdp.prodreg.fragments.ProdRegProcessFragment;
import com.philips.cdp.prodreg.listener.ProdRegBackListener;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;

import java.util.ArrayList;

public class ProdRegUiHelper {

    private static int mContainerId;
    private static ProdRegUiHelper prodRegUiHelper;
    private UiLauncher mUiLauncher;
    private ArrayList<RegisteredProduct> productList;
    private ProdRegUiListener prodRegUiListener;
    /*
         * Initialize everything(resources, variables etc) required for Product Registration.
         * Hosting app, which will integrate this Product Registration, has to pass app
         * context.
         */
    private ProdRegUiHelper() {
    }

    /*
     * Singleton pattern.
     */
    public static ProdRegUiHelper getInstance() {
        if (prodRegUiHelper == null) {
            prodRegUiHelper = new ProdRegUiHelper();
        }
        return prodRegUiHelper;
    }

    /**
     * <p> Invoking ProductRegistration component features to your Fragment Container. Please use this
     * method.
     * </p>
     * <b>Note: </b>
     * <p> 1) Please consider the string "product_registration" to identify the MainScreen Fragment as a
     * Fragment ID. </p>
     */
    private void invokeProductRegistrationAsFragment(FragmentLauncher fragmentLauncher) {
        mContainerId = fragmentLauncher.getParentContainerResourceID();
        final Bundle arguments = new Bundle();
        arguments.putSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT, productList);

        ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegHomeScreen", "specialEvents", "startProductRegistration");
        if (fragmentLauncher.isFirstLaunch()) {
            ProdRegFirstLaunchFragment prodRegFirstLaunchFragment = new ProdRegFirstLaunchFragment();
            prodRegFirstLaunchFragment.setArguments(arguments);
            prodRegFirstLaunchFragment.showFragment(prodRegFirstLaunchFragment,
                    fragmentLauncher, fragmentLauncher.getEnterAnimation(), fragmentLauncher.getExitAnimation());
        } else {
            ProdRegProcessFragment prodRegProcessFragment = new ProdRegProcessFragment();
            prodRegProcessFragment.setArguments(arguments);
            prodRegProcessFragment.showFragment(prodRegProcessFragment,
                    fragmentLauncher, fragmentLauncher.getEnterAnimation(), fragmentLauncher.getExitAnimation());
        }
    }

    public void invokeProductRegistration(final UiLauncher uiLauncher, final ArrayList<Product> products, final ProdRegUiListener prodRegUiListener) {
        this.mUiLauncher = uiLauncher;
        createRegisteredProductsList(products);
        this.prodRegUiListener = prodRegUiListener;
        if (uiLauncher instanceof ActivityLauncher) {
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            invokeProductRegistrationAsActivity(activityLauncher.getFragmentActivity(), uiLauncher.getEnterAnimation(), uiLauncher.getExitAnimation(), activityLauncher.getScreenOrientation());
        } else {
            FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            invokeProductRegistrationAsFragment(fragmentLauncher);
        }
    }

    private void createRegisteredProductsList(final ArrayList<Product> products) {
        productList = new ArrayList<>();
        for (Product product : products) {
            this.productList.add(mapToRegisteredProduct(product));
        }
    }

    public UiLauncher getUiLauncher() {
        return mUiLauncher;
    }

    /**
     * <p> Invoking Product Registration Component from the Intent. </p>
     * <b> Note: </b> Please make sure to set the Locale before invoking this method.
     *
     * @param startAnimation Animation resource ID.
     * @param endAnimation   Animation Resource ID.
     * @param orientation    Orientation
     */
    private void invokeProductRegistrationAsActivity(Context context, int startAnimation, int endAnimation, ActivityLauncher.ActivityOrientation orientation) {
        ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegHomeScreen", "specialEvents", "startProductRegistration");
        Intent intent = new Intent(context, ProdRegBaseActivity.class);
        intent.putExtra(ProdRegConstants.MUL_PROD_REG_CONSTANT, productList);
        intent.putExtra(ProdRegConstants.START_ANIMATION_ID, startAnimation);
        intent.putExtra(ProdRegConstants.STOP_ANIMATION_ID, endAnimation);
        intent.putExtra(ProdRegConstants.PROD_REG_IS_FIRST_LAUNCH, mUiLauncher.isFirstLaunch());
        intent.putExtra(ProdRegConstants.SCREEN_ORIENTATION, orientation.getOrientationValue());
        context.startActivity(intent);
    }

    public boolean onBackPressed(FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        if (fragmentManager != null && !fragmentActivity.isFinishing()) {
            Fragment currentFrag = fragmentManager
                    .findFragmentById(mContainerId);
            if (currentFrag != null && currentFrag instanceof ProdRegBackListener) {
                return ((ProdRegBackListener) currentFrag).onBackPressed();
            }
        }
        return false;
    }

    public ProdRegUiListener getProdRegUiListener() {
        return prodRegUiListener;
    }

    private RegisteredProduct mapToRegisteredProduct(final Product currentProduct) {
        RegisteredProduct registeredProduct = null;
        if (currentProduct != null) {
            registeredProduct = new RegisteredProduct(currentProduct.getCtn(), currentProduct.getSector(), currentProduct.getCatalog());
            registeredProduct.setSerialNumber(currentProduct.getSerialNumber());
            registeredProduct.setPurchaseDate(currentProduct.getPurchaseDate());
            registeredProduct.sendEmail(currentProduct.getEmail());
            registeredProduct.setFriendlyName(currentProduct.getFriendlyName());
        }
        return registeredProduct;
    }
}
