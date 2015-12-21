import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.InputTextFieldsActivity;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.concurrent.Semaphore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHintTextColorAsExpectedMatcher.isHintTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsPixelAsExpectedMatcher.isImageSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;
import static org.hamcrest.Matchers.allOf;

/*
*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.

*/

public class InputFieldTest extends ActivityInstrumentationTestCase2<InputTextFieldsActivity> {

    Semaphore semaphore = new Semaphore(1);
    Activity targetActivity;
    private Resources testResources;
    private InputTextFieldsActivity inputTextFieldsActivity;
    private ThemeUtils themeUtils;

    public InputFieldTest() {

        super(InputTextFieldsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        inputTextFieldsActivity = getActivity();
        SharedPreferences preferences = inputTextFieldsActivity.getSharedPreferences(inputTextFieldsActivity.getString((R.string.app_name)), Context.MODE_PRIVATE);
        themeUtils = new ThemeUtils(preferences);
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    private void relaunchActivity() {
        Intent intent;
        inputTextFieldsActivity.setResult(1);
        intent = new Intent(inputTextFieldsActivity, InputTextFieldsActivity.class);
        inputTextFieldsActivity.startActivity(intent);
        inputTextFieldsActivity.finish();
    }

    public void testInputFieldUnfocusedWithoutEntry() {
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isOutlineColorSimilar("#b9b9b9")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isBackgroundColorSimilar("#ffffff", 5, 5)));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isHintTextColorSimilar("#b9b9b9")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testDBThemeInputFieldFocusedWithEntry() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("Hello There"));

        //Verify the UI parameters
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isOutlineColorSimilar("#03478")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isOutlineColorSimilar("#03478")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#f204b")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isBackgroundColorSimilar("#ffffff", 15, 15)));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testBOThemeInputFieldFocusedWithEntry() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("Hello There"));

        //Verify the UI parameters
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isOutlineColorSimilar("#e9830")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#f204b")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isBackgroundColorSimilar("#ffffff", 15, 15)));
    }

    public void testInputFieldUnFocusedWithEntry() {
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("test@test.com"));

        //Make the input field unfocused
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .perform(click());

        //Verify UI parameters for input field 1
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isOutlineColorSimilar("#b9b9b9")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#f204b")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isBackgroundColorSimilar("#ffffff", 15, 15)));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testDBThemeInputFieldWithIncorrectEntry() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .perform(click());

//        Verify UI parameters for input field 1 with incorrect entry
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isOutlineColorSimilar("#e9830")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#e9830")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isBackgroundColorSimilar("#ffffff", 15, 15)));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testBOThemeInputFieldWithIncorrectEntry() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .perform(click());

        //Verify UI parameters for input field 1 with incorrect entry
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isOutlineColorSimilar("#cd202c")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#cd202c")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isBackgroundColorSimilar("#ffffff", 15, 15)));
    }

    public void testDisableInputField() {
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_disabled))))
                .check(matches(isOutlineColorSimilar("#b9b9b9")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_disabled))))
                .check(matches(isHintTextColorSimilar("#b9b9b9")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_disabled))))
                .check(matches(isBackgroundColorSimilar("#efefef", 5, 5)));
    }

    public void testWarningIconDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.incorrect_icon_dbtheme);

        //Enter text in focused input field

        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .perform(click());

        //Verifying whether incorrect icon is pixel perfect
        onView(allOf(withId(R.id.error_image), withParent(withId(R.id.input_field_1))))
                .check(matches(isImageSimilar(expectedBitmap)));
    }

/*    public void testWarningIconBOTheme() {
        // Apply Bright Orange theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Orange Theme")).perform(click());
        pressBack();

        //Enter text in focused input field

        onView(withText("Input Text Fields")).perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .perform(click());

        //Verifying background color of incorrect icon
        onView(allOf(withId(R.id.error_image), withParent(withId(R.id.input_field_1))))
                .check(matches(isBackgroundColorSimilar("#cd202c")));
    }*/

    public void testWarningMessageDBTheme() {
        themeUtils.setThemePreferences("blue|false|solid|0");
        relaunchActivity();
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .perform(click());

        //Verify warning text
        onView(allOf(withId(R.id.error_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#e9830")));
        onView(allOf(withId(R.id.error_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testWarningMessageBOTheme() {
        themeUtils.setThemePreferences("orange|false|solid|0");
        relaunchActivity();
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .perform(click());

        //Verify warning text
        onView(allOf(withId(R.id.error_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#cd202c")));
        onView(allOf(withId(R.id.error_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testHeightInputFields() {
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_text_field_height))));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_text_field_height))));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_disabled))))
                .check(matches(isHeightSimilar((int) testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_text_field_height))));
    }

    public void testIncorrectMessagePushesContentDown() {

        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .perform(typeText("Hello There"));

        //Make the input field unfocused
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .perform(click());
        onView(allOf(withId(R.id.error_image), withParent(withId(R.id.input_field_1))))
                .check(isAbove(withId(R.id.input_field_2)));
    }
}

// Fonttype to be verified manually











