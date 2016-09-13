package com.philips.platform.catalogapp.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatImageButton;

import com.philips.platform.catalogapp.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ButtonWithIconOnlyTest {

    private AppCompatImageButton iconButton;
    private Resources testResources;
    private Drawable backgroundDrawable;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp(){
        iconButton = new AppCompatImageButton(mActivityTestRule.getActivity());
        backgroundDrawable = iconButton.getBackground();
        testResources = getInstrumentation().getContext().getResources();
    }

    /************************************************Layout************************************************/

//    @Test
//    public void verifyButtonWithIconHeight(){
//        int expectedIconButtonHeight = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.iconbutton_height);
//        assertEquals(expectedIconButtonHeight, iconButton.getHeight());
//    }
//
//    @Test
//    public void verifyIconHeight(){
//        int expectedIconHeight = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.icon_height);
//        assertEquals(expectedIconHeight, backgroundDrawable.getIntrinsicHeight());
//   }
//
//    @Test
//    public void verifyIconWidth(){
//        int expectedIconWidth = (int) testResources.getDimension(com.philips.platform.catalogapp.test.R.dimen.icon_width);
//        assertEquals(expectedIconWidth, backgroundDrawable.getIntrinsicWidth());
//
//    }

    @Test
    public void verifyCornerRadiusOfButtonWithIcon(){



    }

    /************************************************Theming************************************************/





        }
