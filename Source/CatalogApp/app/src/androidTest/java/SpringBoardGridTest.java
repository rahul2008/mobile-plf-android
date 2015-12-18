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
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;

/*
*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.

*/

public class SpringBoardGridTest extends ActivityInstrumentationTestCase2<SpringBoardsctivity> {

    private Resources testResources;
    private SpringBoardsctivity springBoardsctivity;
    private ThemeUtils themeUtils;

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;

    public SpringBoardGridTest() {
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

    public void testCatalogAppDemonstratesSpringboardGrid(){
        onView(withText("6 Grid")).check(matches(isDisplayed()));
    }

    public void testSpringboardGridSupportsHeading(){
        onView(withText("6 Grid")).perform(click());
        onView(withText("Mobile app")).check(matches(isDisplayed()));
    }

    public void testSBGridTitleFontSizeAsExpected(){
        onView(withText("6 Grid")).perform(click());
        onView(withText("Mobile app")).check(matches(isTextSizeSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.springboard_grid_title_font_size))));
    }

    public void testSBGridTitleFontColorAsExpected(){
        onView(withText("6 Grid")).perform(click());
        onView(withText("Mobile app")).check(matches(isTextColorSimilar("#ffffff")));
    }



    public void testTextColorDBTheme(){
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withText("6 Grid")).perform(click());
        onView(withText("Monitor")).check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testTextFontSize(){
        onView(withText("6 Grid")).perform(click());
        onView(withText("Monitor")).check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.springboard_grid_font_size))));
    }

      public void testImageBGColorDBTheme(){
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withText("6 Grid")).perform(click());
        onView(withId(R.id.imageView1)).check(matches(isBackgroundColorSimilar("#ffffff",(int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_x), (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_y))));
    }

    public void testGridItemBGColorDBTheme(){
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withText("6 Grid")).perform(click());
        onView(withId(R.id.item1)).check(matches(isBackgroundColorSimilar("#03478",(int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_x), (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_y))));
    }

    public void testGridItemBGColorBOTheme(){
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withText("6 Grid")).perform(click());
        onView(withId(R.id.item1)).check(matches(isBackgroundColorSimilar("#e9830",(int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_x), (int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.sbgrid_testImageBGColorDBTheme_y))));
    }

    public void testGridItemBGColorBATheme(){
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withText("6 Grid")).perform(click());
        onView(withId(R.id.item1)).check(matches(isBackgroundColorSimilar("#1e9d8b", 5, 10)));
    }

}

