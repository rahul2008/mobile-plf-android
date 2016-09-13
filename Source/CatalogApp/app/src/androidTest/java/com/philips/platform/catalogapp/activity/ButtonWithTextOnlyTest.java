/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.graphics.ColorUtils;

import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.utils.GradientDrawableUtils;
import com.philips.platform.uit.view.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;

public class ButtonWithTextOnlyTest {

    private Button button;
    private Resources testResources;
    private Drawable backgroundDrawable;
    private Context context;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        button = new Button(mActivityTestRule.getActivity());
        backgroundDrawable = button.getBackground();
        testResources = getInstrumentation().getContext().getResources();
        context = mActivityTestRule.getActivity();
    }

    /*************************************************
     * Layout Scenarios
     ************************************************/

    @Test
    public void verifyButtonHeight() {
        int expectedHeight = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_height);
        assertEquals(expectedHeight, button.getMinHeight());
    }

    @Test
    public void verifyButtonLeftPadding() {
        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_left_padding);
        assertEquals(expectedLeftPadding, button.getPaddingLeft());
    }

    @Test
    public void verifyButtonRightPadding() {
        int expectedRightPadding = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_right_padding);
        assertEquals(expectedRightPadding, button.getPaddingLeft());
    }

    @Test
    public void verifyButtonCornerRadius() {
        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(backgroundDrawable);
        float radius = (float) Math.ceil(testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_cornerradius));
        assertEquals(radius, stateColors.getCornerRadius()[0]);
    }

    @Test
    public void verifyButtonFontType() {

    }

    @Test
    public void verifyButtonFontSize() {
        int expectedFontSize = (int) (testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_font_size));
        assertEquals(expectedFontSize, (int) button.getTextSize());
    }

    /*******************************************************
     * Theming
     ******************************************************/

    @Test
    public void verifyPrimaryTextOnlyButtonControlColorULTone() {
        final ColorStateList tintList = button.getSupportBackgroundTintList();
        int actualColor = tintList.getColorForState(new int[]{android.R.attr.state_enabled}, -1);
        assertEquals(Color.parseColor("#1474A4"), actualColor);
    }

    @Test
    public void verifyPrimaryTextOnlyPressedButtonControlColorULTone() {
        final ColorStateList tintList = button.getSupportBackgroundTintList();
        int actualColor = tintList.getColorForState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, -1);
        assertEquals(Color.parseColor("#439AC1"), actualColor);
    }

    @Test
    public void verifyPrimaryTextOnlyDisabledButtonControlColorULTone() {
        final ColorStateList tintList = button.getSupportBackgroundTintList();
        int actualColor = tintList.getColorForState(new int[]{-android.R.attr.state_enabled}, -1);
        final int enabledColor = Color.parseColor("#1474A4");
        assertEquals(modulateColorAlpha(enabledColor, 0.25f), actualColor);
    }

    private static int modulateColorAlpha(int color, float alphaMod) {
        return ColorUtils.setAlphaComponent(color, Math.round(Color.alpha(color) * alphaMod));
    }

    @Test
    public void verifyPrimaryTextOnlyButtonFontColor() {
        button.setEnabled(true);
        final int textColors = button.getCurrentTextColor();
        int actualTextColor = textColors;
        assertEquals(Color.parseColor("#ffffff"), actualTextColor);
    }

    @Test
    public void verifyPrimaryTextOnlyPressedButtonFontColor() {
        button.setPressed(true);
        final int textColors = button.getCurrentTextColor();
        int actualTextColor = textColors;
        assertEquals(Color.parseColor("#ffffff"), actualTextColor);
    }

    @Test
    public void verifyPrimaryTextOnlyDisabledButtonFontColor() {
        button.setEnabled(false);
        final int textColors = button.getCurrentTextColor();
        int actualTextColor = textColors;
        final int disabledTextColor = Color.parseColor("#ffffff");
        assertEquals(modulateColorAlpha(disabledTextColor, 0.35f), actualTextColor);
    }
}

