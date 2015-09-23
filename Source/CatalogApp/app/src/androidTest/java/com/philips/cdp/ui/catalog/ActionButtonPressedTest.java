package com.philips.cdp.ui.catalog;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.ActionButtonsActivity;
import com.philips.cdp.ui.catalog.activity.MainActivity;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.IsPixelAsExpectedMatcher.isImageSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ActionButtonPressedTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;
    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;

    public ActionButtonPressedTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();


    }

/*    Pixel perfectness of the Action buttons are verified in ActionButtonTest,
    Hence only the colour of a pressed state Action button is verified in this test.*/

    public void testDBThemeActionButtonPressedColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ActionButtonsActivity.class);
        onView(withText("Miscellaneous Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.miscBtnCircleArrow, true);
        matchPressedColor(R.id.miscBtnCircleArrow, com.philips.cdp.ui.catalog.test.R.drawable.circle_right_mdpi,"#0f204b" );
    }

    public void testBrightOrangeThemeActionButtonPressedColourAsExpected() {
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ActionButtonsActivity.class);
        onView(withText("Miscellaneous Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.miscBtnCircleArrow, true);
        matchPressedColor(R.id.miscBtnCircleArrow, com.philips.cdp.ui.catalog.test.R.drawable.circle_right_mdpi,"#983222");
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
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isBackgroundColorSimilar(expectedColor)));
        release();
        expectedBitmap.recycle();
    }

    //Target Monitor changes only when our target Activity chagnes.
    private Instrumentation.ActivityMonitor setTargetMonitor(Class<?> targetClass) {
        if (targetActivity == null || !targetActivity.getClass().getSimpleName().equals(targetClass.getSimpleName())) {
            return getInstrumentation().addMonitor(targetClass.getName(),
                    null, false);
        }
        return null;
    }

    //To be called only once or only when our target Activity chagnes.
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
