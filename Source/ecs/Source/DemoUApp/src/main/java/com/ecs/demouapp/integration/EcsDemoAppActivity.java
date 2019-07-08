package com.ecs.demouapp.integration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ecs.demouapp.R;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.ArrayList;

public class EcsDemoAppActivity extends AppCompatActivity implements View.OnClickListener, IAPListener {



    private final String TAG = EcsDemoAppActivity.class.getSimpleName();
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;

    URInterface urInterface;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);

        urInterface = new URInterface();
        urInterface.init(new EcsDemoUAppDependencies(new AppInfra.Builder().build(getApplicationContext())), new EcsDemoAppSettings(getApplicationContext()));

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * Gets the product count in cart
     *
     * @param count - count is an integer through which basis user can update the count visibility
     * @since 1.0.0
     */
    @Override
    public void onGetCartCount(int count) {

    }

    /**
     * Notifies when product count in cart is updated
     *
     * @since 1.0.0
     */
    @Override
    public void onUpdateCartCount() {

    }

    /**
     * Notifies true for cart icon visibility or false for hide
     *
     * @param shouldShow boolean will help to update hte cart icon visibility
     * @since 1.0.0
     */
    @Override
    public void updateCartIconVisibility(boolean shouldShow) {

    }

    /**
     * Notifies true for when fetched complet product form backend service or false if not fetched
     *
     * @param productList will get list of CTNs from backend
     * @since 1.0.0
     */
    @Override
    public void onGetCompleteProductList(ArrayList<String> productList) {

    }

    /**
     * Notifies success of any request
     *
     * @since 1.0.0
     */
    @Override
    public void onSuccess() {

    }

    /**
     * Notifies true for backend response or false for local
     *
     * @param bool will get true or false
     * @since 1.0.0
     */
    @Override
    public void onSuccess(boolean bool) {

    }

    /**
     * It gives failure responses with error code
     *
     * @param errorCode errorCode when IAP encounters any error
     * @since 1.0.0
     */
    @Override
    public void onFailure(int errorCode) {

    }


    private void initTheme() {
        int themeResourceID = new EcsThemeHelper(this).getThemeResourceId();
        int themeIndex = themeResourceID;
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));

    }
}
