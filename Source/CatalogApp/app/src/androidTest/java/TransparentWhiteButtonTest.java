import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOpacityValueAsExpectedMatcher.isOpacityValueSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TransparentWhiteButtonTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public TransparentWhiteButtonTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testTWButtonIsHeightAsExpected() {
        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size))));
    }

    public void testDBThemeTWButtonOutlineColourAsExpected() {
        // Apply Bright blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isOutlineColorSimilar("#ffffff")));
    }

    public void testDBThemeTWButtonTextColor() {
        // Apply Bright blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testDBThemeTWButtonBGColor() {
        // Apply Bright blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isOpacityValueSimilar(0, 30, 30)));
    }

    public void testBOThemeTWButtonOutlineColorAsExpected() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isOutlineColorSimilar("#ffffff")));
    }

    public void testBOThemeTWButtonTextColor() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testBOThemeTWButtonBGColor() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isOpacityValueSimilar(0, 30, 30)));
    }

    public void testBAThemeTWButtonOutlineColorAsExpected() {
        // Apply Bright Aqua theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isOutlineColorSimilar("#ffffff")));
    }

    public void testBAThemeTWButtonTextColor() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testBAThemeTWButtonBGColor() {
        // Apply Bright Aqua theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        onView(withText("Buttons")).perform(click());
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isOpacityValueSimilar(0, 30, 30)));
    }
}
