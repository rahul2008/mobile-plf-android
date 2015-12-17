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
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
/*

*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.

*/

public class OutlineButtonTest extends ActivityInstrumentationTestCase2<ButtonsActivity> {

    private Resources testResources;
    private ButtonsActivity buttonsActivity;
    private ThemeUtils themeUtils;

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;

    public OutlineButtonTest() {

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


    public void testOButtonIsHeightAsExpected() {
        onView(withId(R.id.outlined_button))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size))));
    }

    public void testDBThemeOButtonOutlineColourAsExpected() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_button))
                .check(matches(isOutlineColorSimilar("#03478")));
    }

    public void testDBThemeOButtonTextColor() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_button))
                .check(matches(isTextColorSimilar("#03478")));
    }

    public void testDBThemeOButtonBGColor() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_button))
                .check(matches(isBackgroundColorSimilar("#ffffff", 15, 15)));
    }

    public void testBOThemeOButtonOutlineColorAsExpected() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_button))
                .check(matches(isOutlineColorSimilar("#e9830")));
    }

    public void testBOThemeOButtonTextColor() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_button))
                .check(matches(isTextColorSimilar("#e9830")));
    }

    public void testBOThemeOButtonBGColor() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_button))
                .check(matches(isBackgroundColorSimilar("#ffffff", 15, 15)));
    }

    public void testBAThemeOButtonOutlineColorAsExpected() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_button))
                .check(matches(isOutlineColorSimilar("#1e9d8b")));
    }

    public void testBAThemeOButtonTextColor() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_button))
                .check(matches(isTextColorSimilar("#1e9d8b")));
    }

    public void testBAThemeOButtonBGColor() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.outlined_button))
                .check(matches(isBackgroundColorSimilar("#ffffff", 15, 15)));
    }
}
