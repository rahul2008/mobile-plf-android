import android.content.res.Resources;
import android.support.test.espresso.NoMatchingViewException;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.ActionBarLauncher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsDimensionAsExpectedMatcher.isDimensionSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ActionBarUpTest extends ActivityInstrumentationTestCase2<ActionBarLauncher> {

    private Resources testResources;

    public ActionBarUpTest() {
        super(ActionBarLauncher.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testUpIconDimensionAsExpected() {
        onView(withId(R.id.arrow)).check(matches(isDimensionSimilar((int) (testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.actionbar_up_height)), ((int) (testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.actionbar_up_width))))));
    }

    public void testUpIconBGColorAsExpected() {
        onView(withId(R.id.arrow)).check(matches(isBackgroundColorSimilar("#ffffff", 5,20)));
    }

    public void testUpIconFunctionality() {
        try {
            onView(withId(R.id.arrow)).perform(click());
        } catch (NoMatchingViewException e) {
            return;
        }

    }
}
