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
/*
*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */


public class DisabledButtonTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public DisabledButtonTest() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testDButtonBGColourAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.change_button_state)).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isBackgroundColorSimilar("#b9b9b9", (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate),(int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate))));
    }

//    public void testDButtonTextSize() {
//        onView(withText("Buttons")).perform(click());
//        onView(withId(R.id.change_button_state)).perform(click());
//        onView(withId(R.id.theme_button))
//                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.theme_button_text_size))));
//    }

    public void testDButtonTextColor() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.change_button_state)).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testDButtonIsHeightAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.change_button_state)).perform(click());
        onView(withId(R.id.theme_button))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size))));
    }


}
