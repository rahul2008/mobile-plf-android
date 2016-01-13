import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.ActionButtonsActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsDimensionAsExpectedMatcher.isDimensionSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NotificationLabelsTest extends ActivityInstrumentationTestCase2<ActionButtonsActivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private ActionButtonsActivity actionButtonsActivity;
    private ThemeUtils themeUtils;

    public NotificationLabelsTest() {
        super(ActionButtonsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        actionButtonsActivity = getActivity();
        SharedPreferences preferences = actionButtonsActivity.getSharedPreferences(actionButtonsActivity.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        actionButtonsActivity.setResult(1);
        intent = new Intent(actionButtonsActivity, ActionButtonsActivity.class);
        actionButtonsActivity.startActivity(intent);
        actionButtonsActivity.finish();
    }

    public void testNlabelsLarge2DigitDimensionAsExpected() {
        onView(withId(R.id.badge_view_square_medium)).check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.nlabel_large_2digit_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.nlabel_large_2digit_width))));
    }

    public void testNlabelsLarge1DigitDimensionAsExpected() {
        onView(withId(R.id.badge_view_circle_medium)).check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.nlabel_large_2digit_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.nlabel_large_2digit_height))));
    }

    public void testNlabelsSmall2DigitDimensionAsExpected() {
        onView(withId(R.id.badge_view_square_small)).check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.nlabel_small_2digit_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.nlabel_small_2digit_width))));
    }

    public void testNlabelsSmall1DigitDimensionAsExpected() {
        onView(withId(R.id.badge_view_circle_small)).check(matches(isDimensionSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.nlabel_small_2digit_height), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.nlabel_small_2digit_height))));
    }

    public void testNlabelLarge1DigitBGColor() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.badge_view_circle_medium)).check(matches(isBackgroundColorSimilar("#cd202c", (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate),(int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate))));
    }

    public void testNlabelLarge2DigitBGColor() {
        onView(withId(R.id.badge_view_square_medium)).check(matches(isBackgroundColorSimilar("#cd202c", (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate),(int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate))));
    }

    public void testNlabelSmall1DigitBGColor() {
        onView(withId(R.id.badge_view_circle_small)).check(matches(isBackgroundColorSimilar("#cd202c", (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate),(int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate))));
    }

    public void testNlabelSmall2DigitBGColor() {
        onView(withId(R.id.badge_view_square_small)).check(matches(isBackgroundColorSimilar("#cd202c",(int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate),(int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate))));
    }

    public void testNlabelSmall1DigitTextColor() {
        onView(withId(R.id.badge_view_circle_small)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testNlabelSmall2DigitTextColor() {
        onView(withId(R.id.badge_view_square_small)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testNlabelLarge1DigitTextColor() {
        onView(withId(R.id.badge_view_circle_medium)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testNlabelLarge2DigitTextColor() {
        onView(withId(R.id.badge_view_square_medium)).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testNlabelSmall1DigitTextSize() {
        onView(withId(R.id.badge_view_circle_small)).check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.badgeview_small_fontsize))));
    }

    public void testNlabelLarge1DigitTextSize() {
        onView(withId(R.id.badge_view_circle_medium)).check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.badgeview_large_fontlarge))));
    }


}
