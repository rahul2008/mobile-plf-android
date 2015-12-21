import android.app.Activity;
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
import static com.philips.cdp.ui.catalog.Matchers.IsDimensionAsExpectedMatcher.isDimensionSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ActionButtonTest extends ActivityInstrumentationTestCase2<ActionButtonsActivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private ActionButtonsActivity actionButtonsActivity;
    private ThemeUtils themeUtils;


    public ActionButtonTest() {

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

    public void testAButtonSquareCrossIsDimensionAsExpected() {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_cross_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnSquareCrossMark))
                .check(matches(isDimensionSimilar((int) width, (int) height)));
//        onView(withId(R.id.miscBtnSquareCrossMark))
//                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonSquareMinusIsDimensionAsExpected() {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_minus_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnSquareMinus))
                .check(matches(isDimensionSimilar((int) width, (int) height)));
//        onView(withId(R.id.miscBtnSquareMinus))
//                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonSquarePlusIsDimensionAsExpected() {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_plus_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnSquarePlus))
                .check(matches(isDimensionSimilar((int) width, (int) height)));
//        onView(withId(R.id.miscBtnSquarePlus))
//                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonSquareRightIsDimensionAsExpected() {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.sqaure_right_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnSquareArrow))
                .check(matches(isDimensionSimilar((int) width, (int) height)));
//        onView(withId(R.id.miscBtnSquareArrow))
//                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonCirclePlusIsDimensionAsExpected() {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_plus_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnCirclePlus))
                .check(matches(isDimensionSimilar((int) width, (int) height)));
//        onView(withId(R.id.miscBtnCirclePlus))
//                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonCircleMinusIsDimensionAsExpected() {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_minus_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnCircleMinus))
                .check(matches(isDimensionSimilar((int) width, (int) height)));
//        onView(withId(R.id.miscBtnCircleMinus))
//                .check(matches(isImageSimilar(expectedBitmap)));
    }

    public void testAButtonCircleRightIsDimensionAsExpected() {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.circle_right_mdpi);
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isDimensionSimilar((int) width, (int) height)));
//        onView(withId(R.id.miscBtnCircleArrow))
//                .check(matches(isImageSimilar(expectedBitmap)));
    }

    // Not verifying for pixel perfectness as the design for question mark is a text instead of image
    public void testActionButtonCircleQuestionDimensionAsExpected() {
        float width = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_width);
        float height = testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.action_button_height);
        onView(withId(R.id.miscBtnCircleQuestionMark))
                .check(matches(isDimensionSimilar((int) width, (int) height)));
    }

    public void testDBThemeActionButtonColourAsExpected() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isBackgroundColorSimilar("#03478", 15, 15)));
    }

    public void testBOThemeActionButtonColourAsExpected() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isBackgroundColorSimilar("#e9830", 15, 15)));
    }

    public void testBAThemeActionButtonColourAsExpected() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.miscBtnCircleArrow))
                .check(matches(isBackgroundColorSimilar("#1e9d8b", 15, 15)));
    }
}
