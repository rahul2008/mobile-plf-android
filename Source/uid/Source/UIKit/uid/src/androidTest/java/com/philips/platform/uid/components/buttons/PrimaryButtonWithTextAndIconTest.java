/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.uid.components.buttons;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;

import com.philips.platform.uid.activity.BaseTestActivity;
import com.philips.platform.uid.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uid.view.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.philips.platform.uid.utils.UIDTestUtils.waitFor;

public class PrimaryButtonWithTextAndIconTest {

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);
    private Resources testResources;
    BaseTestActivity activity;

    @Before
    public void setUp() {
        mActivityTestRule.getActivity().switchTo(com.philips.platform.uid.test.R.layout.layout_buttons);
        testResources = getInstrumentation().getContext().getResources();
        activity = mActivityTestRule.getActivity();
    }

    /************************************************
     * Layout
     ************************************************/

    @Test
    public void verifyTextandIconButtonCompoundPadding() {
        waitFor(testResources, 750);
        int expectedCompoundPadding = testResources.getDimensionPixelSize(com.philips.platform.uid.test.R.dimen.iconandtextbutton_compoundpadding);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button button = (Button) activity.findViewById(com.philips.platform.uid.test.R.id.primary_image_text_button);
                button.setImageDrawable(activity.getResources().getDrawable(android.R.drawable.ic_dialog_info, activity.getTheme()));
            }
        });
        getIconandTextButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawablePadding(expectedCompoundPadding)));
    }

    /************************************************
     * Theming
     ************************************************/

    private ViewInteraction getIconandTextButton() {
        return onView(withId(com.philips.platform.uid.test.R.id.primary_image_text_button));
    }
}
