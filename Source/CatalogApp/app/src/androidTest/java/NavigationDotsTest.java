import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.DotNavigation;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsDotOpacityAsExpectedMatcher.isOpacitySimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsSelectedDotBackgroundColorAsExpectedMatcher.isSelectedDotColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsSelectedDotDimensionAsExpectedMatcher.isSelectedDotDimenSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsUnSelectedDotDimensionAsExpectedMatcher.isUnSelectedDotDimenSimilar;
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class NavigationDotsTest extends ActivityInstrumentationTestCase2<DotNavigation> {

    private Resources testResources;
    private DotNavigation dotNavigation;
    private ThemeUtils themeUtils;

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;

    public NavigationDotsTest() {

        super(DotNavigation.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dotNavigation = getActivity();
        SharedPreferences preferences = dotNavigation.getSharedPreferences(dotNavigation.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        dotNavigation.setResult(1);
        intent = new Intent(dotNavigation, DotNavigation.class);
        dotNavigation.startActivity(intent);
        dotNavigation.finish();
    }

    public void testDBThemeSelectedNDotThemable() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.indicator)).check(matches(isSelectedDotColorSimilar("#03478")));
    }

    public void testBOThemeSelectedNDotThemable() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.indicator)).check(matches(isSelectedDotColorSimilar("#e9830")));
    }

    public void testBAThemeSelectedNDotThemable() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.indicator)).check(matches(isSelectedDotColorSimilar("#1e9d8b")));
    }

/*    public void testDBThemeUnSelectedNDotThemable() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.indicator)).check(matches(isUnSelectedDotColorSimilar("#03478")));
    }*/
/*    public void testBOThemeUnSelectedNDotThemable() {
        onView(withText("Dot Navigation")).perform(click());
    }*/

/*    public void testBAThemeUnSelectedNDotThemable() {
        onView(withText("Dot Navigation")).perform(click());
    }*/

    public void testSelectedNDotDimensionAsExpected() {
        onView(withId(R.id.indicator)).check(matches(isSelectedDotDimenSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.ndot_selected_height), (testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.ndot_selected_width)))));
    }

    public void testUnSelectedNDotDimensionAsExpected() {
        onView(withId(R.id.indicator)).check(matches(isUnSelectedDotDimenSimilar((int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.ndot_unselected_height), (int)(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.ndot_unselected_width)))));
    }

    public void testSwipingOfNDot() {
        onView(withId(R.id.indicator)).perform(swipeLeft());
    }

    public void testUnselectedNDotOpacity() {
        onView(withId(R.id.indicator)).check(matches(isOpacitySimilar(77)));
    }
}
