package com.philips.platform.uid.components.buttons;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

public class CircularButtonLargeTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources resources;
    private BaseTestActivity activity;
    private Context mContext;

    @Before
    public void setUp() {
        activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_buttons);
        resources = getInstrumentation().getContext().getResources();
        mContext = activity;
    }

    @Test
    public void verifyCBLargeWidth() {
        int expectedWidth = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_circular_button_large_height_width);
        getButton().check(matches(ViewPropertiesMatchers.isSameViewWidth(expectedWidth)));
    }

    @Test
    public void verifyCBLargeHeight() {
        int expectedHeight = resources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.uid_circular_button_large_height_width);
        getButton().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyCBLargeIconHeight() {
        int expectedIconHeight = resources.getDimensionPixelSize(R.dimen.uid_imagebutton_image_size);
        getButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableHeight(0, expectedIconHeight)));
    }

    @Test
    public void verifyCBLargeIconWidth() {
        int expectedIconWidth = resources.getDimensionPixelSize(R.dimen.uid_imagebutton_image_size);
        getButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableWidth(0, expectedIconWidth)));
    }

    @Test
    public void verifyCBLargeCornerRadius() {
        float radius = (float) Math.floor(resources.getDimensionPixelSize(R.dimen.uid_circular_button_large_radius));
        getButton().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, radius)));

    }

    @Test
    public void verifyCBLargeNormalBackgroundColor(){
        int expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryNormalBackgroundColor);
        getButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, expectedColor)));
    }


  @Test
    public void verifyCBLargePressedBackgroundColor(){
      int expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryPressedBackgroundColor);
      getButton().check(matches(FunctionDrawableMatchers
              .isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 0, expectedColor)));
    }

     @Test
    public void verifyCBLargeDisabledBackgroundColor(){
        int expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryDisabledBackgroundColor);
        getButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 1, expectedColor)));
    }

    @Test
    public void verifyCBLargeNormalIconColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryNormalTextColor);
        getButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableColor(0,android.R.attr.state_enabled, expectedColor)));
    }


    @Test
    public void verifyCBLargeDisabledIconColor() {
        final int disabledTextColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryDisabledTextColor);
        getButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawableColor(0,-android.R.attr.enabled, disabledTextColor)));
    }

    @Test
    public void verufyCBLargeShadowLevel(){
        int expectedElevation = resources.getDimensionPixelSize(R.dimen.uid_circular_button_elevation);
        getButton().check(matches(ViewPropertiesMatchers.isSameElevation(expectedElevation)));
    }

    private ViewInteraction getButton() {
        return onView(ViewMatchers.withId(com.philips.platform.uid.test.R.id.circularButtonLarge));
    }
}
