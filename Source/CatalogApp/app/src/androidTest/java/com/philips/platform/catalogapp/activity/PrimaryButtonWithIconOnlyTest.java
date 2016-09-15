package com.philips.platform.catalogapp.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.R;
import com.philips.platform.uit.view.widget.ImageButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PrimaryButtonWithIconOnlyTest {

    private ImageButton iconButton;
    private Resources testResources;
    private Drawable iconDrawable;
    private Context context;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private int parseColorWhite;

    @Before
    public void setUp() {
        iconButton = new ImageButton(mActivityTestRule.getActivity());
        testResources = getInstrumentation().getContext().getResources();
        context = mActivityTestRule.getActivity();
        iconButton.setImageDrawable(VectorDrawableCompat.create(context.getResources(), R.drawable.share, context.getTheme()));
        iconDrawable = iconButton.getCompoundDrawables()[0];
        final int parseColorWhite = Color.parseColor("#ffffff");
    }

    /************************************************
     * Layout
     ************************************************/

    @Test
    public void verifyButtonWithIconHeight() {
        int expectedIconButtonHeight = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.iconbutton_height);
        assertEquals(expectedIconButtonHeight, iconButton.getMinimumHeight());
    }

    @Test
    public void verifyButtonWithIconWidth() {
        int expectedIconButtonHeight = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.iconbutton_height);
        assertEquals(expectedIconButtonHeight, iconButton.getMinimumWidth());
    }

    @Test
    public void verifyIconHeight() {
        int expectedIconHeight = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.icon_height);
        final int bottom = iconDrawable.getBounds().bottom;
        final int top = iconDrawable.getBounds().top;
        int actualIconHeight = (bottom) - (top);
        assertEquals(expectedIconHeight, actualIconHeight);
    }

    @Test
    public void verifyIconWidth() {
        int expectedIconHeight = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.icon_height);
        final int right = iconDrawable.getBounds().right;
        final int left = iconDrawable.getBounds().left;
        int actualIconHeight = (right) - (left);

        assertEquals(expectedIconHeight, actualIconHeight);
    }

    @Test
    public void verifyButtonWithIconLeftPadding() {
        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.iconbutton_left_padding);
        assertEquals(expectedLeftPadding, iconButton.getPaddingLeft());
    }

    @Test
    public void verifyButtonWithIconRightPadding() {
        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.iconbutton_right_padding);
        assertEquals(expectedLeftPadding, iconButton.getPaddingRight());

    }

    @Test
    public void verifyCornerRadiusOfButtonWithIcon() {
//        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(iconDrawable);
//        float radius = (float) Math.ceil(testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.iconbutton_cornerradius));
//        assertEquals(radius, stateColors.getCornerRadius()[0]);

    }

    /************************************************
     * Theming
     ************************************************/

    @Test
    public void verifyIconButtonDefaultIconColor() {


    }
    @Test
    public void verifyIconButtonPressedIconColor() {


    }
    @Test
    public void verifyIconButtonDisabledIconColor() {


    }
}
