package com.philips.cdp.di.iap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.ControllerFactory;
import com.philips.cdp.di.iap.mocks.TestUtils;
import com.philips.cdp.di.iap.screens.ProductCatalogFragment;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@Ignore
@RunWith(AndroidJUnit4.class)
public class IntegrationTest extends BaseTest {

    public ActivityTestRule<DemoTestActivity> mActivityTestRule = new ActivityTestRule<>(DemoTestActivity.class);
    private Resources resources;
    private Context activityContext;
    ArrayList<String> ctns;

    @Before
    public void setUp() {

        turnOnScreen();
        DemoTestActivity activity;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        ControllerFactory.getInstance().init(true);
        ctns = new ArrayList<>();
        ctns.add("HX8332/11");
        activity = mActivityTestRule.launchActivity(getLaunchIntent(IAPFlows.IAP_PRODUCT_CATALOG_VIEW, CTNs, R.style.Theme_DLS_GroupBlue_UltraLight));
        CartModelContainer.getInstance().setAppInfraInstance(new AppInfra.Builder().build(activity));
//        activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS, ctns);
        activity.switchFragment(new ProductCatalogFragment(), bundle);
        resources = activity.getResources();
        activityContext = activity;
        HybrisDelegate.getInstance().getStore().setLangAndCountry("en", "US");
    }

    @Test
    public void verifyClickOnProductList() {
        TestUtils.pauseTestFor(1000);
        onView(withId(R.id.product_catalog_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        TestUtils.pauseTestFor(1000);
        onView(withId(R.id.iap_productDetailsScreen_buyFromRetailor_button)).perform(click());
    }

}
