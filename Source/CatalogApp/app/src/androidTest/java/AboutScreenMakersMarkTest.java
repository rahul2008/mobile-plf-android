import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class AboutScreenMakersMarkTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public AboutScreenMakersMarkTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testAboutTitle() {
        onView(withText("About Screen")).perform(click());
        onView(withId(R.id.about_screen_mm)).perform(click());
        onView(withText("Mobile app")).check(matches(isDisplayed()));
        onView(withId(R.id.about_title)).check(matches(isTextColorSimilar("#f204b")));
        onView(withId(R.id.about_title))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_title_text_size))));
    }

    public void testAboutVersion() {
        onView(withText("About Screen")).perform(click());
        onView(withText("Maker's Mark")).perform(click());
        onView(withId(R.id.about_version)).check(matches(isTextColorSimilar("#f204b")));
        onView(withId(R.id.about_version))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_version_text_size))));
    }

    public void testCopyright() {
        onView(withText("About Screen")).perform(click());
        onView(withText("Maker's Mark")).perform(click());
        onView(withId(R.id.about_copyright)).check(matches(isTextColorSimilar("#f204b")));
        onView(withId(R.id.about_copyright))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_version_text_size))));
    }

    public void testTermsAndConditions() {
        onView(withText("About Screen")).perform(click());
        onView(withText("Maker's Mark")).perform(click());
        onView(withId(R.id.about_terms)).check(matches(isTextColorSimilar("#f204b")));
        onView(withId(R.id.about_terms))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_terms_content_text_size))));
    }

    public void testPrivacyPolicy() {
        onView(withText("About Screen")).perform(click());
        onView(withText("Maker's Mark")).perform(click());
        onView(withId(R.id.about_policy)).check(matches(isTextColorSimilar("#f204b")));
        onView(withId(R.id.about_policy))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_terms_content_text_size))));
    }

    public void testContent() {
        onView(withText("About Screen")).perform(click());
        onView(withText("Maker's Mark")).perform(click());
        onView(withId(R.id.about_content)).check(matches(isTextColorSimilar("#f204b")));
        onView(withId(R.id.about_content))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_terms_content_text_size))));
    }

    public void testChangeThemeAndTestTextColor() {
        //set the theme to Orange
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();
        onView(withText("About Screen")).perform(click());
        onView(withText("Maker's Mark")).perform(click());
        onView(withId(R.id.about_title)).check(matches(isTextColorSimilar("#f204b")));
    }
}
