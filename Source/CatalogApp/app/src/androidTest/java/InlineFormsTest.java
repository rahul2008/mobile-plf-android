import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.TextLayoutInputFeildInlineForms;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHintTextColorAsExpectedMatcher.isHintTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class InlineFormsTest extends ActivityInstrumentationTestCase2<TextLayoutInputFeildInlineForms> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private TextLayoutInputFeildInlineForms inlineForms;
    private ThemeUtils themeUtils;

    public InlineFormsTest() {
        super(TextLayoutInputFeildInlineForms.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        inlineForms = getActivity();
        SharedPreferences preferences = inlineForms.getSharedPreferences(inlineForms.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        inlineForms.setResult(1);
        intent = new Intent(inlineForms, TextLayoutInputFeildInlineForms.class);
        inlineForms.startActivity(intent);
        inlineForms.finish();
    }

    public void testIFDisabledTextColor() {
        onView(withId(R.id.diabaledtextfeild1))
                .check(matches(isTextColorSimilar("#b9b9b9")));
    }

    public void testIFDisabledTextSize() {
        onView(withId(R.id.diabaledtextfeild1))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testIFDisabledBGColor() {
        onView(withId(R.id.disabledlayout))
                .check(matches(isBackgroundColorSimilar("#efefef", 15, 15)));
    }

    public void testFocusedFieldTextViewColor() {
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.firstname)).check(matches(isTextColorSimilar("#f204b")));
    }

    public void testFocusedFieldTextSize() {
        onView(withId(R.id.firstname))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testFocusedFieldHintTextColor() {
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).check(matches(isHintTextColorSimilar("#b9b9b9")));
    }

    public void testFocusedFieldEnteredTextColorDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).perform(typeText("test@test.com"));
        onView(withId(R.id.firstnamevalue)).check(matches(isTextColorSimilar("#f204b")));
    }

    public void testFocusedFieldEnteredTextColorBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).perform(typeText("test@test.com"));
        onView(withId(R.id.firstnamevalue)).check(matches(isTextColorSimilar("#f204b")));
    }

    public void testFocusedFieldEnteredTextColorBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).perform(typeText("test@test.com"));
        onView(withId(R.id.firstnamevalue)).check(matches(isTextColorSimilar("#f204b")));
    }

    public void testUnFocusedFieldEnteredTextColorDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).perform(typeText("test@test.com"));
        onView(withId(R.id.lastnamelayouthorizontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).check(matches(isTextColorSimilar("#f204b")));
    }

    public void testUnFocusedFieldEnteredTextColorBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).perform(typeText("test@test.com"));
        onView(withId(R.id.lastnamelayouthorizontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).check(matches(isTextColorSimilar("#f204b")));
    }

    public void testUnFocusedFieldEnteredTextColorBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).perform(typeText("test@test.com"));
        onView(withId(R.id.lastnamelayouthorizontal))
                .perform(click());
        onView(withId(R.id.firstnamevalue)).check(matches(isTextColorSimilar("#f204b")));
    }


    public void testFocusedFieldIncorrectTextColorDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.lastnamelayouthorizontal))
                .perform(click());
        onView(withId(R.id.lastnamevalue)).perform(typeText("test"));
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.lastname)).check(matches(isTextColorSimilar("#e9830")));
        onView(withId(R.id.lastnamevalue)).check(matches(isTextColorSimilar("#e9830")));
        onView(withId(R.id.error_text)).check(matches(isTextColorSimilar("#e9830")));

    }


    public void testFocusedFieldIncorrectTextColorBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.lastnamelayouthorizontal))
                .perform(click());
        onView(withId(R.id.lastnamevalue)).perform(typeText("test"));
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.lastname)).check(matches(isTextColorSimilar("#cd202c")));
        onView(withId(R.id.lastnamevalue)).check(matches(isTextColorSimilar("#cd202c")));
        onView(withId(R.id.error_text)).check(matches(isTextColorSimilar("#cd202c")));

    }

    public void testFocusedFieldIncorrectTextColorBATheme() {
        themeUtils.setThemePreferences("aqua|false|solid|0");
        relaunchActivity();
        onView(withId(R.id.lastnamelayouthorizontal))
                .perform(click());
        onView(withId(R.id.lastnamevalue)).perform(typeText("test"));
        onView(withId(R.id.firstnamelayouthorzontal))
                .perform(click());
        onView(withId(R.id.lastname)).check(matches(isTextColorSimilar("#e9830")));
        onView(withId(R.id.lastnamevalue)).check(matches(isTextColorSimilar("#e9830")));
        onView(withId(R.id.error_text)).check(matches(isTextColorSimilar("#e9830")));

    }

    public void testHeightInlineForms() {
        onView(withId(R.id.disabledlayout)).check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.inline_form_field_height))));
        onView(withId(R.id.firstnamelayouthorzontal)).check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.inline_form_field_height))));
        onView(withId(R.id.lastnamelayouthorizontal)).check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.inline_form_field_height))));
    }

}
