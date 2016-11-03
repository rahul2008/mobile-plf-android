/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.components.Buttons;

import android.content.res.Resources;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.matcher.TextViewPropertiesMatchers;
import com.philips.platform.uit.view.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.platform.uit.utils.UITTestUtils.waitFor;

public class PrimaryButtonWithTextAndIconTest {

    private Button button;
    private Resources testResources;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp(){
        mActivityTestRule.getActivity().switchTo(com.philips.platform.uit.test.R.layout.layout_buttons);
        button = new Button(mActivityTestRule.getActivity());
        testResources = getInstrumentation().getContext().getResources();
    }

    /************************************************Layout************************************************/

    @Test
    public void verifyTextandIconButtonCompoundPadding() {
        waitFor(testResources, 750);
        int expectedCompoundPadding = (int) (testResources.getDimension(com.philips.platform.uit.test.R.dimen.iconandtextbutton_compoundpadding));
        getIconandTextButton().check(matches(TextViewPropertiesMatchers.isSameCompoundDrawablePadding(expectedCompoundPadding)));
    }

    /************************************************Theming************************************************/

    private ViewInteraction getIconandTextButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.demo_image_text_button));
    }

        }
