package com.philips.platform.uid.components.buttons;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.util.TypedValue;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.matcher.ViewPropertiesMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.Button;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;

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
    public void verifyCircularButtonLargeWidth() {
        int expectedWidth = resources.getDimensionPixelSize(R.dimen.circular_button_large_height_width);
        getButton().check(matches(ViewPropertiesMatchers.isSameViewWidth(expectedWidth)));
    }

    @Test
    public void verifyCircularButtonLargeHeight() {
        int expectedHeight = resources.getDimensionPixelSize(R.dimen.circular_button_large_height_width);
        getButton().check(matches(ViewPropertiesMatchers.isSameViewHeight(expectedHeight)));
    }

    @Test
    public void verifyCircularButtonIconLargeSize() {
        int expectedIconSize = resources.getDimensionPixelSize(R.dimen.circular_button_text_size);
        getButton().check(matches(TextViewPropertiesMatchers.isSameFontSize(expectedIconSize)));
    }

    @Test
    public void verifyCBLargeNormalBackgroundColor(){
        UIDTestUtils.waitFor(resources, 1000);
        int expectedColor;
        if(isAccentButton())
            expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonAccentNormalBackgroundColor);
        else
            expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryNormalBackgroundColor);
        getButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorList(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, android.R.attr.state_enabled, expectedColor)));
    }


  @Test
    public void verifyCBLargePressedBackgroundColor(){
      UIDTestUtils.waitFor(resources, 1000);
      int expectedColor;
      if(isAccentButton())
          expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonAccentPressedBackgroundColor);
      else
          expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryPressedBackgroundColor);
      getButton().check(matches(FunctionDrawableMatchers
              .isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 0, expectedColor)));
    }

     @Test
    public void verifyCBLargeDisabledBackgroundColor(){
         UIDTestUtils.waitFor(resources, 1000);
         int expectedColor;
         if(isAccentButton())
             expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonAccentDisabledBackgroundColor);
         else
             expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryDisabledBackgroundColor);
        getButton().check(matches(FunctionDrawableMatchers
                .isSameColorFromColorListWithReflection(TestConstants.FUNCTION_GET_SUPPORT_BACKROUND_TINT_LIST, 1, expectedColor)));
    }

    @Test
    public void verifyCBLargeNormalIconColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryNormalTextColor);
        getButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(android.R.attr.state_enabled, expectedColor)));
    }

    @Test
    public void verifyCBLargePressedIconColor() {
        final int expectedColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryNormalTextColor);
        getButton().check(matches(TextViewPropertiesMatchers.isSameTextColorWithReflection(1, expectedColor)));
    }

    @Test
    public void verifyCBLargeDisabledIconColor() {
        final int disabledTextColor = UIDTestUtils.getAttributeColor(mContext, R.attr.uidButtonPrimaryDisabledTextColor);
        getButton().check(matches(TextViewPropertiesMatchers.isSameTextColor(-android.R.attr.enabled, disabledTextColor)));
    }

    private ViewInteraction getButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.circularImageButtonLarge));
    }

    private boolean isAccentButton(){
        Button  button = (Button) activity.findViewById(com.philips.platform.uid.test.R.id.circularImageButtonLarge);
        if(button != null)
            return button.isAccentButton();
        return false;
    }
}
