import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.AboutScreenLauncher;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
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

public class AboutScreenPhilipsTest extends ActivityInstrumentationTestCase2<AboutScreenLauncher> {

    private Resources testResources;
    private AboutScreenLauncher aboutScreenLauncher;
    private ThemeUtils themeUtils;

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;


    public AboutScreenPhilipsTest() {

        super(AboutScreenLauncher.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        aboutScreenLauncher = getActivity();
        SharedPreferences preferences = aboutScreenLauncher.getSharedPreferences(aboutScreenLauncher.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }


    private void relaunchActivity() {
        Intent intent;
        aboutScreenLauncher.setResult(1);
        intent = new Intent(aboutScreenLauncher, AboutScreenLauncher.class);
        aboutScreenLauncher.startActivity(intent);
        aboutScreenLauncher.finish();
    }

    public void testAboutScreenOptions() {
        onView(withText("Philips")).check(matches(isDisplayed()));
        onView(withText("Maker's Mark")).check(matches(isDisplayed()));
    }

    public void testAboutTitle() {
        onView(withText("Philips")).perform(click());
        onView(withText("Mobile app")).check(matches(isDisplayed()));
    }

    public void testAboutTitleTextColorAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_title)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testAboutTitleTextSizeAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_title))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_title_text_size))));
    }

    public void testAboutVersionTextColorAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_version)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testAboutVersionTextSizeAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_version))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_version_text_size))));
    }

    public void testCopyrightTextColorAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_copyright)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testCopyrightTextSizeAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_copyright))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_version_text_size))));
    }

    public void testTermsAndConditionsTextColorAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_terms)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testTermsAndConditionsTextSizeASExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_terms))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_terms_content_text_size))));
    }

    public void testPrivacyPolicyTextColorAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_policy)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testPrivacyPolicyTextSizeAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_policy))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_terms_content_text_size))));
    }

    public void testContentTextColorAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_content)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testContentTextSizeAsExpected() {
        onView(withText("Philips")).perform(click());
        onView(withId(R.id.about_content))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_terms_content_text_size))));
    }

/*    public void testPhilipsLogo() {
        onView(withText("Philips")).perform(click());

        // verify whether philips logo is pixel perfect
*//*
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.philips_shield);
        onView(withId(com.philips.cdp.uikit.R.id.about_screen_logo))
                .check(matches(isImageSimilar(expectedBitmap)));
*//*
        // Verify height of philips logo
        onView(withId(R.id.about_screen_logo))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.about_screen_philips_logo_height))));
    }*/

    public void testChangeThemeBOColorTextColor() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withText("Philips")).perform(click());
        onView(withText("Mobile app")).check(matches(isDisplayed()));
        onView(withId(R.id.about_title)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testChangeThemeBAColorTextColor() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withText("Philips")).perform(click());
        onView(withText("Mobile app")).check(matches(isDisplayed()));
        onView(withId(R.id.about_title)).check(matches(isTextColorSimilar("#ffffff")));
    }
}
