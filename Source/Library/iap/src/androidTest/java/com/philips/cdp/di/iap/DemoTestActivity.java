package com.philips.cdp.di.iap;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.deps.guava.annotations.VisibleForTesting;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.screens.InAppBaseFragment;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.mockito.Mock;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * Created by philips on 5/2/17.
 */

public class DemoTestActivity extends AppCompatActivity implements IAPListener, ActionBarListener {
    private Resources resources;


    @Mock
    private IAPUser iapUserMock;
    private InAppIdlingResource mIdlingResource;

    @Override
    public Resources getResources() {
        if (resources == null) {
            resources = new IAPResources(super.getResources());
        }
        return resources;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
        if (resources != null) {
            // The real (and thus managed) resources object was already updated
            // by ResourcesManager, so pull the current metrics from there.
            final DisplayMetrics newMetrics = super.getResources().getDisplayMetrics();
            resources.updateConfiguration(newConfig, newMetrics);
        }
    }

    public static String IAP_VIEW = "IAP_VIEW";
    public static String IAP_PRODUCTS = "IAP_PRODUCT";
    public static String IAP_THEME = "IAP_THEME";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iap_activity);
    }

    public void switchFragment(final InAppBaseFragment fragment, final Bundle bundle) {
//        runOnUiThread(
//                new Runnable() {
//                    @Override
//                    public void run() {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        fragment.setActionBarListener(this, this);
        fragmentTransaction.replace(R.id.fl_mainFragmentContainer, fragment);
        fragmentTransaction.commitAllowingStateLoss();
//    }
//                });
    }


    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new InAppIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onGetCartCount(int count) {

    }

    @Override
    public void onUpdateCartCount() {

    }

    @Override
    public void updateCartIconVisibility(boolean shouldShow) {

    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> productList) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onSuccess(boolean bool) {

    }




    @Override
    public void onFailure(int errorCode) {

    }

    @Override
    public void updateActionBar(int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }
}
