package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.cdp.di.iap.BaseTest;
import com.philips.cdp.di.iap.DemoTestActivity;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.matcher.TextViewPropertiesMatchers;
import com.philips.cdp.di.iap.matcher.ViewPropertiesMatchers;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@Ignore
public class ProductDetailFragmentTest extends BaseTest {

    public ActivityTestRule<DemoTestActivity> mActivityTestRule = new ActivityTestRule<>(DemoTestActivity.class);
    private Resources resources;
    private Context activityContext;

    @Before
    public void setUp() {
        turnOnScreen();
        DemoTestActivity activity;
        activity = mActivityTestRule.launchActivity(getLaunchIntent(IAPFlows.IAP_PRODUCT_DETAIL_VIEW, CTNs, R.style.Theme_DLS_GroupBlue_UltraLight));
        //  activity.switchTo(com.philips.platform.uid.test.R.layout.main_layout);
        activity.switchFragment(new ProductDetailFragment(), null);
        resources = activity.getResources();
        activityContext = activity;
    }


    //*********************************Label layout TestScenarios**************************//

    @Test
    public void verifyPaddingStartDetailScreen() {
        float expectedPadding = resources.getDimensionPixelSize(R.dimen.border16_margin_left_right);
        getRelativeLayoutForDetailScreen().check(matches(ViewPropertiesMatchers.isSameStartPadding((int) expectedPadding)));
    }

    @Test
    public void verifyPaddingEndDetailScreen() {
        float expectedPadding = resources.getDimensionPixelSize(R.dimen.border16_margin_left_right);
        getRelativeLayoutForDetailScreen().check(matches(ViewPropertiesMatchers.isSameEndPadding((int) expectedPadding)));
    }

    @Test
    public void verifyPaddingTopDetailScreen() {
        float expectedPadding = resources.getDimensionPixelSize(R.dimen.border24_margin);
        getRelativeLayoutForDetailScreen().check(matches(ViewPropertiesMatchers.isSameTopPadding((int) expectedPadding)));
    }


    @Test
    public void verifyProductDiscriptionTextFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(com.philips.cdp.di.iap.R.dimen.iap_product_description);
        getTextLabelForProductDescription().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyCTNTextFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(R.dimen.iap_product_ctn_12);
        getTextLabelForCTN().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyTopPaddingForCTN() {
        float expectedTopPadding = resources.getDimensionPixelSize(R.dimen.border8_margin);
        getTextLabelForCTN().check(matches(ViewPropertiesMatchers.isSameTopPadding((int) expectedTopPadding)));
    }

    @Test
    public void verifyDiscountedPriceTextFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(R.dimen.iap_product_ctn_12);
        getTextLabelForDiscountedPrice().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyIndividualPriceTextFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(R.dimen.iap_product_original_price);
        getTextLabelForIndividualPrice().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyProductOverviewTextFontSize() {
        float expectedFontSize = resources.getDimensionPixelSize(R.dimen.uid_label_text_size);
        getTextLabelForProductOverview().check(matches(TextViewPropertiesMatchers.isSameFontSize((int) expectedFontSize)));
    }

    @Test
    public void verifyBuyFromRetailerButtonFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.cdp.di.iap.R.dimen.uid_label_text_size);
        getBuyFromRetailerButton().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyAddToCartButtonFontSize() {
        int expectedFontSize = resources.getDimensionPixelSize(com.philips.cdp.di.iap.R.dimen.uid_label_text_size);
        getAddToCartButton().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedFontSize)));
    }

    @Test
    public void verifyViewPagerHeight() {
        int expectedHeight = resources.getDimensionPixelSize(R.dimen.iap_product_detail_image_height);
        getViewPagerForDetailImage().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyTopMarginForViewPager() {
        int expectedRightMargin = (int) Math.ceil(resources.getDimensionPixelSize(R.dimen.border24_margin));
        getViewPagerForDetailImage().check(matches(ViewPropertiesMatchers.isSameTopMargin(expectedRightMargin)));
    }

    @Test
    public void verifyTopMarginForCircleIndicator() {
        int expectedRightMargin = (int) Math.ceil(resources.getDimensionPixelSize(R.dimen.border24_margin));
        getCircleIndicatorForViewPager().check(matches(ViewPropertiesMatchers.isSameTopPadding(expectedRightMargin)));
    }

    @Test
    public void verifyBottomMarginForCircleIndicator() {
        int expectedRightMargin = (int) Math.ceil(resources.getDimensionPixelSize(R.dimen.border24_margin));
        getCircleIndicatorForViewPager().check(matches(ViewPropertiesMatchers.isSameBottomPadding(expectedRightMargin)));
    }

    private ViewInteraction getTextLabelForProductDescription() {
        return onView(withId(com.philips.cdp.di.iap.R.id.iap_productDetailScreen_productDescription_lebel));
    }

    private ViewInteraction getTextLabelForCTN() {
        return onView(withId(R.id.iap_productDetailsScreen_ctn_lebel));
    }

    private ViewInteraction getTextLabelForDiscountedPrice() {
        return onView(withId(R.id.iap_productCatalogItem_discountedPrice_lebel));
    }

    private ViewInteraction getTextLabelForIndividualPrice() {
        return onView(withId(R.id.iap_productDetailsScreen_individualPrice_lebel));
    }

    private ViewInteraction getTextLabelForProductOverview() {
        return onView(withId(R.id.iap_productDetailsScreen_productOverview));
    }

    private ViewInteraction getAddToCartButton() {
        return onView(withId(R.id.iap_productDetailsScreen_addToCart_button));
    }

    private ViewInteraction getBuyFromRetailerButton() {
        return onView(withId(R.id.iap_productDetailsScreen_buyFromRetailor_button));
    }

    private ViewInteraction getScrollViewForProductDetail() {
        return onView(withId(R.id.scrollView));
    }

    private ViewInteraction getViewPagerForDetailImage() {
        return onView(withId(R.id.pager));
    }

    private ViewInteraction getCircleIndicatorForViewPager() {
        return onView(withId(R.id.indicator));
    }

    private ViewInteraction getRelativeLayoutForDetailScreen() {
        return onView(withId(R.id.detail_layout));
    }
}