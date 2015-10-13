package com.philips.cdp.ui.catalog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.espresso.core.deps.guava.base.Verify;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.ui.catalog.activity.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.philips.cdp.ui.catalog.Matchers.IsBackgroundColorAsExpectedMatcher.isBackgroundColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsOutlineColorAsExpectedMatcher.isOutlineColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextColorAsExpectedMatcher.isTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHintTextColorAsExpectedMatcher.isHintTextColorSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsPixelAsExpectedMatcher.isImageSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsHeightAsExpectedMatcher.isHeightSimilar;
import static com.philips.cdp.ui.catalog.Matchers.IsTextSizeAsExpectedMatcher.isTextSizeSimilar;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static org.hamcrest.Matchers.allOf;

/*
*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.

*/

public class InputFieldTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Resources testResources;

    public InputFieldTest() {

        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        testResources = getInstrumentation().getContext().getResources();
    }

    public void testInputFieldUnfocusedWithoutEntry() {
        onView(withText("Input Text Fields")).perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isOutlineColorSimilar("#b9b9b9")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isHintTextColorSimilar("#b9b9b9")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testDBThemeInputFieldFocusedWithEntry() {
        // Apply Dark blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        //Enter text in focused input field
        onView(withText("Input Text Fields")).perform(click());
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
                .check(matches(isBackgroundColorSimilar("#ffffff")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testBOThemeInputFieldFocusedWithEntry() {
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

        //Verify the UI parameters
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isOutlineColorSimilar("#e9830")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#f204b")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
    }

    public void testInputFieldUnFocusedWithEntry() {
        //Enter text in focused input field

        onView(withText("Input Text Fields")).perform(click());
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
                .check(matches(isBackgroundColorSimilar("#ffffff")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    public void testDBThemeInputFieldWithIncorrectEntry() {
        // Apply Dark blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
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

//        Verify UI parameters for input field 1 with incorrect entry
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isOutlineColorSimilar("#e9830")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#e9830")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

    /*public void testBOThemeInputFieldWithIncorrectEntry() {
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

        //Verify UI parameters for input field 1 with incorrect entry
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isOutlineColorSimilar("#cd202c")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#cd202c")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isBackgroundColorSimilar("#ffffff")));
    }*/

    public void testDisableInputField() {
        //Verifying disabled field color codes

        onView(withText("Input Text Fields")).perform(click());
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_disabled))))
                .check(matches(isOutlineColorSimilar("#b9b9b9")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_disabled))))
                .check(matches(isHintTextColorSimilar("#b9b9b9")));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_disabled))))
                .check(matches(isBackgroundColorSimilar("#efefef")));
    }

    public void testWarningIconDBTheme() {
        // Apply Dark Blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
        pressBack();

        Bitmap expectedBitmap = BitmapFactory.decodeResource(testResources, com.philips.cdp.ui.catalog.test.R.drawable.incorrect_icon_dbtheme);

        //Enter text in focused input field

        onView(withText("Input Text Fields")).perform(click());
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

    /*public void testWarningIconBOTheme() {
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
                .check(matches(isOutlineColorSimilar("#cd202c")));
    }*/

    public void testWarningMessageDBTheme() {
        // Apply Dark Blue theme
        onView(withText("Change Theme")).perform(click());
        onView(withText("Blue Theme")).perform(click());
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

        //Verify warning text
        onView(allOf(withId(R.id.error_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#e9830")));
        onView(allOf(withId(R.id.error_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }

   /* public void testWarningMessageBOTheme() {
         Apply Bright orange theme
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

        //Verify warning text
        onView(allOf(withId(R.id.error_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextColorSimilar("#cd202c")));
        onView(allOf(withId(R.id.error_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isTextSizeSimilar(testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_field_text_size))));
    }*/

    public void testHeightInputFields() {

        onView(withText("Input Text Fields")).perform(click());

        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_1))))
                .check(matches(isHeightSimilar((int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_text_field_height))));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_2))))
                .check(matches(isHeightSimilar((int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_text_field_height))));
        onView(allOf(withId(R.id.edit_text), withParent(withId(R.id.input_field_disabled))))
                .check(matches(isHeightSimilar((int)testResources.getDimension(com.philips.cdp.ui.catalog.test.R.dimen.input_text_field_height))));
    }

    public void testIncorrectMessagePushesContentDown() {

        onView(withText("Input Text Fields")).perform(click());
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











