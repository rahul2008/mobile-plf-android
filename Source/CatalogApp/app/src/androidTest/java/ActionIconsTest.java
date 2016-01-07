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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ActionIconsTest extends ActivityInstrumentationTestCase2<ActionButtonsActivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private ActionButtonsActivity actionButtonsActivity;
    private ThemeUtils themeUtils;

    public ActionIconsTest() {
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

    public void testActionIcons() {
        onView(withId(R.id.actionIcon1)).check(matches(isDisplayed()));
        onView(withId(R.id.actionIcon2)).check(matches(isDisplayed()));
    }

/*    public void testBGColorOfActionIconDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.actionIcon1)).check(matches(isBackgroundColorSimilar("#03478", 20, 15)));
    }

    public void testBGColorOfActionIconBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.actionIcon1)).check(matches(isBackgroundColorSimilar("#e9830", 20, 15)));
    }

    public void testBGColorOfActionIconBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.actionIcon1)).check(matches(isBackgroundColorSimilar("#1e9d8b", 20, 15)));
    }*/
}
