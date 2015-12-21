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
import com.philips.cdp.ui.catalog.activity.ActionButtonsActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;

//*
// * (C) Koninklijke Philips N.V., 2015.
// * All rights reserved.
//

public class ActionButtonPressedTest extends ActivityInstrumentationTestCase2<ActionButtonsActivity> {

    private Resources testResources;
    private ActionButtonsActivity actionButtonsActivity;
    private ThemeUtils themeUtils;

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;


    public ActionButtonPressedTest() {

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


//    Pixel perfectness of the Action buttons are verified in ActionButtonTest,
//    Hence only the colour of a pressed state Action button is verified in this test.


    public void testDBThemeActionButtonPressedColourAsExpected() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ActionButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.miscBtnCircleArrow, true);
        matchPressedColor(R.id.miscBtnCircleArrow, com.philips.cdp.ui.catalog.test.R.drawable.circle_right_mdpi, "#f204b");
    }

    public void testBOThemeActionButtonPressedColourAsExpected() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ActionButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.miscBtnCircleArrow, true);
        matchPressedColor(R.id.miscBtnCircleArrow, com.philips.cdp.ui.catalog.test.R.drawable.circle_right_mdpi, "#983222");
    }

    public void testBAThemeActionButtonPressedColourAsExpected() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        Instrumentation.ActivityMonitor monitor = setTargetMonitor(ActionButtonsActivity.class);
        setTargetActivity(monitor);
        setPressed(R.id.miscBtnCircleArrow, true);
        matchPressedColor(R.id.miscBtnCircleArrow, com.philips.cdp.ui.catalog.test.R.drawable.circle_right_mdpi, "#156570");
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
                .check(matches(isBackgroundColorSimilar(expectedColor, 15, 15)));
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
