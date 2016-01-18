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
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;

/*
*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.

*/

public class SpringBoardListsTest extends ActivityInstrumentationTestCase2<SpringBoardsctivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private SpringBoardsctivity springBoardsctivity;
    private ThemeUtils themeUtils;

    public SpringBoardListsTest() {
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

    public void testCatalogAppDemonstratesSpringboardList() {
        onView(withText("Lists")).check(matches(isDisplayed()));
    }

    public void testSpringboardListSupportsHeading() {
        onView(withText("Lists")).perform(click());
        onView(withText("Mobile app")).check(matches(isDisplayed()));
    }

    public void testSBListTitleFontSizeAsExpected() {
        onView(withText("Lists")).perform(click());
        onView(withText("Mobile app")).check(matches(isTextSizeSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.springboard_list_title_font_size))));
    }

    public void testSBListTitleFontColorAsExpected() {
        onView(withText("Lists")).perform(click());
        onView(withText("Mobile app")).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testSBListHeightofListItemAsExpected() {
        onView(withText("Lists")).perform(click());
        onView(withChild(withText("Shop ")))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.springboard_lists_item_size))));
    }


    public void testTextColorDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withText("Lists")).perform(click());
        onView(withText("Shop ")).check(matches(isTextColorSimilar("#ffffff")));
    }

//    public void testTextFontSize() {
//        onView(withText("Lists")).perform(click());
//        onView(withText("Shop ")).check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.springboard__lists_font_size))));
//    }

    public void testImageBGColorDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withText("Lists")).perform(click());
        onView(withId(R.id.imageView1)).check(matches(isBackgroundColorSimilar("#ffffff", (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_x), (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_y))));
    }

//    public void testListItemBGColorDBTheme(){
//        themeUtils.setThemePreferences("blue|false|solid|0");
//        relaunchActivity();
//        onView(withText("Lists")).perform(click());
//        onView(withId(R.id.row_layout1)).check(matches(isBackgroundColorSimilar("#03478", (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_x), (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_y))));
//    }
//
//    public void testListItemBGColorBOTheme(){
//        themeUtils.setThemePreferences("orange|false|solid|0");
//        relaunchActivity();
//        onView(withText("Lists")).perform(click());
//        onView(withId(R.id.row_layout1)).check(matches(isBackgroundColorSimilar("#e98300", (int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_x), (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_y))));
//    }
//
//    public void testListItemBGColorBATheme(){
//        themeUtils.setThemePreferences("aqua|false|solid|0");
//        relaunchActivity();
//        onView(withText("Lists")).perform(click());
//        onView(withId(R.id.row_layout1)).check(matches(isBackgroundColorSimilar("#1e9d8b",(int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_x), (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_y))));
//    }


}

