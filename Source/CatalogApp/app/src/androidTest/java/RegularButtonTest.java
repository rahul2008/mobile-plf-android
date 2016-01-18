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
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegularButtonTest extends ActivityInstrumentationTestCase2<ButtonsActivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private ButtonsActivity buttonsActivity;
    private ThemeUtils themeUtils;


    public RegularButtonTest() {

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

    public void testRButtonIsHeightAsExpected() {
        onView(withId(R.id.theme_button))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.button_size))));
    }

    public void testDBThemeRButtonColourAsExpected() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.theme_button))
                .check(matches(isBackgroundColorSimilar("#03478", (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate),(int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate))));
    }

    public void testBOThemeRButtonColourAsExpected() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.theme_button))
                .check(matches(isBackgroundColorSimilar("#e9830", (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate),(int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate))));
    }

    public void testBAThemeRButtonColourAsExpected() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.theme_button))
                .check(matches(isBackgroundColorSimilar("#1e9d8b", (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate),(int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_cordinate))));
    }

    public void testDBThemeRButtonTextColor() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.theme_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testBOThemeRButtonTextColor() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.theme_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testBAThemeRButtonTextColor() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.theme_button))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

//    public void testActionSampleButtonTextSize() {
//        onView(withId(R.id.theme_button))
//                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.theme_button_text_size))));
//    }
}
