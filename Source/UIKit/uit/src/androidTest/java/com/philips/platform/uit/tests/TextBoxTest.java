/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.uit.tests;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uit.R;
import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.DrawableMatcher;
import com.philips.platform.uit.matcher.FunctionDrawableMatchers;
import com.philips.platform.uit.utils.TestConstants;
import com.philips.platform.uit.view.widget.TextEditBox;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class TextBoxTest {

    private Resources testResources;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp() {
        final BaseTestActivity activity = mActivityTestRule.getActivity();
        activity.switchTo(com.philips.platform.uit.test.R.layout.layout_textbox);
        testResources = getInstrumentation().getContext().getResources();
    }

    @Test
    public void verifySameTextEditBoxRadiusOnDynamicCreation() {
        TextEditBox textEditBox = new TextEditBox(mActivityTestRule.getActivity());
        float expectedRadius = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.texteditbox_corner_radius);
        Drawable strokeDrawable = ((LayerDrawable) textEditBox.getBackground()).findDrawableByLayerId(R.id.uit_texteditbox_stroke_drawable);
        DrawableMatcher.isSameRadius(0, expectedRadius).matches(strokeDrawable);
    }

    @Test
    public void verifyTextEditBoxStrokeBackgroundRadius() {
        float expectedRadius = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.texteditbox_corner_radius);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, expectedRadius, R.id.uit_texteditbox_stroke_drawable)));
    }

    @Test
    public void verifyTextEditBoxFillBackgroundRadius() {
        float expectedRadius = (int) testResources.getDimension(com.philips.platform.uit.test.R.dimen.texteditbox_corner_radius);
        getTextBox().check(matches(FunctionDrawableMatchers.isSameRadius(TestConstants.FUNCTION_GET_BACKGROUND, 0, expectedRadius, R.id.uit_texteditbox_fill_drawable)));
    }

    private ViewInteraction getTextBox() {
        return onView(withId(com.philips.platform.uit.test.R.id.textBox));
    }
}