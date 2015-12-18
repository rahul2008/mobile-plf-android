import android.app.Activity;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.SplashLauncher;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsDimensionAsExpectedMatcher.isDimensionSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class SplashScreenMobileTest extends ActivityInstrumentationTestCase2<SplashLauncher> {

    private Resources testResources;
    private SplashLauncher splashLauncher;
    private ThemeUtils themeUtils;

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;


    public SplashScreenMobileTest() {

        super(SplashLauncher.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        splashLauncher = getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testSplashScreenLogoAsExpected() {
        onView(withText("Logo Center, Title bottom")).perform(click());
       //        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.philips_shield);
        onView(withId(R.id.splash_logo))
                .check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.splash_logo_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.splash_logo_width))));
    }

    public void testSplashScreenTitleAsExpected() {
        onView(withText("Logo Center, Title bottom")).perform(click());
        onView(withId(R.id.splash_title))
                .check(matches(isTextColorSimilar("#ffffff")));
        onView(withId(R.id.splash_title))
                .check(matches(isTextSizeSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.splash_title_text_size))));
    }

    public void testSplashScreenLogoTopAligned() {
        onView(withText("Logo Top")).perform(click());
        onView(withId(R.id.splash_logo)).check(isAbove(withId(R.id.splash_title)));
    }

    public void testSplashScreenLogoBottomAligned() {
        onView(withText("Logo Bottom")).perform(click());
        onView(withId(R.id.splash_logo)).check(isBelow(withId(R.id.splash_title)));
    }
}
