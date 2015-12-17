import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.ButtonsActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOpacityValueAsExpectedMatcher.isOpacityValueSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TransparentButtonTest extends ActivityInstrumentationTestCase2<ButtonsActivity> {

    private Resources testResources;
    private ButtonsActivity buttonsActivity;
    private ThemeUtils themeUtils;

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;

    public TransparentButtonTest() {

        super(ButtonsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        buttonsActivity = getActivity();
        SharedPreferences preferences = buttonsActivity.getSharedPreferences(buttonsActivity.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        buttonsActivity.setResult(1);
        intent = new Intent(buttonsActivity, ButtonsActivity.class);
        buttonsActivity.startActivity(intent);
        buttonsActivity.finish();
    }

    public void testTButtonIsHeightAsExpected() {
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size))));
    }

    public void testDBThemeTButtonOutlineColourAsExpected() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOutlineColorSimilar("#b3c8e6")));
    }

    public void testDBThemeTButtonTextColor() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isTextColorSimilar("#b3c8e6")));
    }

    public void testDBThemeTButtonBGColor() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOpacityValueSimilar(0, 30, 30)));
    }

    public void testBOThemeTButtonOutlineColorAsExpected() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOutlineColorSimilar("#fbd476")));
    }

    public void testBOThemeTButtonTextColor() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isTextColorSimilar("#fbd476")));
    }

    public void testBOThemeTButtonBGColor() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOpacityValueSimilar(0, 30, 30)));
    }

/*    public void testBAThemeTButtonOutlineColorAsExpected() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOutlineColorSimilar("#cae3e9")));
    }*/

    public void testBAThemeTButtonTextColor() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isTextColorSimilar("#cae3e9")));
    }

    public void testBAThemeTButtonBGColor() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOpacityValueSimilar(0, 30, 30)));
    }
}

