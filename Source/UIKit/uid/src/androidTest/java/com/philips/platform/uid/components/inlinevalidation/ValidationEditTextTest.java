package com.philips.platform.uid.components.inlinevalidation;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uid.R;
import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.components.BaseTest;
import com.philips.platform.uid.matcher.FunctionDrawableMatchers;
import com.philips.platform.uid.utils.TestConstants;
import com.philips.platform.uid.utils.UIDTestUtils;
import com.philips.platform.uid.view.widget.ValidationEditText;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ValidationEditTextTest extends BaseTest{

    Resources testResources;

    @Rule
    public ActivityTestRule<BaseTestActivity> testRule = new ActivityTestRule<BaseTestActivity>(BaseTestActivity.class, false, false);
    private BaseTestActivity activity;

    @Before
    public void setUp() throws Exception {
        final Intent launchIntent = getLaunchIntent(1, 0);
        activity = testRule.launchActivity(launchIntent);
        activity.switchTo(com.philips.platform.uid.test.R.layout.layout_validation_text);
        testResources = activity.getResources();
    }

    @Test
    public void verifyErrorTextBoxBoderColor() {
        int expectedTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidTextBoxDefaultValidatedBorderColor);
        ValidationEditText validationEditText = new ValidationEditText(activity);
        validationEditText.setError(true);
        int[] attrs = {R.attr.uid_state_error, android.R.attr.state_enabled, -android.R.attr.state_focused};
        assertTrue(FunctionDrawableMatchers.isSameStrokeColor(TestConstants.FUNCTION_GET_BACKGROUND, attrs, expectedTextColor, R.id.uid_texteditbox_stroke_drawable).matches(validationEditText));
    }

    @Test
    public void verifyErrorTextBackgroundColor() {
        int expectedTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidTextBoxDefaultValidatedBackgroundColor);
        ValidationEditText validationEditText = new ValidationEditText(activity);
        validationEditText.setError(true);
        int[] attrs = {R.attr.uid_state_error, android.R.attr.state_enabled, -android.R.attr.state_focused};
        assertTrue(FunctionDrawableMatchers.isSameColor(TestConstants.FUNCTION_GET_BACKGROUND, attrs, expectedTextColor, R.id.uid_texteditbox_fill_drawable, false).matches(validationEditText));
    }

    @Test
    public void verifyEditErrorTextColor() {
        int expectedTextColor = UIDTestUtils.getAttributeColor(activity, R.attr.uidTextBoxDefaultValidatedTextColor);
        ValidationEditText validationEditText = new ValidationEditText(activity);
        validationEditText.setError(true);
        int[] attrs = {R.attr.uid_state_error, android.R.attr.state_enabled, -android.R.attr.state_focused};
        assertTrue(FunctionDrawableMatchers.isSameColorFromColorList(TestConstants.FUNCTION_GET_TEXT_COLORS, attrs, expectedTextColor).matches(validationEditText));
    }
}