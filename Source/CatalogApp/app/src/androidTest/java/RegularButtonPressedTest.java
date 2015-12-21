import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.ButtonsActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class RegularButtonPressedTest extends ActivityInstrumentationTestCase2<ButtonsActivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private ButtonsActivity buttonsActivity;
    private ThemeUtils themeUtils;


    public RegularButtonPressedTest() {

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

    public void testDBThemeRButtonPressedColourAsExpected() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.theme_button, true);
        matchPressedColor(R.id.theme_button, com.philips.cdp.ui.catalog.test.R.drawable.regularbtn_darkblue_pressed_mdpi, "#f204b");
    }

    public void testBOThemeRButtonPressedColourAsExpected() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.theme_button, true);
        matchPressedColor(R.id.theme_button, com.philips.cdp.ui.catalog.test.R.drawable.regularbtn_brightorange_pressed_mdpi, "#983222");
    }

    public void testBAThemeRButtonPressedColourAsExpected() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.theme_button, true);
        matchPressedColor(R.id.theme_button, com.philips.cdp.ui.catalog.test.R.drawable.regularbtn_brightorange_pressed_mdpi, "#156570");
    }

    public void testBAThemeRButtonPressedTextColourAsExpected() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.theme_button, true);
        matchPressedTextColor(R.id.theme_button, "#ffffff");
    }

    private void setPressed(final int buttonID, boolean state) {
        acquire();
        targetActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                targetActivity.findViewById(buttonID).setPressed(true);
                release();
            }
        });
    }

    private void matchPressedColor(int buttonID, int expectedBitmapID, String expectedColor) {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, expectedBitmapID);
        acquire();
        onView(withId(R.id.theme_button))
                .check(matches(isBackgroundColorSimilar(expectedColor, 15, 15)));
        release();
        expectedBitmap.recycle();
    }

    private void matchPressedTextColor(int buttonID, String expectedColor) {
        acquire();
        onView(withId(R.id.theme_button))
                .check(matches(isTextColorSimilar(expectedColor)));
        release();
    }

    private Instrumentation.ActivityMonitor setTargetMonitor(Class<?> targetClass) {
        if (targetActivity == null || !targetActivity.getClass().getSimpleName().equals(targetClass.getSimpleName())) {
            return getInstrumentation().addMonitor(targetClass.getName(),
                    null, false);
        }
        return null;
    }

    private void setTargetActivity(Instrumentation.ActivityMonitor monitor) {
        if (monitor != null) {
            targetActivity = monitor.waitForActivity();
            getInstrumentation().removeMonitor(monitor);
        }
    }

    private void acquire() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void release() {
        semaphore.release();
    }
}
