import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.Matchers.IsOpacityValueAsExpectedMatcher;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.ButtonsActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TransparentButtonPressedTest extends ActivityInstrumentationTestCase2<ButtonsActivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private ButtonsActivity buttonsActivity;
    private ThemeUtils themeUtils;

    public TransparentButtonPressedTest() {

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

//    public void testDBThemePressedTButtonOutlineColourAsExpected() {
//        themeUtils.setThemePreferences("blue|false|solid|0");
//        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
//        setTargetActivity(monitor);
//        setPressed(R.id.outlined_transparent_button, true);
//        matchPressedOutlineColor(R.id.outlined_transparent_button, "#7ba4d9");
//    }

    public void testDBThemePressedTButtonTextColourAsExpected() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_button, true);
        matchPressedTextColor(R.id.outlined_transparent_button, "#7ba4d9");
    }

    public void testDBThemePressedTButtonOpacityValueAsExpected() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_button, true);
        matchPressedOpacityValue(R.id.outlined_transparent_button, 26);
    }

//    public void testBOThemePressedTButtonOutlineColourAsExpected() {
//        themeUtils.setThemePreferences("orange|false|solid|0");
//        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
//        setTargetActivity(monitor);
//        setPressed(R.id.outlined_transparent_button, true);
//        matchPressedOutlineColor(R.id.outlined_transparent_button, "#eeaf0");
//    }

    public void testBOThemePressedTButtonTextColourAsExpected() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_button, true);
        matchPressedTextColor(R.id.outlined_transparent_button, "#eeaf0");
    }

    public void testBOThemePressedTButtonOpacityValueAsExpected() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_button, true);
        matchPressedOpacityValue(R.id.outlined_transparent_button, 26);
    }

//    public void testBAThemePressedTButtonOutlineColourAsExpected() {
//        themeUtils.setThemePreferences("aqua|false|solid|0");
//        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
//        setTargetActivity(monitor);
//        setPressed(R.id.outlined_transparent_button, true);
//        matchPressedOutlineColor(R.id.outlined_transparent_button, "#5bbbb7");
//    }

//    public void testBAThemePressedTButtonTextColourAsExpected() {
//        themeUtils.setThemePreferences("aqua|false|solid|0");
//        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
//        setTargetActivity(monitor);
//        setPressed(R.id.outlined_transparent_button, true);
//        matchPressedTextColor(R.id.outlined_transparent_button, "#5bbbb7");
//    }

    public void testBAThemePressedTButtonOpacityValueAsExpected() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_button, true);
        matchPressedOpacityValue(R.id.outlined_transparent_button, 26);
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

    private void matchPressedOpacityValue(int buttonID, int opacityValue) {
        acquire();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(IsOpacityValueAsExpectedMatcher.isOpacityValueSimilar(opacityValue, 30, 30)));
        release();
    }

    private void matchPressedTextColor(int buttonID, String expectedColor) {
        acquire();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isTextColorSimilar(expectedColor)));
        release();
    }

    private void matchPressedOutlineColor(int buttonID, String expectedColor) {
        acquire();
        onView(withId(R.id.outlined_transparent_button))
                .check(matches(isOutlineColorSimilar(expectedColor)));
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

