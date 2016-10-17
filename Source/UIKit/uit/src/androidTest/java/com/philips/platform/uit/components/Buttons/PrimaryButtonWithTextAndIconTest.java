package com.philips.platform.uit.components.Buttons;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.view.widget.Button;

import org.junit.Before;
import org.junit.Rule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PrimaryButtonWithTextAndIconTest {

    private Button button;
    private Resources testResources;
    private Drawable backgroundDrawable;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        button = new Button(mActivityTestRule.getActivity());
        backgroundDrawable = button.getBackground();
        testResources = getInstrumentation().getContext().getResources();
    }

    /************************************************Layout************************************************/

    /************************************************Theming************************************************/

}
