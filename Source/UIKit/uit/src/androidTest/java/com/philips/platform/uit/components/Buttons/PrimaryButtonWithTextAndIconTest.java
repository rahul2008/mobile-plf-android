/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.uit.components.Buttons;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.philips.platform.uit.activity.BaseTestActivity;
import com.philips.platform.uit.view.widget.Button;

import org.junit.Before;
import org.junit.Rule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class PrimaryButtonWithTextAndIconTest {

    private Button button;

    @Rule
    public ActivityTestRule<BaseTestActivity> mActivityTestRule = new ActivityTestRule<>(BaseTestActivity.class);

    @Before
    public void setUp(){
        button = new Button(mActivityTestRule.getActivity());
    }

    /************************************************Layout************************************************/


    /************************************************Theming************************************************/



    private ViewInteraction getIconandTextButton() {
        return onView(withId(com.philips.platform.uit.test.R.id.demo_image_text_button));
    }



        }
