import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.ActionButtonsActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;



/*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 **/

public class SwitchesTest extends ActivityInstrumentationTestCase2<ActionButtonsActivity> {

    private Resources testResources;
    private ActionButtonsActivity actionbuttonsActivity;
    private ThemeUtils themeUtils;

    public SwitchesTest() {

        super(ActionButtonsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        actionbuttonsActivity = getActivity();
        SharedPreferences preferences = actionbuttonsActivity.getSharedPreferences(actionbuttonsActivity.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        actionbuttonsActivity.setResult(1);
        intent = new Intent(actionbuttonsActivity, ActionButtonsActivity.class);
        actionbuttonsActivity.startActivity(intent);
        actionbuttonsActivity.finish();
    }

    public void testSwitchIsDisplayed() {
        onView(withId(R.id.uikit_switch)).check(matches(isDisplayed()));
    }

    public void testDBThemeBGColorSwitchOff() {
        // Apply Dark blue theme
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();

        //Verify the background/outline color
        onView(withId(R.id.uikit_switch))
                .check(matches(isOutlineColorSimilar("#03478")));
    }

    public void testBOThemeBGColorSwitchOff() {
        // Apply Bright Orange theme
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();

        //Verify the background/outline color
        onView(withId(R.id.uikit_switch))
                .check(matches(isOutlineColorSimilar("#e9830")));
    }

    public void testBAThemeBGColorSwitchOff() {
        // Apply Bright Aqua theme
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();

        //Verify the background/outline color
        onView(withId(R.id.uikit_switch))
                .check(matches(isOutlineColorSimilar("#1e9d8b")));
    }

    //this test is ignored because height and width doesnot match due to technical limitation.

/*    public void testSwitchOffPixelPerfectMdpi() {
        // Apply Dark blue theme
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.switch_off_mdpi) ;
        onView(withId(R.id.uikit_switch))
                .check(matches(isImageSimilar(expectedBitmap)));
    }*/

    public void testDBThemeBGColorSwitchOn() {
        // Apply Dark blue theme
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();

        //Verify the background/outline color
        onView(withId(R.id.uikit_switch)).perform(click());
        onView(withId(R.id.uikit_switch)) // change id
                .check(matches(isOutlineColorSimilar("#5b8f22")));
    }

    public void testBOThemeBGColorSwitchOn() {
        // Apply Bright Orange theme
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();


        //Verify the background/outline color
        onView(withId(R.id.uikit_switch)).perform(click());
        onView(withId(R.id.uikit_switch)) // change id
                .check(matches(isOutlineColorSimilar("#5b8f22")));
    }

    public void testBAThemeBGColorSwitchOn() {
        // Apply Bright Orange theme
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();


        //Verify the background/outline color
        onView(withId(R.id.uikit_switch)).perform(click());
        onView(withId(R.id.uikit_switch)) // change id
                .check(matches(isOutlineColorSimilar("#5b8f22")));
    }

    //this test is ignored because height and width doesnot match due to technical limitation.

/*    public void testSwitchOnPixelPerfectMdpi() {
        onView(withText("Controls")).perform(click());

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.switch_on_mdpi) ;
        onView(withId(R.id.uikit_switch)).perform(click());
        onView(withId(R.id.uikit_switch))
        .check(matches(isImageSimilar(expectedBitmap)));
    }*/

}

