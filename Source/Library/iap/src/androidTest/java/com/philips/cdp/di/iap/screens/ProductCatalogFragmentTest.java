package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.philips.cdp.di.iap.BaseTest;
import com.philips.cdp.di.iap.DemoTestActivity;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.matcher.TextViewPropertiesMatchers;
import com.philips.cdp.di.iap.matcher.ViewPropertiesMatchers;
import com.philips.cdp.di.iap.mocks.MockNetworkController;
import com.philips.cdp.di.iap.mocks.TestUtils;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.store.StoreListener;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@Ignore
public class ProductCatalogFragmentTest extends BaseTest {
    MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    public ActivityTestRule<DemoTestActivity> mActivityTestRule = new ActivityTestRule<>(DemoTestActivity.class);
    private Resources resources;
    private IdlingResource mIdlingResource;
    private Context mContext;
    @Mock
    private StoreListener mStore;

    @Before
    public void setUp() {
        turnOnScreen();
        DemoTestActivity activity = mActivityTestRule.launchActivity(getLaunchIntent(IAPFlows.IAP_PRODUCT_CATALOG_VIEW, CTNs, R.style.Theme_DLS_GroupBlue_UltraLight));
        mContext = activity;
        MockitoAnnotations.initMocks(this);
//        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore(new IAPSettings(mock(Context.class)));
//        mStore.initStoreConfig(/*"en", "us", */null);
//        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
//        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(mContext);

        TestUtils.getStubbedHybrisDelegate();
        TestUtils.getStubbedStore();
        //  activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new ProductCatalogFragment(), null);
        resources = activity.getResources();
        resources = activity.getResources();
        registerIdlingResources(activity);
    }

    public void registerIdlingResources(final DemoTestActivity baseTestActivity) {
        mIdlingResource = baseTestActivity.getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    private ViewInteraction getProductCatalogRecyclerView() {
        return onView(withId(R.id.product_catalog_recycler_view));
    }

    private ViewInteraction getProductCatalogTextView() {
        return onView(withId(R.id.iap_productCatalog_emptyProductCatalogText_lebel));
    }

    @Test
    public void verifyRecyclerViewIsVisible() {
        getProductCatalogRecyclerView().check(matches(ViewPropertiesMatchers.isVisible(View.VISIBLE)));
        // Assert.assertTrue(true);
    }

    @Test
    public void verifyProductOverviewTextFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(R.dimen.uid_label_text_size);
        getProductCatalogTextView().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }
}