import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class DisabledNoFillButtonTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public DisabledNoFillButtonTest() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testDOButtonBGColourAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.change_button_state)).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isBackgroundColorSimilar("#ffffff", 15, 15)));
    }

//    public void testDOButtonTextSize() {
//        onView(withText("Buttons")).perform(click());
//        onView(withId(R.id.change_button_state)).perform(click());
//        onView(withId(R.id.outlined_button))
//                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.theme_button_text_size))));
//    }

    public void testDOButtonTextColor() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.change_button_state)).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isTextColorSimilar("#b9b9b9")));
    }

    public void testDOButtonIsHeightAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.change_button_state)).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size))));
    }

/*    public void testDOButtonOutlineColourAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.change_button_state)).perform(click());
        onView(withId(R.id.outlined_button))
                .check(matches(isOutlineColorSimilar("#b9b9b9")));
    }*/


}

