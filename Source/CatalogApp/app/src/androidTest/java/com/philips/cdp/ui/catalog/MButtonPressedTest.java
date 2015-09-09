package com.philips.cdp.ui.catalog;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.IsSimilarMatcher.isImageSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MButtonPressedTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;
    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;

    public MButtonPressedTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testMButtonSquarePlusPressedExpected() {
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ActionButtonsActivity.class);
        onView(withText("Miscellaneous Buttons")).perform(click());
        setTargetActivity(monitor);
        setPressed(R.id.miscBtnSquarePlus, true);
        matchPressedColor(R.id.miscBtnSquarePlus, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_plus_pressed);
    }

    private void setPressed(int buttonID, boolean state) {
        acquire();
        targetActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                targetActivity.findViewById(R.id.miscBtnSquarePlus).setPressed(true);
                release();
            }
        });
    }

    private void matchPressedColor(int buttonID, int expectedBitmapID) {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, expectedBitmapID);
        acquire();
        onView(withId(buttonID))
                .check(matches(isImageSimilar(expectedBitmap)));
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
