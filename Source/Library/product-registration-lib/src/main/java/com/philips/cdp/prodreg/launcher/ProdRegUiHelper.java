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

import android.content.Intent;
import android.os.Bundle;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.fragments.ProdRegFirstLaunchFragment;
import com.philips.cdp.prodreg.fragments.ProdRegProcessFragment;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;

import java.util.ArrayList;

/**
 * Product registration helper class used to invoke product registration
 */
public class ProdRegUiHelper {

    private static ProdRegUiHelper prodRegUiHelper;
    private UiLauncher mUiLauncher;
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
     * @param uiLauncher        - Launcher to differentiate activity or fragment
     * @param prodRegConfig     - Product registration config class to define products and app flow
     * @param prodRegUiListener - Callbacks to be registered for back and continue button
     */
    public void invokeProductRegistration(final UiLauncher uiLauncher, final ProdRegConfig prodRegConfig, final ProdRegUiListener prodRegUiListener) {
        this.mUiLauncher = uiLauncher;
        this.prodRegUiListener = prodRegUiListener;
        if (uiLauncher instanceof ActivityLauncher) {
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            invokeProductRegistrationAsActivity(activityLauncher, prodRegConfig);
        } else {
            FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            invokeProductRegistrationAsFragment(fragmentLauncher, prodRegConfig);
        }
    }

    private ArrayList<RegisteredProduct> getRegisteredProductsList(final ArrayList<Product> products) {
        final ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        for (Product product : products) {
            registeredProducts.add(mapToRegisteredProduct(product));
        }
        return registeredProducts;
    }

    /**
     *
     * @return - returns the launcher type
     */
    public UiLauncher getUiLauncher() {
        return mUiLauncher;
    }

    /**
     * <p> Invoking Product Registration Component from the Intent. </p>
     * <b> Note: </b> Please make sure to set the Locale before invoking this method.
     *
     * @param activityLauncher launcher which includes orientation, start and end animation.
     * @param prodRegConfig   product registration configuration.
     */
    private void invokeProductRegistrationAsActivity(final ActivityLauncher activityLauncher, final ProdRegConfig prodRegConfig) {
        ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegHomeScreen", "specialEvents", "startProductRegistration");
        Intent intent = new Intent(activityLauncher.getFragmentActivity(), ProdRegBaseActivity.class);
        intent.putExtra(ProdRegConstants.MUL_PROD_REG_CONSTANT, prodRegConfig.getProducts());
        intent.putExtra(ProdRegConstants.START_ANIMATION_ID, activityLauncher.getEnterAnimation());
        intent.putExtra(ProdRegConstants.STOP_ANIMATION_ID, activityLauncher.getExitAnimation());
        intent.putExtra(ProdRegConstants.PROD_REG_IS_FIRST_LAUNCH, prodRegConfig.isAppLaunch());
        intent.putExtra(ProdRegConstants.SCREEN_ORIENTATION, activityLauncher.getScreenOrientation());
        intent.putExtra(ProdRegConstants.UI_KIT_THEME, activityLauncher.getUiKitTheme());
        activityLauncher.getFragmentActivity().startActivity(intent);
    }

    /**
     * <p> Invoking ProductRegistration component features to your Fragment Container. Please use this
     * method.
     * </p>
     * <b>Note: </b>
     * <p> 1) Please consider the string "product_registration" to identify the MainScreen Fragment as a
     * Fragment ID. </p>
     */
    private void invokeProductRegistrationAsFragment(FragmentLauncher fragmentLauncher, final ProdRegConfig prodRegConfig) {
        final Bundle arguments = new Bundle();
        final ArrayList<RegisteredProduct> registeredProducts = getRegisteredProductsList(prodRegConfig.getProducts());
        arguments.putSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT, registeredProducts);

        ProdRegTagging.getInstance().trackActionWithCommonGoals("ProdRegHomeScreen", "specialEvents", "startProductRegistration");
        if (prodRegConfig.isAppLaunch()) {
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

    /**
     * @return - returns the instance of listener set
     */
    public ProdRegUiListener getProdRegUiListener() {
        return prodRegUiListener;
    }

    private RegisteredProduct mapToRegisteredProduct(final Product currentProduct) {
        RegisteredProduct registeredProduct = null;
        if (currentProduct != null) {
            registeredProduct = new RegisteredProduct(currentProduct.getCtn(), currentProduct.getSector(), currentProduct.getCatalog());
            registeredProduct.setSerialNumber(currentProduct.getSerialNumber().trim());
            registeredProduct.setPurchaseDate(currentProduct.getPurchaseDate());
            registeredProduct.sendEmail(currentProduct.getEmail());
            registeredProduct.setFriendlyName(currentProduct.getFriendlyName());
        }
        return registeredProduct;
    }
}
