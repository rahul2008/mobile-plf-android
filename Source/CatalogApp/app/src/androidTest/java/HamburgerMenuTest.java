import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.HamburgerActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsWidthAsExpectedMatcher.isWidthSimilar;
import static org.hamcrest.Matchers.allOf;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HamburgerMenuTest extends ActivityInstrumentationTestCase2<HamburgerActivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private HamburgerActivity hamburgerActivity;
    private ThemeUtils themeUtils;

    public HamburgerMenuTest() {
        super(HamburgerActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        hamburgerActivity = getActivity();
        SharedPreferences preferences = hamburgerActivity.getSharedPreferences(hamburgerActivity.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        hamburgerActivity.setResult(1);
        intent = new Intent(hamburgerActivity, HamburgerActivity.class);
        hamburgerActivity.startActivity(intent);
        hamburgerActivity.finish();
    }

    public void testHMenuNotificationlabel() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
    }

    public void testHamburgerMenuItems() {
        onView(withText("Hamburger Menu")).check(matches(isDisplayed()));
        onView(withText("Hamburger Menu with Icons")).check(matches(isDisplayed()));
    }


    public void testHMHeightOfListItem() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(withChild(withText("Eget Odio")))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.hmenu_listitem_height))));
    }

    public void testHMWidthOfListItem() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(withChild(withText("Eget Odio")))
                .check(matches(isWidthSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.hmenu_listitem_width))));
    }

/*    public void testHamburgerMenuIconAsExpected() {
        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.hamburger_menu_icon);
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).check(matches(isImageSimilar(expectedBitmap)));
    }*/

    public void testHMFontSizeOfListItem() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.hamburger_item_text), withText("Eget Odio")))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.hmenu_fontsize_list_item))));
    }

    public void testHMDividerColor() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
//        onView(allOf(withId(R.id.hamburger_parent), withText("Eget Odio")))
//                .check(matches(isBackgroundColorSimilar("#0066a1")));
//        onView(allOf(withId(R.id.divider_bottom)), (withText("Eget Odio"))
//                .check(matches(isBackgroundColorSimilar("#0066a1")));
        onView(allOf(
                withId(R.id.divider_bottom),
                hasSibling(withText("Eget Odio"))
        )).check(matches(isBackgroundColorSimilar("#066a1", 0, 0)));
    }

    public void testHMDividerColorBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.divider_bottom), hasSibling(withText("Eget Odio"))))
                .check(matches(isBackgroundColorSimilar("#eeaf0", 0, 0)));
    }

    public void testHMDividerColorBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.divider_bottom), hasSibling(withText("Eget Odio"))))
                .check(matches(isBackgroundColorSimilar("#5bbbb7", 0, 0)));
    }

    public void testHMFontColorOfListItem() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.hamburger_item_text), withText("Eget Odio")))
                .check(matches(isTextColorSimilar("#b3c8e6")));
    }

    public void testHMWidthOfTitle() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(withChild(withText("Title long")))
                .check(matches(isWidthSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.hmenu_listitem_width))));
    }

    public void testHMHeightOfTitle() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(withChild(withText("Title long")))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.hmenu_title_listitem_height))));
    }

    public void testHMFontSizeOfTitleText() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.hamburger_item_text), withText("Title long")))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.hmenu_fontsize_list_item))));
    }

    public void testHMFontColorOfTitleText() {
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.hamburger_item_text), withText("Title long")))
                .check(matches(isTextColorSimilar("#ffffff")));
    }

//    public void testHMBGColorOfTitleTextDBTheme() {
//        themeUtils.setThemePreferences("blue|false|solid|0");
//        relaunchActivity();
//        onView(withText("Hamburger Menu")).perform(click());
//        onView(withId(R.id.hamburger_icon)).perform(click());
//        onView(allOf(withId(R.id.hamburger_item_text), withText("Title long")))
//                .check(matches(isBackgroundColorSimilar("#03478", 5, 5)));
//    }

//    public void testBGColorOfTitleTextBOTheme() {
//        themeUtils.setThemePreferences("orange|false|solid|0");
//        relaunchActivity();
//        onView(withText("Hamburger Menu")).perform(click());
//        onView(withId(R.id.hamburger_icon)).perform(click());
//        onView(allOf(withId(R.id.hamburger_item_text), withText("Title long")))
//                .check(matches(isOpacityValueSimilar(127, 5, 5)));
//    }
//
//    public void testHMBGColorOfTitleTextBATheme() {
//        themeUtils.setThemePreferences("aqua|false|solid|0");
//        relaunchActivity();
//        onView(withText("Hamburger Menu")).perform(click());
//        onView(withId(R.id.hamburger_icon)).perform(click());
//        onView(allOf(withId(R.id.hamburger_item_text), withText("Title long")))
//                .check(matches(isOpacityValueSimilar(127, 5, 5)));
//    }

    public void testHMTextColorOfFocusedListItemDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withText("Hamburger Menu")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.hamburger_item_text), withText("Eget Odio"))).
                perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.hamburger_item_text), withText("Eget Odio"))).
                check(matches(isTextColorSimilar("#ffffff")));
    }

    public void testHMBGColorOfIconsDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withText("Hamburger Menu with Icons")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.hamburger_list_icon), hasSibling(withText("Eget Odio"))))
                .check(matches(isBackgroundColorSimilar("#b3c8e6", 5, 5)));
    }

    public void testHMBGColorOfIconsBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withText("Hamburger Menu with Icons")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.hamburger_list_icon), hasSibling(withText("Eget Odio"))))
                .check(matches(isBackgroundColorSimilar("#fbd476", 5, 5)));
    }

    public void testHMBGColorOfIconsBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withText("Hamburger Menu with Icons")).perform(click());
        onView(withId(R.id.hamburger_icon)).perform(click());
        onView(allOf(withId(R.id.hamburger_list_icon), hasSibling(withText("Eget Odio"))))
                .check(matches(isBackgroundColorSimilar("#cae3e9", 5, 5)));
    }


}
