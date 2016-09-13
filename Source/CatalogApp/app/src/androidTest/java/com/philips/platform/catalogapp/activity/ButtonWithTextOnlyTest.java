package com.philips.platform.catalogapp.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.utils.GradientDrawableUtils;
import com.philips.platform.uit.view.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ButtonWithTextOnlyTest {

    private Button button;
    private Resources testResources;
    private Drawable backgroundDrawable;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp(){
        button = new Button(mActivityTestRule.getActivity());
        backgroundDrawable = button.getBackground();
        testResources = getInstrumentation().getContext().getResources();
    }

    /************************************************Layout************************************************/

    @Test
    public void verifyButtonHeight(){
        int expectedHeight = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_height);
        assertEquals(expectedHeight, button.getMinHeight());
    }

    @Test
    public void verifyButtonLeftPadding(){
        int expectedLeftPadding = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_left_padding);
        assertEquals(expectedLeftPadding, button.getPaddingLeft());
    }

    @Test
    public void verifyButtonRightPadding(){
        int expectedRightPadding = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_right_padding);
        assertEquals(expectedRightPadding, button.getPaddingLeft());

    }

    @Test
    public void verifyButtonCornerRadius(){
        GradientDrawableUtils.StateColors stateColors = GradientDrawableUtils.getStateColors(backgroundDrawable);
        float radius = (float) Math.ceil(testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_cornerradius));
        assertEquals(radius, stateColors.getCornerRadius()[0]);

    }

    @Test
    public void verifyButtonFontType(){

    }

    @Test
    public void verifyButtonFontSize(){
        int expectedFontSize = (int)(testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.button_font_size));
        assertEquals(expectedFontSize, (int)button.getTextSize());
    }

    /******************************************************Theming********************************************/

    @Test
    public void verifyButtonFontColor(){


    }

        }
