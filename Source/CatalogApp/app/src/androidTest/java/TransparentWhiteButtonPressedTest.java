import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.Matchers.IsOpacityValueAsExpectedMatcher;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.ButtonsActivity;
import com.philips.cdp.ui.catalog.activity.MainActivity;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TransparentWhiteButtonPressedTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;
    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;

    public TransparentWhiteButtonPressedTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testDBThemePressedTWButtonOutlineColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        onView(withText("Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_white_button, true);
        matchPressedOutlineColor(R.id.outlined_transparent_white_button, "#ffffff");
    }

    public void testDBThemePressedTWButtonTextColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        onView(withText("Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_white_button, true);
        matchPressedTextColor(R.id.outlined_transparent_white_button, "#ffffff");
    }

    public void testDBThemePressedTWButtonOpacityValueAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        onView(withText("Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_white_button, true);
        matchPressedOpacityValue(R.id.outlined_transparent_white_button, 26);
    }

    public void testBOThemePressedTWButtonOutlineColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        onView(withText("Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_white_button, true);
        matchPressedOutlineColor(R.id.outlined_transparent_white_button, "#ffffff");
    }

    public void testBOThemePressedTWButtonTextColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        onView(withText("Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_white_button, true);
        matchPressedTextColor(R.id.outlined_transparent_white_button, "#ffffff");
    }

    public void testBOThemePressedTWButtonOpacityValueAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        onView(withText("Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_white_button, true);
        matchPressedOpacityValue(R.id.outlined_transparent_white_button, 26);
    }

    public void testBAThemePressedTWButtonOutlineColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        onView(withText("Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_white_button, true);
        matchPressedOutlineColor(R.id.outlined_transparent_white_button, "#ffffff");
    }

    public void testBAThemePressedTWButtonTextColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        onView(withText("Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_white_button, true);
        matchPressedTextColor(R.id.outlined_transparent_white_button, "#ffffff");
    }

    public void testBAThemePressedTWButtonOpacityValueAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Aqua Theme")).perform(click());
        onView(withId(R.id.colorSwitch)).perform(click());
        pressBack();

        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ButtonsActivity.class);
        onView(withText("Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.outlined_transparent_white_button, true);
        matchPressedOpacityValue(R.id.outlined_transparent_white_button, 26);
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
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(IsOpacityValueAsExpectedMatcher.isOpacityValueSimilar(opacityValue, 30, 30)));
        release();
    }

    private void matchPressedTextColor(int buttonID, String expectedColor) {
        acquire();
        onView(withId(R.id.outlined_transparent_white_button))
                .check(matches(isTextColorSimilar(expectedColor)));
        release();
    }

    private void matchPressedOutlineColor(int buttonID, String expectedColor) {
        acquire();
        onView(withId(R.id.outlined_transparent_white_button))
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

