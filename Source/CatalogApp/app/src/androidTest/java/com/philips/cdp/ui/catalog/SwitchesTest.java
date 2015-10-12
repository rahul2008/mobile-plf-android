/*
package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsPixelAsExpectedMatcher.isImageSimilar;


*/
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 *//*

public class SwitchesTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public SwitchesTest() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testSwitchIsDisplayed(){
        onView(withText("Controls")).perform(click());
        onView(withId(R.id.switch)).check(matches(isDisplayed()));
    }

    public void testDBThemeBGColorSwitchOff() {
        // Apply Dark blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        //Verify the background/outline color
        onView(withText("Controls")).perform(click());
        onView(withId(R.id.switch)) // change id
                .check(matches(isOutlineColorSimilar("#03478")));
    }

    public void testBOThemeBGColorSwitchOff() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        //Verify the background/outline color
        onView(withText("Controls")).perform(click());
        onView(withId(R.id.switch)) // change id
                .check(matches(isOutlineColorSimilar("#e9830")));
    }

    public void testSwitchOffPixelPerfectMdpi() {
        onView(withText("Controls")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.switch_off_mdpi) ;
        onView(withId(R.id.switch))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testDBThemeBGColorSwitchOn(){
        // Apply Dark blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        //Verify the background/outline color
        onView(withText("Controls")).perform(click());
        onView(withId(R.id.switch)).perform(click());
        onView(withId(R.id.switch)) // change id
        .check(matches(isOutlineColorSimilar("#5b8f22")));
    }

    public void testBOThemeBGColorSwitchOn(){
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        //Verify the background/outline color
        onView(withText("Controls")).perform(click());
        onView(withId(R.id.switch)).perform(click());
        onView(withId(R.id.switch)) // change id
        .check(matches(isOutlineColorSimilar("#5b8f22")));
    }

    public void testSwitchOnPixelPerfectMdpi() {
        onView(withText("Controls")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.switch_on_mdpi) ;
        onView(withId(R.id.switch)).perform(click());
        onView(withId(R.id.switch))
        .check(matches(isImageSimilar(expectedBitmap)));
    }

}
*/
