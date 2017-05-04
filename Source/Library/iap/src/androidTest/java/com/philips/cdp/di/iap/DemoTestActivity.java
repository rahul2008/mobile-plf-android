package com.philips.cdp.di.iap;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.deps.guava.annotations.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.philips.cdp.di.iap.store.IAPUser;

import org.mockito.Mock;

import javax.annotation.Nullable;

/**
 * Created by philips on 5/2/17.
 */

public class DemoTestActivity extends AppCompatActivity {
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
//        IAPSettings mIAPSettings = new IAPSettings(this);
//
//        StoreListener mStore = (new MockStore(this, iapUserMock)).getStore(mIAPSettings);
//        mStore.initStoreConfig(/*"en", "GB",*/ null);
//        IAPDependencies mIapDependencies = new IAPDependencies(new AppInfra.Builder().build(this));
//        IAPInterface mIapInterface = new IAPInterface();
//        mIapInterface.init(mIapDependencies, mIAPSettings);
        //switchFragment(new ProductCatalogFragment());

    }

    public void switchFragment(final Fragment fragment) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fl_mainFragmentContainer, fragment);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                });
    }


    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new InAppIdlingResource();
        }
        return mIdlingResource;
    }

}
