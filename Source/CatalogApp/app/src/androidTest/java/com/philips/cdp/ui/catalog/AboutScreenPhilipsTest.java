package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftAlignedWith;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class AboutScreenPhilipsTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public AboutScreenPhilipsTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testAboutTitle(){
        onView(withText("About Screen")).perform(click());
        onView(withText("Philips")).perform(click());
        onView(withText("Mobile app")).check(matches(isDisplayed()));
        onView(withId(R.id.about_title)).check(matches(isTextColorSimilar("#ffffff")));
        onView(withId(R.id.about_title))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_title_text_size))));
        onView(withId(R.id.about_title)).check(isLeftAlignedWith(withId(R.id.about_screen)));
    }

    public void testAboutVersion(){
        onView(withText("About Screen")).perform(click());
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_version)).check(matches(isTextColorSimilar("#ffffff")));
       onView(withId(R.id.about_version))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_version_text_size))));
        onView(withId(R.id.about_version)).check(isLeftAlignedWith(withId(R.id.about_screen)));
            }

    public void testCopyright(){
        onView(withText("About Screen")).perform(click());
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_copyright)).check(matches(isTextColorSimilar("#ffffff")));
        onView(withId(R.id.about_copyright))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_version_text_size))));
        onView(withId(R.id.about_copyright)).check(isLeftAlignedWith(withId(R.id.about_screen)));
    }

    public void testTermsAndConditions(){
        onView(withText("About Screen")).perform(click());
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_terms)).check(matches(isTextColorSimilar("#ffffff")));
        onView(withId(R.id.about_terms))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_terms_content_text_size))));
        onView(withId(R.id.about_terms)).check(isLeftAlignedWith(withId(R.id.about_screen)));
    }

    public void testPrivacyPolicy(){
        onView(withText("About Screen")).perform(click());
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_policy)).check(matches(isTextColorSimilar("#ffffff")));
        onView(withId(R.id.about_policy))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_terms_content_text_size))));
    }

    public void testContent(){
        onView(withText("About Screen")).perform(click());
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_content)).check(matches(isTextColorSimilar("#ffffff")));
        onView(withId(R.id.about_content))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_terms_content_text_size))));
        onView(withId(R.id.about_content)).check(isLeftAlignedWith(withId(R.id.about_screen)));
    }

    public void testPhilipsLogo(){
        onView(withText("About Screen")).perform(click());
        onView(withText("Philips")).perform(click());

        // verify whether philips logo is pixel perfect
/*
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.philips_shield);
        onView(withId(com.philips.cdp.uikit.R.id.about_screen_logo))
                .check(matches(isImageSimilar(expectedBitmap)));
*/

        // verify philips logo color
        onView(withId(R.id.about_screen_logo)).check(matches(isBackgroundColorSimilar("#ffffff")));

        // Verify height of philips logo
        onView(withId(R.id.about_screen_logo))
                .check(matches(isHeightSimilar(85)));
    }

    public void testChangeThemeBGColorTextColor(){
        //set the theme to Orange
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        onView(withText("About Screen")).perform(click());
        onView(withText("Philips")).perform(click());
        onView(withText("Mobile app")).check(matches(isDisplayed()));
        onView(withId(R.id.about_title)).check(matches(isTextColorSimilar("#ffffff")));

    }

}
