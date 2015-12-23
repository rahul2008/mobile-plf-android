import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.SocialIconsActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsDimensionAsExpectedMatcher.isDimensionSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */


public class SocialIconsRegular20pxTest extends ActivityInstrumentationTestCase2<SocialIconsActivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private SocialIconsActivity socialIconsActivity;
    private ThemeUtils themeUtils;

    public SocialIconsRegular20pxTest() {
        super(SocialIconsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        socialIconsActivity = getActivity();
        SharedPreferences preferences = socialIconsActivity.getSharedPreferences(socialIconsActivity.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        socialIconsActivity.setResult(1);
        intent = new Intent(socialIconsActivity, SocialIconsActivity.class);
        socialIconsActivity.startActivity(intent);
        socialIconsActivity.finish();
    }

    public void testDBThemeTwitterIconPixelPerfect() {
        onView(withId(R.id.uikit_social_twitter_20)).check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_width))));
    }

    public void testDBThemeFacebookIconPixelPerfect() {
        onView(withId(R.id.uikit_social_facebook_20)).check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_width))));
    }

    public void testDBThemeYoutubeIconPixelPerfect() {
        onView(withId(R.id.uikit_social_youtube_20)).check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_width))));
    }

    public void testDBThemePininterestIconPixelPerfect() {
        onView(withId(R.id.uikit_social_pininterest_20)).check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_width))));
    }

    public void testDBThemeLinkedinIconPixelPerfect() {
        onView(withId(R.id.uikit_social_linkedin_20)).check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.socialicons_width))));
    }

    public void testBOThemeBGColorAsExpected() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.uikit_social_youtube_20)).check(matches(isBackgroundColorSimilar("#e9830", 3, 10)));
    }

//    public void testBAThemeBGColorAsExpected() {
//        themeUtils.setThemePreferences("aqua|false|solid|0");
//        relaunchActivity();
//        onView(withId(R.id.uikit_social_youtube_20)).check(matches(isBackgroundColorSimilar("#1e9d8b",5,11)));
//
//    }
}
