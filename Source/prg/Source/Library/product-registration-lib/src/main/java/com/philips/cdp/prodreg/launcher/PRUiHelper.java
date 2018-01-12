/**
 * PRUiHelper is the Singleton class helps to manage,customize the features through
 * the supported API's.
 * <b> Note: </b>
 * <p> Few Methods may not relevant your requirement. As it playing the Horizontal Component
 * - API's are added by considering the common requirement  for the integrating applications.
 *
 * @author : yogesh.hs@philips.com
 * @since : 5 May 2015
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.prodreg.launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.philips.cdp.prodreg.activity.ProdRegBaseActivity;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.fragments.*;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.product_registration_lib.BuildConfig;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.util.ArrayList;

/**
 * Product registration helper class used to invoke product registration
 * @since 1.0.0
 */
public class PRUiHelper {

    private static PRUiHelper prodRegHelper;
    private static LoggingInterface loggingInterface;
    private UiLauncher mUiLauncher;
    private ProdRegUiListener prodRegUiListener;
    private Context context;
    private AppInfra appInfra;
    private String mCountryCode;
    private String mLocale;
    ThemeConfiguration themeConfiguration;

    int theme;

    /*
     * Initialize everything(resources, variables etc) required for Product Registration.
     * Hosting app, which will integrate this Product Registration, has to pass app
     * context.
     */
    private PRUiHelper() {
    }

    /*
     * Singleton pattern.
     */
    public static PRUiHelper getInstance() {
        if (prodRegHelper == null) {
            prodRegHelper = new PRUiHelper();
        }
        return prodRegHelper;
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
     * @param prLaunchInput   product registration configuration.
     */
    private void invokeProductRegistrationAsActivity(final ActivityLauncher activityLauncher, final PRLaunchInput prLaunchInput) {
        ProdRegTagging.getInstance().trackAction("ProdRegStartEvent", "specialEvents", "startProductRegistration");
        Intent intent = new Intent(context, ProdRegBaseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ProdRegConstants.MUL_PROD_REG_CONSTANT, prLaunchInput.getProducts());
        intent.putExtra(ProdRegConstants.START_ANIMATION_ID, activityLauncher.getEnterAnimation());
        intent.putExtra(ProdRegConstants.STOP_ANIMATION_ID, activityLauncher.getExitAnimation());
        intent.putExtra(ProdRegConstants.PROD_REG_IS_FIRST_LAUNCH, prLaunchInput.isAppLaunchFlow());
        intent.putExtra(ProdRegConstants.SCREEN_ORIENTATION, activityLauncher.getScreenOrientation());
        intent.putExtra(ProdRegConstants.UI_KIT_THEME,  activityLauncher.getUiKitTheme());
        intent.putExtra(ProdRegConstants.PROD_REG_FIRST_IMAGE_ID, prLaunchInput.getBackgroundImageResourceId());
        context.startActivity(intent);
    }

    /**
     * <p> Invoking ProductRegistration component features to your Fragment Container. Please use this
     * method.
     * </p>
     * <b>Note: </b>
     * <p> 1) Please consider the string "product_registration" to identify the MainScreen Fragment as a
     * Fragment ID. </p>
     */
    private void invokeProductRegistrationAsFragment(FragmentLauncher fragmentLauncher, final PRLaunchInput PRLaunchInput) {
        if (prodRegUiListener != null) {
            final Bundle arguments = new Bundle();
            final ArrayList<RegisteredProduct> registeredProducts = getRegisteredProductsList(PRLaunchInput.getProducts());
            arguments.putSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT, registeredProducts);
            arguments.putInt(ProdRegConstants.PROD_REG_FIRST_IMAGE_ID, PRLaunchInput.getBackgroundImageResourceId());
            arguments.putBoolean(ProdRegConstants.PROD_REG_IS_FIRST_LAUNCH, PRLaunchInput.isAppLaunchFlow());
            ProdRegTagging.getInstance().trackAction("ProdRegStartEvent", "specialEvents", "startProductRegistration");
            final User user = new User(fragmentLauncher.getFragmentActivity());
            if (PRLaunchInput.isAppLaunchFlow()) {
                ProdRegFirstLaunchFragment prodRegFirstLaunchFragment = new ProdRegFirstLaunchFragment();
                prodRegFirstLaunchFragment.setArguments(arguments);
                prodRegFirstLaunchFragment.showFragment(prodRegFirstLaunchFragment,
                        fragmentLauncher, fragmentLauncher.getEnterAnimation(), fragmentLauncher.getExitAnimation());
            } else if (!user.isUserSignIn()) {
                prodRegUiListener.onProdRegFailed(ProdRegError.USER_NOT_SIGNED_IN);
                if (fragmentLauncher.getFragmentActivity() instanceof ProdRegBaseActivity)
                    fragmentLauncher.getFragmentActivity().finish();
            } else {
                ProdRegRegistrationFragment prodRegProcessFragment = new ProdRegRegistrationFragment();
                prodRegProcessFragment.setArguments(arguments);
                prodRegProcessFragment.showFragment(prodRegProcessFragment,
                        fragmentLauncher, fragmentLauncher.getEnterAnimation(), fragmentLauncher.getExitAnimation());
            }
        } else {
            throw new RuntimeException("Listener not set");
        }
    }

    /**
     * @return - returns the instance of listener set
     */
    public ProdRegUiListener getProdRegUiListener() {
        return prodRegUiListener;
    }

    public void unRegisterProdRegListener() {
        this.prodRegUiListener = null;
    }

    private RegisteredProduct mapToRegisteredProduct(final Product currentProduct) {
        RegisteredProduct registeredProduct = null;
        if (currentProduct != null) {
            registeredProduct = new RegisteredProduct(currentProduct.getCtn().trim(), currentProduct.getSector(), currentProduct.getCatalog());
            registeredProduct.setRegistrationState(RegistrationState.PENDING);
            registeredProduct.setSerialNumber(currentProduct.getSerialNumber().trim());
            registeredProduct.setPurchaseDate(currentProduct.getPurchaseDate());
            registeredProduct.sendEmail(currentProduct.getEmail());
            registeredProduct.setFriendlyName(currentProduct.getFriendlyName());
        }
        return registeredProduct;
    }

    public LoggingInterface getLoggerInterface() {
        if (loggingInterface == null) {
            loggingInterface = appInfra.getLogging().
                    createInstanceForComponent("Product Registration", BuildConfig.VERSION_NAME);
        }
        return loggingInterface;
    }

    protected void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
        this.appInfra = (AppInfra) uappDependencies.getAppInfra();
        new ProdRegHelper().init();
        ProdRegTagging.init(appInfra);
    }

    protected void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        this.mUiLauncher = uiLauncher;

        final PRLaunchInput PRLaunchInput = (PRLaunchInput) uappLaunchInput;
        this.prodRegUiListener = PRLaunchInput.getProdRegUiListener();
        if (uiLauncher instanceof ActivityLauncher) {
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            setThemeConfiguration(((ActivityLauncher) uiLauncher).getDlsThemeConfiguration());
            setTheme(((ActivityLauncher) uiLauncher).getUiKitTheme());
            invokeProductRegistrationAsActivity(activityLauncher, PRLaunchInput);
        } else {
            FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            invokeProductRegistrationAsFragment(fragmentLauncher, PRLaunchInput);
        }
    }

    public ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }

    public void setThemeConfiguration(ThemeConfiguration themeConfiguration) {
        this.themeConfiguration = themeConfiguration;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public SecureStorageInterface getAppInfraSecureStorageInterface() {
        if (appInfra != null)
            return appInfra.getSecureStorage();

        return null;
    }

    public TimeInterface getServerTime() {
        if (appInfra != null)
            return appInfra.getTime();

        return null;
    }
    private AppInfraInterface appInfraInterface;

    public void setAppInfraInstance(AppInfraInterface appInfra) {
        this.appInfraInterface = appInfra;
    }

    public AppInfraInterface getAppInfraInstance() {
        return appInfraInterface;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String mLocale) {
        this.mLocale = mLocale;
    }
}
