import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.SpringBoardsctivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsWidthAsExpectedMatcher.isWidthSimilar;

/*
*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.

*/

public class SpringBoardFullBlocksTest extends ActivityInstrumentationTestCase2<SpringBoardsctivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private SpringBoardsctivity springBoardsctivity;
    private ThemeUtils themeUtils;

    public SpringBoardFullBlocksTest() {
        super(SpringBoardsctivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        springBoardsctivity = getActivity();
        SharedPreferences preferences = springBoardsctivity.getSharedPreferences(springBoardsctivity.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        springBoardsctivity.setResult(1);
        intent = new Intent(springBoardsctivity, SpringBoardsctivity.class);
        springBoardsctivity.startActivity(intent);
        springBoardsctivity.finish();
    }

    public void testCatalogAppDemonstratesSpringboardFullBlock() {
        onView(withText("Full Blocks")).check(matches(isDisplayed()));
    }

//    public void testDividerColorDBTheme(){
//        themeUtils.setThemePreferences("blue|false|solid|0");
//        relaunchActivity();
//        onView(withText("Full Blocks")).perform(click());
//        onView(withId(R.id.HorizonView1)).check(matches(isBackgroundColorSimilar("#066a1", (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_dividercolor_x), (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_dividercolor_y))));
////        onView(withId(R.id.HorizonView1)).check(matches(isOpacityValueSimilar(77, 1, 2)));
//    }
//
//    public void testDividerColorBOTheme(){
//        themeUtils.setThemePreferences("orange|false|solid|0");
//        relaunchActivity();
//        onView(withText("Full Blocks")).perform(click());
//        onView(withId(R.id.HorizonView1)).check(matches(isBackgroundColorSimilar("#eeaf00", (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_dividercolor_x), (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_dividercolor_y))));
////        onView(withId(R.id.HorizonView1)).check(matches(isOpacityValueSimilar(77, 1, 2)));
//    }
//
//    public void testDividerColorBATheme(){
//        themeUtils.setThemePreferences("aqua|false|solid|0");
//        relaunchActivity();
//        onView(withText("Full Blocks")).perform(click());
//        onView(withId(R.id.HorizonView1)).check(matches(isBackgroundColorSimilar("#5bbbb7", (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_dividercolor_x), (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_dividercolor_y))));
////        onView(withId(R.id.HorizonView1)).check(matches(isOpacityValueSimilar(77, 1, 2)));
//    }

    public void testDividerHeight() {
        onView(withText("Full Blocks")).perform(click());
        onView(withId(R.id.HorizonView1)).check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.springboard_divider_height))));
    }

    public void testDividerWidth() {
        onView(withText("Full Blocks")).perform(click());
        onView(withId(R.id.FirstView)).check(matches(isWidthSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.springboard_divider_height))));
    }

    public void testTextColorDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withText("Full Blocks")).perform(click());
        onView(withText("Telephone ")).check(matches(isTextColorSimilar("#ffffff")));
    }

//    public void testTextColorBOTheme(){
//        themeUtils.setThemePreferences("orange|false|solid|0");
//        relaunchActivity();
//        onView(withText("Full Blocks")).perform(click());
//        onView(withText("Telephone ")).check(matches(isTextColorSimilar("#ffffff")));
//    }

//    public void testTextColorBATheme() {
//        themeUtils.setThemePreferences("aqua|false|solid|0");
//        relaunchActivity();
//        onView(withText("Full Blocks")).perform(click());
//        onView(withText("Telephone ")).check(matches(isTextColorSimilar("#ffffff")));
//    }

    public void testTextFontSize() {
        onView(withText("Full Blocks")).perform(click());
        onView(withText("Telephone ")).check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.springboard_font_size))));
    }

    public void testBGColorOfSelectedBlockAsExpected() {
        onView(withText("Full Blocks")).perform(click());

    }

//    works only on mdpi device due to coordinates
//    public void testImageBGColorDBTheme() {
//        themeUtils.setThemePreferences("blue|false|solid|0");
//        relaunchActivity();
//        onView(withText("Full Blocks")).perform(click());
//        onView(withId(R.id.imageView6)).check(matches(isBackgroundColorSimilar("#ffffff", 5, 10)));
//    }

    //    works only on mdpi device due to coordinates
//    public void testImageBGColorBOTheme() {
//        themeUtils.setThemePreferences("orange|false|solid|0");
//        relaunchActivity();
//        onView(withText("Full Blocks")).perform(click());
//        onView(withId(R.id.imageView6)).check(matches(isBackgroundColorSimilar("#ffffff", 5, 10)));
//    }

//    public void testImageBGColorBATheme() {
//        themeUtils.setThemePreferences("aqua|false|solid|0");
//        relaunchActivity();
//        onView(withText("Full Blocks")).perform(click());
//        onView(withId(R.id.imageView6)).check(matches(isBackgroundColorSimilar("#ffffff", 5, 10)));
//    }


}

