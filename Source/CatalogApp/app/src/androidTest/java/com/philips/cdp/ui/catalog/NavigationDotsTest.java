package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.IsCircleRadiusAsExpectedMatcher.isCircleRadiusSimilar;
import static com.philips.cdp.ui.catalog.IsCircleBackgroundColorAsExpectedMatcher.isCircleColorSimilar;
import static com.philips.cdp.ui.catalog.IsOpacityAsExpectedMatcher.isOpacitySimilar;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NavigationDotsTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public NavigationDotsTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testNDotFocusedUnfocusedRadiusAsExpected() {
        onView(withText("Dot Navigation")).perform(click());
        onView(withId(R.id.indicator))
                .check(matches(isCircleRadiusSimilar(6,4)));
    }

    public void testNavigationDotSwipeFocusedRadiusAsExpected() {
        onView(withText("Dot Navigation")).perform(click());
        onView(withId(R.id.indicator)).perform(swipeLeft());
        onView(withId(R.id.indicator)).perform(swipeLeft());
        onView(withId(R.id.indicator))
                .check(matches(isCircleRadiusSimilar(6,4)));
    }

    public void testDBThemeFocusedBGColorAsExpected(){
        //Change theme to blue and verify the background color
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        //Verify the BG color
        onView(withText("Dot Navigation")).perform(click());
        onView(withId(R.id.indicator))
                .check(matches(isCircleColorSimilar("052120", "052120")));

    }

    public void testBOThemeFocusedBGColorAsExpected(){
        //Change theme to Bright orange and verify the background color
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        //Verify the BG color
        onView(withText("Dot Navigation")).perform(click());
        onView(withId(R.id.indicator))
                .check(matches(isCircleColorSimilar("2331310","2331310")));

    }

    public void testOpacityAsExpected(){
        //Verify the Opacity
        onView(withText("Dot Navigation")).perform(click());
        onView(withId(R.id.indicator))
                .check(matches(isOpacitySimilar(178)));

    }
}
